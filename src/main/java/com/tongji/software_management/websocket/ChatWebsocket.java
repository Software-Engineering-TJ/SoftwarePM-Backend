package com.tongji.software_management.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.tongji.software_management.entity.DBEntity.ChoiceQuestion;
import com.tongji.software_management.entity.DBEntity.Practice;
import com.tongji.software_management.entity.DBEntity.PracticeScore;
import com.tongji.software_management.entity.LogicalEntity.*;
import com.tongji.software_management.error.GameServerError;
import com.tongji.software_management.interceptor.GameServerException;
import com.tongji.software_management.service.ChoiceQuestionService;
import com.tongji.software_management.service.PracticeScoreService;
import com.tongji.software_management.service.PracticeService;
import com.tongji.software_management.utils.MatchCacheUtil;
import com.tongji.software_management.utils.MessageCode;
import com.tongji.software_management.utils.MessageTypeEnum;
import com.tongji.software_management.utils.StatusEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


@Component
@Slf4j
@ServerEndpoint(value = "/game/match/{userId}")
public class ChatWebsocket {

    private Session session;

    private String userId; //对应student表中的student_number
    private int contestId; //对应practice表中的practice_id
    static MatchCacheUtil matchCacheUtil;
    static Lock lock = new ReentrantLock();
    static Condition matchCond = lock.newCondition();

    static PracticeService practiceService;
    static ChoiceQuestionService choiceQuestionService;
    static PracticeScoreService practiceScoreService;


    @Autowired
    public void setMatchCacheUtil(MatchCacheUtil matchCacheUtil) {
        ChatWebsocket.matchCacheUtil = matchCacheUtil;
    }

    @Autowired
    public void setPracticeService(PracticeService practiceService) {
        ChatWebsocket.practiceService = practiceService;
    }

    @Autowired
    public void setChoiceQuestionService(ChoiceQuestionService choiceQuestionService) {
        ChatWebsocket.choiceQuestionService = choiceQuestionService;
    }

    @Autowired
    public void setPracticeScoreService(PracticeScoreService practiceScoreService) {
        ChatWebsocket.practiceScoreService = practiceScoreService;
    }

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {

        log.info("ChatWebsocket open 有新连接加入 userId: {}", userId);

        this.userId = userId;
        this.session = session;
        matchCacheUtil.addClient(userId, this);

        log.info("ChatWebsocket open 连接建立完成 userId: {}", userId);
    }

    @OnError
    public void onError(Session session, Throwable error) {

        log.error("ChatWebsocket onError 发生了错误 userId: {}, errorMessage: {}", userId, error.getMessage());

        matchCacheUtil.removeClinet(userId);
        matchCacheUtil.removeUserOnlineStatus(userId);
        matchCacheUtil.removeUserFromRoom(userId);
        matchCacheUtil.removeUserMatchInfo(userId);
        matchCacheUtil.removeUserContestInfo(userId);
        log.info("ChatWebsocket onError 连接断开完成 userId: {}", userId);
    }

    @OnClose
    public void onClose()
    {
        log.info("ChatWebsocket onClose 连接断开 userId: {}", userId);

        matchCacheUtil.removeClinet(userId);
        matchCacheUtil.removeUserOnlineStatus(userId);
        matchCacheUtil.removeUserFromRoom(userId);
        matchCacheUtil.removeUserMatchInfo(userId);
        matchCacheUtil.removeUserContestInfo(userId);

        log.info("ChatWebsocket onClose 连接断开完成 userId: {}", userId);
    }

    @OnMessage
    public void onMessage(String message, Session session) {

        log.info("ChatWebsocket onMessage userId: {}, 来自客户端的消息 message: {}", userId, message);

        JSONObject jsonObject = JSON.parseObject(message);

        System.out.println(jsonObject);

        MessageTypeEnum type = jsonObject.getObject("type", MessageTypeEnum.class);

        System.out.println(type.toString());

        log.info("ChatWebsocket onMessage userId: {}, 来自客户端的消息类型 type: {}", userId, type);

        if (type == MessageTypeEnum.ADD_USER) {
            addUser(jsonObject);
        } else if (type == MessageTypeEnum.MATCH_USER) {
            matchUser(jsonObject);
        } else if (type == MessageTypeEnum.CANCEL_MATCH) {
            cancelMatch(jsonObject);
        } else if (type == MessageTypeEnum.PLAY_GAME) {
            toPlay(jsonObject);
        } else if (type == MessageTypeEnum.GAME_OVER) {
            gameover(jsonObject);
        } else {
            throw new GameServerException(GameServerError.WEBSOCKET_ADD_USER_FAILED);
        }

        log.info("ChatWebsocket onMessage userId: {} 消息接收结束", userId);
    }

    /**
     * 群发消息
     */
    private void sendMessageAll(MessageReply<?> messageReply) {

        log.info("ChatWebsocket sendMessageAll 消息群发开始 userId: {}, messageReply: {}", userId, JSON.toJSONString(messageReply));

        Set<String> receivers = messageReply.getChatMessage().getReceivers();
        for (String receiver : receivers) {
            ChatWebsocket client = matchCacheUtil.getClient(receiver);
            client.session.getAsyncRemote().sendText(JSON.toJSONString(messageReply));
        }

        log.info("ChatWebsocket sendMessageAll 消息群发结束 userId: {}", userId);
    }

    /**
     * 用户加入游戏， jsonObject中必须有contestId
     */
    private void addUser(JSONObject jsonObject) {

        log.info("ChatWebsocket addUser 用户加入游戏开始 message: {}, userId: {}", jsonObject.toJSONString(), userId);
        int cid = jsonObject.getInteger("contestId");
        contestId = cid;
        MessageReply<Object> messageReply = new MessageReply<>();
        ChatMessage<Object> result = new ChatMessage<>();
        result.setType(MessageTypeEnum.ADD_USER);
        result.setSender(userId);

        /*
         * 获取用户的在线状态
         * 如果缓存中没有保存用户状态，表示用户新加入，则设置为在线状态和对抗练习加入信息
         * 否则直接返回
         */
        StatusEnum status = matchCacheUtil.getUserOnlineStatus(userId);
        if (status != null) {
            /*
             * 游戏结束状态，重新设置为在线状态和对抗练习加入信息
             * 否则返回错误提示信息
             */
            if (status.compareTo(StatusEnum.GAME_OVER) == 0) {
                messageReply.setCode(MessageCode.SUCCESS.getCode());
                messageReply.setDesc(MessageCode.SUCCESS.getDesc());
                matchCacheUtil.setUserIDLE(userId);
                matchCacheUtil.setUserContestInfo(userId,String.valueOf(cid));

            } else {
                messageReply.setCode(MessageCode.USER_IS_ONLINE.getCode());
                messageReply.setDesc(MessageCode.USER_IS_ONLINE.getDesc());
            }
        } else {
            messageReply.setCode(MessageCode.SUCCESS.getCode());
            messageReply.setDesc(MessageCode.SUCCESS.getDesc());
            matchCacheUtil.setUserIDLE(userId);
            matchCacheUtil.setUserContestInfo(userId,String.valueOf(cid));
        }

        Set<String> receivers = new HashSet<>();
        receivers.add(userId);
        result.setReceivers(receivers);
        messageReply.setChatMessage(result);

        sendMessageAll(messageReply);

        log.info("ChatWebsocket addUser 用户加入游戏结束 message: {}, userId: {}", jsonObject.toJSONString(), userId);

    }

    /**
     * 用户随机匹配对手
     */
    @SneakyThrows
    private void matchUser(JSONObject jsonObject) {

        log.info("ChatWebsocket matchUser 用户随机匹配对手开始 message: {}, userId: {}", jsonObject.toJSONString(), userId);
        int cid = jsonObject.getInteger("contestId");
        MessageReply<GameMatchInfo> messageReply = new MessageReply<>();
        ChatMessage<GameMatchInfo> result = new ChatMessage<>();
        result.setSender(userId);
        result.setType(MessageTypeEnum.MATCH_USER);

        lock.lock();
        try {
            // 设置用户状态为匹配中
            matchCacheUtil.setUserInMatch(userId);
            matchCond.signal();
        } finally {
            lock.unlock();
        }

        // 创建一个异步线程任务，负责匹配其他同样处于匹配状态的其他用户
        Thread matchThread = new Thread(() -> {
            boolean flag = true;
            String receiver = null;
            String receiver2 = null;
            Integer i = 0;
            while (flag) {
                // 获取除自己以外的其他待匹配用户
                lock.lock();
                i++;
                try {
                    // 当前用户不处于待匹配状态
                    if (matchCacheUtil.getUserOnlineStatus(userId).compareTo(StatusEnum.IN_GAME) == 0
                            || matchCacheUtil.getUserOnlineStatus(userId).compareTo(StatusEnum.GAME_OVER) == 0) {
                        log.info("ChatWebsocket matchUser 当前用户 {} 已退出匹配", userId);
                        return;
                    }
                    // 当前用户取消匹配状态
                    if (matchCacheUtil.getUserOnlineStatus(userId).compareTo(StatusEnum.IDLE) == 0) {
                        // 当前用户取消匹配
                        messageReply.setCode(MessageCode.CANCEL_MATCH_ERROR.getCode());
                        messageReply.setDesc(MessageCode.CANCEL_MATCH_ERROR.getDesc());
                        Set<String> set = new HashSet<>();
                        set.add(userId);
                        result.setReceivers(set);
                        result.setType(MessageTypeEnum.CANCEL_MATCH);
                        messageReply.setChatMessage(result);
                        log.info("ChatWebsocket matchUser 当前用户 {} 已退出匹配", userId);
                        sendMessageAll(messageReply);
                        return;
                    }
                    receiver = matchCacheUtil.getUserInMatchRandom(userId,String.valueOf(cid));
                    receiver2 = matchCacheUtil.getUserInMatchRandom(userId,String.valueOf(cid),receiver);
                    if (receiver != null&&receiver2!=null) {
                        // 对手不处于待匹配状态
                        if (matchCacheUtil.getUserOnlineStatus(receiver).compareTo(StatusEnum.IN_MATCH) != 0) {
                            log.info("ChatWebsocket matchUser 当前用户 {}, 匹配对手 {} 已退出匹配状态", userId, receiver);
                        }
                        else if (matchCacheUtil.getUserOnlineStatus(receiver2).compareTo(StatusEnum.IN_MATCH) != 0) {
                            log.info("ChatWebsocket matchUser 当前用户 {}, 匹配对手 {} 已退出匹配状态", userId, receiver2);
                        }
                        else {
                            matchCacheUtil.setUserInGame(userId);
                            matchCacheUtil.setUserInGame(receiver);
                            matchCacheUtil.setUserInGame(receiver2);
                            matchCacheUtil.setUserInRoom(userId, receiver,receiver2);

                            flag = false;
                        }
                    } else {
                        // 如果当前没有待匹配用户，进入等待队列
                        try {
                            log.info("ChatWebsocket matchUser 当前用户 {} 无对手可匹配", userId);
                            long nanos = TimeUnit.SECONDS.toNanos(500); // 20s
                            matchCond.awaitNanos(nanos);
                        } catch (InterruptedException e) {
                            log.error("ChatWebsocket matchUser 匹配线程 {} 发生异常: {}",
                                    Thread.currentThread().getName(), e.getMessage());
                        }
                    }
                } finally {
                    lock.unlock();
                }
            }

            UserMatchInfo senderInfo = new UserMatchInfo();
            UserMatchInfo receiverInfo = new UserMatchInfo();
            UserMatchInfo receiver2Info = new UserMatchInfo();
            senderInfo.setUserId(userId);
            senderInfo.setScore(0);
            receiverInfo.setUserId(receiver);
            receiverInfo.setScore(0);
            receiver2Info.setUserId(receiver);
            receiver2Info.setScore(0);

            matchCacheUtil.setUserMatchInfo(userId, JSON.toJSONString(senderInfo));
            matchCacheUtil.setUserMatchInfo(receiver, JSON.toJSONString(receiverInfo));
            matchCacheUtil.setUserMatchInfo(receiver2, JSON.toJSONString(receiver2Info));

            GameMatchInfo gameMatchInfo = new GameMatchInfo();

            // 获取题目(5道题）
            List<ChoiceQuestion> questions = new ArrayList<ChoiceQuestion>();
            String[] choiceIdArray = practiceService.get(cid).getChoiceId().split(",");
            for(String choiceId : choiceIdArray) {
                questions.add(choiceQuestionService.get(Integer.parseInt(choiceId)));
            }
            gameMatchInfo.setQuestions(questions);

            gameMatchInfo.setSelfInfo(senderInfo);
            List<UserMatchInfo> opponentInfo =  new ArrayList<>();
            opponentInfo.add(receiverInfo);
            opponentInfo.add(receiver2Info);
            gameMatchInfo.setOpponentInfo(opponentInfo);

            messageReply.setCode(MessageCode.SUCCESS.getCode());
            messageReply.setDesc(MessageCode.SUCCESS.getDesc());
            // 发给user
            result.setData(gameMatchInfo);
            Set<String> set = new HashSet<>();
            set.add(userId);
            result.setReceivers(set);
            result.setType(MessageTypeEnum.MATCH_USER);
            messageReply.setChatMessage(result);
            sendMessageAll(messageReply);
            // 发给receiver1
            opponentInfo.clear();
            opponentInfo.add(senderInfo);
            opponentInfo.add(receiver2Info);
            gameMatchInfo.setSelfInfo(receiverInfo);
            gameMatchInfo.setOpponentInfo(opponentInfo);

            result.setData(gameMatchInfo);
            set.clear();
            set.add(receiver);
            result.setReceivers(set);
            messageReply.setChatMessage(result);

            sendMessageAll(messageReply);
            // 发给receive2
            opponentInfo.clear();
            opponentInfo.add(senderInfo);
            opponentInfo.add(receiverInfo);
            gameMatchInfo.setSelfInfo(receiver2Info);
            gameMatchInfo.setOpponentInfo(opponentInfo);

            result.setData(gameMatchInfo);
            set.clear();
            set.add(receiver2);
            result.setReceivers(set);
            messageReply.setChatMessage(result);

            sendMessageAll(messageReply);

            log.info("ChatWebsocket matchUser 用户随机匹配对手结束 messageReply: {}", JSON.toJSONString(messageReply));

        }, "MatchTask -- " + userId);
        matchThread.start();
    }

    /**
     * 取消匹配
     */
    private void cancelMatch(JSONObject jsonObject) {

        log.info("ChatWebsocket cancelMatch 用户取消匹配开始 userId: {}, message: {}", userId, jsonObject.toJSONString());

        lock.lock();
        try {
            matchCacheUtil.setUserIDLE(userId);
        } finally {
            lock.unlock();
        }

        log.info("ChatWebsocket cancelMatch 用户取消匹配结束 userId: {}", userId);
    }

    /**
     * 游戏中
     */
    @SneakyThrows
    public void toPlay(JSONObject jsonObject) {

        log.info("ChatWebsocket toPlay 用户更新对局信息开始 userId: {}, message: {}", userId, jsonObject.toJSONString());

        MessageReply<UserMatchInfo> messageReply = new MessageReply<>();

        ChatMessage<UserMatchInfo> result = new ChatMessage<>();
        result.setSender(userId);
        String[] receivers = matchCacheUtil.getUserFromRoom(userId).split("#");
        String receiver = null;
        String receiver2 = null;


        receiver = receivers[0];
        if(receivers.length>=2){
            receiver2 = receivers[1];
        }

        Set<String> set = new HashSet<>();
        set.add(receiver);
        if(receiver2!=null) {
            set.add(receiver2);
        }
        result.setReceivers(set);
        result.setType(MessageTypeEnum.PLAY_GAME);

        Integer newScore = jsonObject.getInteger("data");
        UserMatchInfo userMatchInfo = new UserMatchInfo();
        userMatchInfo.setUserId(userId);
        userMatchInfo.setScore(newScore);

        matchCacheUtil.setUserMatchInfo(userId, JSON.toJSONString(userMatchInfo));

        result.setData(userMatchInfo);
        messageReply.setCode(MessageCode.SUCCESS.getCode());
        messageReply.setDesc(MessageCode.SUCCESS.getDesc());
        messageReply.setChatMessage(result);

        sendMessageAll(messageReply);

        log.info("ChatWebsocket toPlay 用户更新对局信息结束 userId: {}, userMatchInfo: {}", userId, JSON.toJSONString(userMatchInfo));
    }

    /**
     * 游戏结束
     */
    public void gameover(JSONObject jsonObject) {

        log.info("ChatWebsocket gameover 用户对局结束 userId: {}, message: {}", userId, jsonObject.toJSONString());
        // 保存完成时间和做题成绩到缓存中
        MessageReply<UserMatchInfo> messageReply = new MessageReply<>();
        Timestamp finishTime = new Timestamp(System.currentTimeMillis());
        Integer newScore = jsonObject.getInteger("data");
        UserMatchInfo userMatchInfo = new UserMatchInfo();
        userMatchInfo.setUserId(userId);
        userMatchInfo.setContestId(String.valueOf(contestId));
        userMatchInfo.setScore(newScore);
        userMatchInfo.setTime(finishTime);
        matchCacheUtil.setUserMatchInfo(userId, JSON.toJSONString(userMatchInfo));

        ChatMessage<UserMatchInfo> result = new ChatMessage<>();
        result.setSender(userId);
        String[] receivers = matchCacheUtil.getUserFromRoom(userId).split("#");
        String receiver = null;
        String receiver2 = null;
        receiver = receivers[0];
        if(receivers.length>=2){
            receiver2 = receivers[1];
        }
        result.setType(MessageTypeEnum.GAME_OVER);

        lock.lock();
        try {
            matchCacheUtil.setUserGameover(userId);
            if (matchCacheUtil.getUserOnlineStatus(receiver).compareTo(StatusEnum.GAME_OVER) == 0&&matchCacheUtil.getUserOnlineStatus(receiver2).compareTo(StatusEnum.GAME_OVER) == 0) {
                String receiverInfo = matchCacheUtil.getUserMatchInfo(receiver);
                String receiver2Info = matchCacheUtil.getUserMatchInfo(receiver2);
                UserMatchInfo receiverMatchInfo = JSON.parseObject(receiverInfo, UserMatchInfo.class);
                UserMatchInfo receiver2MatchInfo = JSON.parseObject(receiver2Info, UserMatchInfo.class);
                //计算得分
                UserMatchInfo[] userMatchInfos = new UserMatchInfo[3];
                userMatchInfos[0] = userMatchInfo;
                userMatchInfos[1] = receiverMatchInfo;
                userMatchInfos[2] = receiver2MatchInfo;
                Arrays.sort(userMatchInfos);
                userMatchInfos[0].setScore(60);
                userMatchInfos[1].setScore(60);
                userMatchInfos[2].setScore(100);

                //发送消息
                messageReply.setCode(MessageCode.SUCCESS.getCode());
                messageReply.setDesc(MessageCode.SUCCESS.getDesc());

                result.setData(userMatchInfo);
                messageReply.setChatMessage(result);
                Set<String> set = new HashSet<>();
                set.add(userId);
                result.setReceivers(set);
                sendMessageAll(messageReply);


                result.setData(receiverMatchInfo);
                messageReply.setChatMessage(result);
                set.clear();
                set.add(receiver);
                result.setReceivers(set);
                sendMessageAll(messageReply);


                result.setData(receiver2MatchInfo);
                messageReply.setChatMessage(result);
                set.clear();
                set.add(receiver2);
                result.setReceivers(set);
                sendMessageAll(messageReply);


                matchCacheUtil.removeUserMatchInfo(userId);
                matchCacheUtil.removeUserFromRoom(userId);
                matchCacheUtil.removeUserContestInfo(userId);
                //保存得分
                for(UserMatchInfo u : userMatchInfos) {
                    PracticeScore practiceScore = new PracticeScore();
                    int pid = Integer.parseInt(u.getContestId());
                    Practice practice = practiceService.get(pid);
                    practiceScore.setStudentNumber(u.getUserId());
                    practiceScore.setPracticeName(practice.getPracticeName());
                    practiceScore.setCourseId(practice.getCourseId());
                    practiceScore.setClassId(practice.getClassId());
                    practiceScore.setIndividualScore(u.getScore().doubleValue());
                    practiceScore.setIndividualTime(u.getTime());
                    practiceScoreService.insert(practiceScore);
                }

            }
        }  finally {
            lock.unlock();
        }
        log.info("ChatWebsocket gameover 对局 [{} - {} - {}] 结束", userId, receiver, receiver2);
    }
}

package com.tongji.software_management.redis;

/**
 * Redis 存储 key 的枚举
 *
 * @author yeeq
 * @date 2021/2/27
 */
public enum EnumRedisKey {

    /**
     * userOnline 在线状态
     */
    USER_STATUS,
    /**
     * userOnline 匹配信息
     */
    USER_MATCH_INFO,
    /**
     * 房间
     */
    ROOM,
    /**
     * 加入对抗练习信息，用户-contestId
     */
    USER_CONTEST_INFO
     ;

    public String getKey() {
        return this.name();
    }
}
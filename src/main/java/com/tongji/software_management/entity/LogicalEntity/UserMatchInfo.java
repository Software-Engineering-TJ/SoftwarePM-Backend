package com.tongji.software_management.entity.LogicalEntity;
import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;


@Data
public class UserMatchInfo implements Comparable<UserMatchInfo>{

    private String userId;
    private String contestId;
    private Integer score;
    private Timestamp time;

    @Override
    public int compareTo(UserMatchInfo o){
        Integer x = this.score.compareTo(o.getScore());
        if (x==0){
            return o.time.compareTo(this.time);
        }
        return x;
    }
}
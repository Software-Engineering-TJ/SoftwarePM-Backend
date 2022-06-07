package com.tongji.software_management.entity.LogicalEntity;
import lombok.Data;

import java.util.List;


@Data
public class GameMatchInfo {

    private UserMatchInfo selfInfo;
    private List<UserMatchInfo> opponentInfo;
//    private List<Exercise> exercises;
}
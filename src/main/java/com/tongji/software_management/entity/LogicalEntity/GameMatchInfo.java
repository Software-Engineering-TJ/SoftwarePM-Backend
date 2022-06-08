package com.tongji.software_management.entity.LogicalEntity;
import com.tongji.software_management.entity.DBEntity.ChoiceQuestion;
import lombok.Data;

import java.util.List;


@Data
public class GameMatchInfo {

    private UserMatchInfo selfInfo;
    private List<UserMatchInfo> opponentInfo;
    private List<ChoiceQuestion> questions;
}
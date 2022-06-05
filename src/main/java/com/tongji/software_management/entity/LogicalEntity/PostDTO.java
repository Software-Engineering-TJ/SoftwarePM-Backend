package com.tongji.software_management.entity.LogicalEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostDTO {
    private int id;
    private String studentNumber;
    private String studentName;
    private String title;
    private String content;
    private int type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp date;
}

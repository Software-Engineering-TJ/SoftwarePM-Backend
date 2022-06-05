package com.tongji.software_management.entity.LogicalEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class PostCommentDTO {
    private int id;
    private int postId;
    private String studentNumber;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp commentTime;

    private String studentName;
}

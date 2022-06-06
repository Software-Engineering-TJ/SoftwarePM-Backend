package com.tongji.software_management.entity.LogicalEntity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ResourceDTO {
    private int id;
    private String instructorNumber;
    private String url;
    private String title;
    private int type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Timestamp uploadTime;

    private String instructorName;
    private String fileSize;
}

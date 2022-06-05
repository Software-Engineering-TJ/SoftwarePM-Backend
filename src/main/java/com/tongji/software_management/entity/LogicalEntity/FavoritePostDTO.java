package com.tongji.software_management.entity.LogicalEntity;

import lombok.Data;

@Data
public class FavoritePostDTO {
    private int id;
    private String studentNumber;
    private int postId;

    private String postTitle;
    private int postType;
    private String studentName;
}

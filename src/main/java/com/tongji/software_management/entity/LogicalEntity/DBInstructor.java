package com.tongji.software_management.entity.LogicalEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DBInstructor {
    private String instructorNumber;
    private String email;
    private String password;
    private String name;
    private int sex;
    private String phoneNumber;
    private int status;
}

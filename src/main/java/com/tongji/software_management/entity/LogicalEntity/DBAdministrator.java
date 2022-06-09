package com.tongji.software_management.entity.LogicalEntity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DBAdministrator {
    private String adminNumber;
    private String email;
    private String password;
    private String name;
}

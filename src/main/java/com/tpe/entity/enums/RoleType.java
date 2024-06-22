package com.tpe.entity.enums;

import lombok.Getter;

@Getter
public enum RoleType {

    ADMIN("Admin"),
    STAFF("Staff"),
    EMPLOYEE("Employee");


    public final String name;

    RoleType(String name) {
        this.name = name;
    }

}

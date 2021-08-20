package com.webank.scaffold.enums;

import lombok.Getter;

/**
 * @author aaronchu
 * @Description
 * @date 2021/08/10
 */

public enum ProjectType {
    Gradle("gradle"),

    Maven("maven");

    public String getName() {
        return name;
    }

    private String name;

    ProjectType(String name){
        this.name = name;
    }

    public static ProjectType nameOf(String projectType) {
        for(ProjectType projectTypeEnum:ProjectType.values()){
            if(projectTypeEnum.getName().equals(projectType.toLowerCase())){
                return projectTypeEnum;
            }
        }
        return null;
    }
}

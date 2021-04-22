package com.webank.scaffold.config;

import lombok.Data;

/**
 * Config files for users
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */

@Data
public class UserConfig{

    private String group;
    private String artifact;
    private String solidityDir;
    private String outputDir;
    private String need;

    private UserConfig(){}

    public static UserConfig getInstance(String group, String artifact, String solidityDir, String outputDir, String need){

        UserConfig config = new UserConfig();
        config.setGroup(group);
        config.setArtifact(artifact);
        config.setSolidityDir(solidityDir);
        config.setOutputDir(outputDir);
        config.setNeed(need);

        return config;
    }
}

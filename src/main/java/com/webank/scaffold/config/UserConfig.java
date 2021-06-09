package com.webank.scaffold.config;

/**
 * Config files for users
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */

public class UserConfig{

    private String group;
    private String artifact;
    private String solidityDir;
    private String outputDir;
    private String need;
    private String gradle;

    public String getGradle() {
        return gradle;
    }

    private UserConfig(){}

    public static UserConfig getInstance(String group, String artifact, String solidityDir, String outputDir, String need, String gradle){

        UserConfig config = new UserConfig();
        config.setGroup(group);
        config.setArtifact(artifact);
        config.setSolidityDir(solidityDir);
        config.setOutputDir(outputDir);
        config.setNeed(need);
        config.setGradle(gradle);
        return config;
    }

    private void setGradle(String gradle) {
        this.gradle = gradle;
    }

    /**
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return the artifact
     */
    public String getArtifact() {
        return artifact;
    }

    /**
     * @param artifact the artifact to set
     */
    public void setArtifact(String artifact) {
        this.artifact = artifact;
    }

    /**
     * @return the solidityDir
     */
    public String getSolidityDir() {
        return solidityDir;
    }

    /**
     * @param solidityDir the solidityDir to set
     */
    public void setSolidityDir(String solidityDir) {
        this.solidityDir = solidityDir;
    }

    /**
     * @return the outputDir
     */
    public String getOutputDir() {
        return outputDir;
    }

    /**
     * @param outputDir the outputDir to set
     */
    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    /**
     * @return the need
     */
    public String getNeed() {
        return need;
    }

    /**
     * @param need the need to set
     */
    public void setNeed(String need) {
        this.need = need;
    }
    
    
}

package com.webank.scaffold.factory;

import com.webank.scaffold.artifact.BuildGradle;
import com.webank.scaffold.artifact.GradleDir;
import com.webank.scaffold.artifact.MainDir;
import com.webank.scaffold.artifact.ProjectArtifact;
import com.webank.scaffold.artifact.SettingsGradle;
import com.webank.scaffold.artifact.SrcDir;
import com.webank.scaffold.artifact.TestDir;
import com.webank.scaffold.artifact.TestJavaDir;
import com.webank.scaffold.artifact.webase.NewGradleDir;
import com.webank.scaffold.artifact.webase.NewMainDir;
import com.webank.scaffold.artifact.webase.NewMainResourceDir.ContractInfo;
import com.webank.scaffold.config.GeneratorOptions;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.exception.ScaffoldException;
import java.io.File;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class ProjectFactory {

    private static final Logger logger = LoggerFactory.getLogger(ProjectFactory.class);

    /**
     * Build project, which can be ran by users by using gradle test.
     */
    public ProjectArtifact buildProjectDir(String solidityDir, String group, String artifact, String outputDir, String need){
        /**
         * 1. Create UserConfig object
         */
        UserConfig config = getUserConfig(group, artifact);
        /**
         * 2. Create root project directory
         */
        ProjectArtifact project = buildProjectDir(outputDir, config);

        /**
         * 3. Create sub contents in project
         */
        try{
            createSubContents(need, project, solidityDir, config);
            System.out.println("Project build complete:"+project.toFile());
        }
        catch (Exception ex){
            ex.printStackTrace();
            /**
             * 4. Delete all project data in case of exception
             */
            try{
                project.clean();
            }
            catch (Exception e){}
        }
        return project;
    }


    /**
     * use raw content to generate project
     * @param contractInfoList
     * @param group
     * @param artifact
     * @param outputDir
     * @param systemPeers can be null
     * @param groupId can be null
     * @param hexPrivateKey can be null
     * @param sdkContentMap can be null
     * @return
     */
    public ProjectArtifact buildProjectDir(List<ContractInfo> contractInfoList,
        String group, String artifact, String outputDir, String gradleDir,
        String systemPeers, Integer groupId, String hexPrivateKey, Map<String, String> sdkContentMap)
        throws Exception {
        /**
         * 1. Create UserConfig object
         */
        UserConfig config = getUserConfig(group, artifact);
        /**
         * 2. Create root project directory
         */
        ProjectArtifact project = buildProjectDir(outputDir, config);

        /**
         * 3. Create sub contents in project
         */
        try {
            createSubContents(project, config, contractInfoList, gradleDir,
                systemPeers, groupId, hexPrivateKey, sdkContentMap);
            logger.info("Project build complete:" + project.toFile());
        }
        catch (Exception ex){
            logger.error("buildProjectDir param failed:[]", ex);
            /**
             * 4. Delete all project data in case of exception
             */
            try {
                project.clean();
            }
            catch (Exception e){}
            throw ex;
        }
        return project;
    }


    private ProjectArtifact buildProjectDir(String outputDir, UserConfig config){
        ProjectArtifact project = new ProjectArtifact(new File(outputDir), config);
        if(project.toFile().exists()){
            throw new ScaffoldException("Project is not clean, please remove the directory first:"+project.toFile().getAbsolutePath());
        }
        return project;
    }

    private UserConfig getUserConfig(String group, String artifact){

        UserConfig config = new UserConfig();
        config.put(GeneratorOptions.GENERATOR_GROUP, group);
        config.put(GeneratorOptions.GENERATOR_ARTIFACT, artifact);

        return config;
    }

    private void createSubContents(String need, ProjectArtifact project, String solidityDir, UserConfig config)
    throws Exception {
        SrcDir srcDir = new SrcDir(project.toFile());
        MainDir mainDir = new MainDir(need, srcDir.toFile(), new File(solidityDir), config);
        TestDir testDir = new TestDir(srcDir.toFile());
        TestJavaDir testJavaDir = new TestJavaDir(testDir.toFile(), config);
        BuildGradle buildGradle = new BuildGradle(project.toFile(), config);
        SettingsGradle settingsGradle = new SettingsGradle(project.toFile(), config);
        GradleDir gradle = new GradleDir(project.toFile());
        project.generate();
        srcDir.generate();
        mainDir.generate();
        testDir.generate();
        testJavaDir.generate();
        buildGradle.generate();
        settingsGradle.generate();
        gradle.generate();
    }

    private void createSubContents(ProjectArtifact project, UserConfig config,
        List<ContractInfo> contractInfoList, String gradleDir,
        String systemPeers, Integer groupId, String hexPrivateKey, Map<String, String> sdkContentMap)
        throws Exception {
        SrcDir srcDir = new SrcDir(project.toFile());
        NewMainDir mainDir = new NewMainDir(srcDir.toFile(), config, contractInfoList,
            systemPeers, groupId, hexPrivateKey, sdkContentMap);
        TestDir testDir = new TestDir(srcDir.toFile());
        TestJavaDir testJavaDir = new TestJavaDir(testDir.toFile(), config);
        BuildGradle buildGradle = new BuildGradle(project.toFile(), config);
        SettingsGradle settingsGradle = new SettingsGradle(project.toFile(), config);

        project.generate();
        srcDir.generate();
        mainDir.generate();
        testDir.generate();
        testJavaDir.generate();
        buildGradle.generate();
        settingsGradle.generate();
        // gradle wrapper if needed
        if (StringUtils.isNotBlank(gradleDir)) {
            logger.info("copy gradle wrapper from :{}", gradleDir);
            NewGradleDir gradle = new NewGradleDir(project.toFile(), gradleDir);
            gradle.generate();
        } else {
            logger.info("gradleDir is blank, skip copy gradle wrapper");
        }
    }


}

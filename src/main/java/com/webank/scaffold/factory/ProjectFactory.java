package com.webank.scaffold.factory;

import com.webank.scaffold.artifact.*;
import com.webank.scaffold.artifact.webase.NewMainDir;
import com.webank.scaffold.artifact.webase.NewMainDir.SolInfo;
import com.webank.scaffold.artifact.webase.NewMainResourceDir.ContractInfo;
import com.webank.scaffold.config.GeneratorOptions;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.exception.ScaffoldException;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class ProjectFactory {

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
     * @param solList
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
    public ProjectArtifact buildProjectDir(List<SolInfo> solList, List<ContractInfo> contractInfoList,
        String group, String artifact, String outputDir,
        String systemPeers, Integer groupId, String hexPrivateKey, Map<String, String> sdkContentMap) {
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
            createSubContents(project, config, solList, contractInfoList,
                systemPeers, groupId, hexPrivateKey, sdkContentMap);
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
        List<SolInfo> solList, List<ContractInfo> contractInfoList,
        String systemPeers, Integer groupId, String hexPrivateKey, Map<String, String> sdkContentMap)
        throws Exception {
        SrcDir srcDir = new SrcDir(project.toFile());
        NewMainDir mainDir = new NewMainDir(srcDir.toFile(), config, solList, contractInfoList,
            systemPeers, groupId, hexPrivateKey, sdkContentMap);
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
}

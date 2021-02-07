package com.webank.scaffold.core.factory;

import com.webank.scaffold.core.artifact.*;
import com.webank.scaffold.core.config.GeneratorOptions;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.exception.ScaffoldException;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class ProjectFactory {

    /**
     * Build project, which can be ran by users by using gradle test.
     */
    public ProjectArtifact buildProjectDir(String group, String artifact, String filter, String peerStr, String solidityDir, String certDir,
                                           String outputDir){
        /**
         * 1. Create UserConfig object
         */
        UserConfig config = getUserConfig(group, artifact, peerStr);
        /**
         * 2. Create root project directory
         */
        ProjectArtifact project = buildProjectDir(outputDir, config);

        /**
         * 3. Create sub contents in project
         */
        try{
            createSubContents(filter, project, solidityDir, certDir, config);
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

    private UserConfig getUserConfig(String group, String artifact, String peerStr){
        List<String> peers = Arrays.stream(peerStr.split(","))
                .filter(s->!s.isEmpty()).collect(Collectors.toList());

        UserConfig config = new UserConfig();
        config.put(GeneratorOptions.GENERATOR_GROUP, group);
        config.put(GeneratorOptions.GENERATOR_ARTIFACT, artifact);
        config.put(GeneratorOptions.GENERATOR_PEERS, peers);

        return config;
    }

    private void createSubContents(String filter, ProjectArtifact project, String solidityDir, String certDir, UserConfig config)
    throws Exception{
        SrcDir srcDir = new SrcDir(project.toFile());
        MainDir mainDir = new MainDir(filter, srcDir.toFile(), new File(solidityDir), config);
        ConfDir confDir = new ConfDir(project.toFile(), new File(certDir));
        ConfigToml configToml = new ConfigToml(confDir.toFile(), config);
        TestDir testDir = new TestDir(srcDir.toFile());
        TestJavaDir testJavaDir = new TestJavaDir(testDir.toFile(), config);
        BuildGradle buildGradle = new BuildGradle(project.toFile(), config);
        project.generate();
        srcDir.generate();
        mainDir.generate();
        confDir.generate();
        configToml.generate();
        testDir.generate();
        testJavaDir.generate();
        buildGradle.generate();
    }

}

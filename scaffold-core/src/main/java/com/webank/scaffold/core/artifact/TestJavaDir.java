package com.webank.scaffold.core.artifact;

import com.webank.scaffold.core.config.GeneratorOptions;
import com.webank.scaffold.core.config.UserConfig;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class TestJavaDir extends DirectoryArtifact{

    private UserConfig config;

    public TestJavaDir(File parentDir,  UserConfig userConfig) {
        super(parentDir);
        this.config = userConfig;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        /**
         * 1. Build sub directory due to package name
         */
        String groupName = this.config.getProperty(GeneratorOptions.GENERATOR_GROUP);
        String artifactName = this.config.getProperty(GeneratorOptions.GENERATOR_ARTIFACT);
        String packageName = groupName + "." + artifactName;
        File packageDir = new File(this.toFile(), packageName);
        packageDir.mkdirs();

        /**
         * 2. Generate test code
         */
        new TestConnectionJava(packageDir, this.config).generate();
    }

    @Override
    public String getName() {
        return "java";
    }
}

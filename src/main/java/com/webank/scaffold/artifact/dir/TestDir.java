package com.webank.scaffold.artifact.dir;

import com.webank.scaffold.config.UserConfig;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class TestDir extends DirectoryArtifact {

    private UserConfig config;

    public TestDir(File parentDir, UserConfig config) {
        super(parentDir);
        this.config = config;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        TestJavaDir testJavaDir = new TestJavaDir(this.toFile(), this.config);
        testJavaDir.generate();
    }

    @Override
    public String getName() {
        return "test";
    }
}

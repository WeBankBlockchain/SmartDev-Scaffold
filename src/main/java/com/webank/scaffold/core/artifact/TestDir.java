package com.webank.scaffold.core.artifact;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class TestDir extends DirectoryArtifact {

    public TestDir(File parentDir) {
        super(parentDir);
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
    }

    @Override
    public String getName() {
        return "test";
    }
}

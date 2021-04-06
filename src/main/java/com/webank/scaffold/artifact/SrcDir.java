package com.webank.scaffold.artifact;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/19
 */
public class SrcDir extends DirectoryArtifact {
    private static final String SRC_DIR = "src";

    public SrcDir(File basePath) {
        super(basePath);
    }

    @Override
    protected void doGenerateSubContents() throws Exception {

    }

    @Override
    public String getName() {
        return SRC_DIR;
    }
}

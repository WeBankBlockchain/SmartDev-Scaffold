package com.webank.scaffold.core.artifact;

import com.webank.scaffold.core.util.IOUtil;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/04/06
 */
public class GradleDir extends DirectoryArtifact  {

    public GradleDir(File parentDir) {
        super(parentDir);
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        IOUtil.copyFolder(new File("gradle"),this.toFile());
    }

    @Override
    public String getName() {
        return "gradle";
    }
}

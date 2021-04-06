package com.webank.scaffold.core.artifact;

import com.webank.scaffold.core.util.IOUtil;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/04/06
 */
public class Gradle  extends DirectoryArtifact  {

    public Gradle(File parentDir) {
        super(parentDir);
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        IOUtil.copyFile(new File("gradle"),this.toFile());
    }

    @Override
    public String getName() {
        return "gradle";
    }
}

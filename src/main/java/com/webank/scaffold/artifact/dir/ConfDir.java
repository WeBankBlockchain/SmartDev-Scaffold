package com.webank.scaffold.artifact.dir;

import com.webank.scaffold.constants.DirNameConstants;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/19
 */
public class ConfDir extends DirectoryArtifact {

    public ConfDir(File path) {
        super(path);
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
    }

    @Override
    public String getName() {
        return DirNameConstants.CONF_DIR;
    }
}

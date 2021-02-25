package com.webank.scaffold.core.artifact;

import com.webank.scaffold.core.DirectoryCopier;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/19
 */
public class ConfDir extends DirectoryArtifact {

    private static final String CONF_DIR = "conf";

    public ConfDir(File path) {
        super(path);
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
    }

    @Override
    public String getName() {
        return CONF_DIR;
    }
}

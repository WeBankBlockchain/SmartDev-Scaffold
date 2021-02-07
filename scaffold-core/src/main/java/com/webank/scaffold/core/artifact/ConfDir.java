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

    private File srcConfDir;
    public ConfDir(File path, File srcConfDir) {
        super(path);
        this.srcConfDir = srcConfDir;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        new DirectoryCopier().copy(this.srcConfDir.getAbsolutePath(), this.toFile().getAbsolutePath());
    }

    @Override
    public String getName() {
        return CONF_DIR;
    }
}

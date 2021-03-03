package com.webank.scaffold.core.artifact;

import com.webank.scaffold.core.DirectoryCopier;
import com.webank.scaffold.core.util.IOUtil;

import java.io.File;
import java.io.InputStream;

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

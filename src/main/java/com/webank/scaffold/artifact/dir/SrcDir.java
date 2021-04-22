package com.webank.scaffold.artifact.dir;

import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.DirNameConstants;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/19
 */
public class SrcDir extends DirectoryArtifact {

    private UserConfig config;

    public SrcDir(File parentPath, UserConfig config) {
        super(parentPath);
        this.config = config;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {

        MainDir mainDir = new MainDir(this.toFile(), config);
        mainDir.generate();

        TestDir testDir = new TestDir(this.toFile(), config);
        testDir.generate();
    }

    @Override
    public String getName() {
        return DirNameConstants.SRC_DIR;
    }
}

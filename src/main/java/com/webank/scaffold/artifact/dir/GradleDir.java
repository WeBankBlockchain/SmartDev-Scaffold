package com.webank.scaffold.artifact.dir;

import com.webank.scaffold.constants.DirNameConstants;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @data 2021/04/06
 */
public class GradleDir extends DirectoryArtifact {

    public GradleDir(File parentDir) {
        super(parentDir);
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        FileUtils.copyDirectory(new File("gradle"),this.toFile());
    }

    @Override
    public String getName() {
        return DirNameConstants.GRADLE;
    }
}

package com.webank.scaffold.artifact.webase;

import com.webank.scaffold.artifact.DirectoryArtifact;
import com.webank.scaffold.util.IOUtil;

import java.io.File;
import lombok.Getter;

/**
 * get source gradle dir
 * @author marsli
 */
@Getter
public class NewGradleDir extends DirectoryArtifact {

    private String sourceDir;
    public NewGradleDir(File parentDir, String sourceDir) {
        super(parentDir);
        this.sourceDir = sourceDir;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        IOUtil.copyFolder(new File(this.sourceDir), this.toFile());
    }

    @Override
    public String getName() {
        return "gradle";
    }
}

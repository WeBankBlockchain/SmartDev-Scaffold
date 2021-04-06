package com.webank.scaffold.core.artifact;

import java.io.File;

/**
 *
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */
public abstract class DirectoryArtifact implements Artifact {

    protected File parentDir;
    public DirectoryArtifact(File parentDir){
        this.parentDir = parentDir;
    }

    @Override
    public final void generate() throws Exception {
        this.toFile().mkdirs();
        this.doGenerateSubContents();
    }

    protected abstract void doGenerateSubContents() throws Exception;

    @Override
    public final boolean isDirectory() {
        return true;
    }

    @Override
    public final File getParentDir(){
        return this.parentDir;
    }


}

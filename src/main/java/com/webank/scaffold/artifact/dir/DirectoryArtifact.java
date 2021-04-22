package com.webank.scaffold.artifact.dir;

import com.webank.scaffold.artifact.Artifact;
import com.webank.scaffold.exception.ScaffoldException;

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

        if(!this.toFile().mkdirs()){
            throw new ScaffoldException(this.getName() + " directory has exist");
        }
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

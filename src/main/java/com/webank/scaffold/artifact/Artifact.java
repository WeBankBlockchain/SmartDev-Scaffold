package com.webank.scaffold.artifact;

import java.io.File;

public interface Artifact {

    void generate() throws Exception;

    default boolean isDirectory(){
        return false;
    }

    default File toFile(){
        return new File(getParentDir(), getName());
    }

    File getParentDir();

    String getName();
}

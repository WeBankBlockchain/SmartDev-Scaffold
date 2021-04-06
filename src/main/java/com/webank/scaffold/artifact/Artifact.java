package com.webank.scaffold.artifact;

import java.io.File;

/**
 * Marks an item in target project
 * @author aaronchu
 * @Description
 * @data 2021/01/19
 */
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

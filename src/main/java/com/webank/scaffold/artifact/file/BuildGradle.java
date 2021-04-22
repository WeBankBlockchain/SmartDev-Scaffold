package com.webank.scaffold.artifact.file;

import com.webank.scaffold.artifact.Artifact;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.ReplaceConstants;
import com.webank.scaffold.constants.FileNameConstants;
import com.webank.scaffold.util.IOUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Generate build.gradle file
 * @author aaronchu
 * @author grayson
 * @Description
 * @data 2021/01/15
 */
public class BuildGradle implements Artifact {

    private File parent;
    private UserConfig config;

    public BuildGradle(File parent, UserConfig config){
        this.parent = parent;
        this.config = config;
    }

    @Override
    public void generate() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(ReplaceConstants.GENERATOR_GROUP, config.getGroup());
        IOUtil.replaceAllStr(FileNameConstants.TEMPLATE_BUILD_GRADLE, map, this.toFile());
    }

    @Override
    public File getParentDir() {
        return this.parent;
    }

    @Override
    public String getName() {
        return FileNameConstants.BUILD_GRADLE_FILE;
    }
}

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
 * @author aaronchu
 * @author grayson
 * @Description
 * @data 2021/02/24
 */
public class SettingsGradle implements Artifact {

    private File parent;
    private UserConfig config;

    public SettingsGradle(File parent, UserConfig config){
        this.parent = parent;
        this.config = config;
    }

    @Override
    public void generate() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put(ReplaceConstants.GENERATOR_ARTIFACT, config.getArtifact());
        IOUtil.replaceAllStr(FileNameConstants.TEMPLATE_SETTING_GRADLE, map, this.toFile());
    }

    @Override
    public File getParentDir() {
        return this.parent;
    }

    @Override
    public String getName() {
        return FileNameConstants.SETTINGS_GRADLE_FILE;
    }
}

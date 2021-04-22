package com.webank.scaffold.artifact.dir;

import java.io.File;

import com.webank.scaffold.artifact.file.BuildGradle;
import com.webank.scaffold.artifact.file.SettingsGradle;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.util.IOUtil;

/**
 * @author aaronchu
 * @author grayson
 * @Description
 * @data 2021/01/19
 */
public class RootDir extends DirectoryArtifact {

    private UserConfig config;

    public RootDir(File basePath, UserConfig config) {
        super(basePath);
        this.config = config;
    }

    @Override
    public String getName() {
        return this.config.getArtifact();
    }

    @Override
    public void doGenerateSubContents() throws Exception {

        SrcDir srcDir = new SrcDir(this.toFile(), config);
        srcDir.generate();

        BuildGradle buildGradle = new BuildGradle(this.toFile(), config);
        buildGradle.generate();

        SettingsGradle settingsGradle = new SettingsGradle(this.toFile(), config);
        settingsGradle.generate();

        GradleDir gradle = new GradleDir(this.toFile());
        gradle.generate();
    }

    public void clean() {
        IOUtil.removeItem(this.toFile());
    }
}

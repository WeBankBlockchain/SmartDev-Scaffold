package com.webank.scaffold.artifact.dir.root;

import com.webank.scaffold.artifact.dir.GradleDir;
import com.webank.scaffold.artifact.file.BuildGradle;
import com.webank.scaffold.artifact.file.SettingsGradle;
import com.webank.scaffold.config.UserConfig;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @date 2021/08/10
 */
public class GradleRootDir extends RootDir {

    public GradleRootDir(File basePath, UserConfig config) {
        super(basePath, config);
    }

    @Override
    public void doGenerateSubContents() throws Exception {
        super.doGenerateSubContents();

        BuildGradle buildGradle = new BuildGradle(this.toFile(), config);
        buildGradle.generate();

        SettingsGradle settingsGradle = new SettingsGradle(this.toFile(), config);
        settingsGradle.generate();

        GradleDir gradle = new GradleDir(this.toFile(), this.config);
        gradle.generate();
    }
}

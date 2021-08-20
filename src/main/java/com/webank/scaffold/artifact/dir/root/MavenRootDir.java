package com.webank.scaffold.artifact.dir.root;

import com.webank.scaffold.artifact.file.BuildGradle;
import com.webank.scaffold.artifact.file.Mvnw;
import com.webank.scaffold.artifact.file.MvnwCmd;
import com.webank.scaffold.artifact.file.Pom;
import com.webank.scaffold.config.UserConfig;

import java.io.File;

/**
 * @author aaronchu
 * @Description
 * @date 2021/08/10
 */
public class MavenRootDir extends RootDir {

    public MavenRootDir(File basePath, UserConfig config) {
        super(basePath, config);
    }

    @Override
    public void doGenerateSubContents() throws Exception {
        super.doGenerateSubContents();

        Pom pom = new Pom(this.toFile(), config);
        pom.generate();

        Mvnw mvnw = new Mvnw(this.toFile(), config);
        mvnw.generate();

        MvnwCmd mvnwCmd = new MvnwCmd(this.toFile(), config);
        mvnwCmd.generate();
    }
}

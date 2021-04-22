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
 * @Author grayson
 * @Description
 * @data 2021/03/02
 */
public class ApplicationJava implements Artifact {

    private File parentDir;
    private UserConfig config;

    public ApplicationJava(File parent, UserConfig config){
        this.parentDir = parent;
        this.config = config;
    }

    @Override
    public void generate() throws Exception {
        this.parentDir.mkdirs();
        String pkg = config.getGroup() + "." + config.getArtifact();
        Map<String, String> map = new HashMap<>();
        map.put(ReplaceConstants.PACKAGE, pkg);
        IOUtil.replaceAllStr(FileNameConstants.TEMPLATE_APPLICATION, map, this.toFile());
    }

    @Override
    public File getParentDir() {
        return this.parentDir;
    }

    @Override
    public String getName() {
        return FileNameConstants.APPLICATION_JAVA;
    }
}

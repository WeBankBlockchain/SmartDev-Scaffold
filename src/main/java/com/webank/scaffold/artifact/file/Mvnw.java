package com.webank.scaffold.artifact.file;

import com.webank.scaffold.artifact.Artifact;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.FileNameConstants;
import com.webank.scaffold.constants.ReplaceConstants;
import com.webank.scaffold.util.IOUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aaronchu
 * @Description
 * @date 2021/08/10
 */
public class Mvnw implements Artifact {

    private File parent;
    private UserConfig config;

    public Mvnw(File parent, UserConfig config){
        this.parent = parent;
        this.config = config;
    }

    @Override
    public void generate() throws Exception {
        Map<String, String> map = new HashMap<>();
        IOUtil.replaceAllStr(FileNameConstants.TEMPLATE_MVNW, map, this.toFile());
    }

    @Override
    public File getParentDir() {
        return this.parent;
    }

    @Override
    public String getName() {
        return FileNameConstants.MVNW_FILE;
    }
}

package com.webank.scaffold.artifact.dir;

import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.constants.DirNameConstants;
import com.webank.scaffold.constants.FileNameConstants;
import com.webank.scaffold.constants.ReplaceConstants;
import com.webank.scaffold.util.IOUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author aaronchu
 * @Description
 * @data 2021/04/06
 */
public class GradleDir extends DirectoryArtifact {

    private UserConfig config;

    public GradleDir(File parentDir, UserConfig config) {
        super(parentDir);
        this.config = config;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        /**
         * 1. Create gradle wrapper directory
         */
        File wrapperDir = new File(this.toFile(), "wrapper");
        wrapperDir.mkdirs();

        /**
         * 2. Copy gradle wrapper jar
         */
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream jarIn = classLoader.getResourceAsStream(FileNameConstants.TEMPLATE_GRADLE_WRAPPER_JAR);
            OutputStream jarOut = new FileOutputStream(new File(wrapperDir, FileNameConstants.GRADLE_WRAPPER_JAR))
        ){
            IOUtils.copy(jarIn, jarOut);
        }

        /**
         * 3. Generate gradle properties file
         */
        Map<String, String> map = new HashMap<>();
        map.put(ReplaceConstants.GRADLE, config.getGradle());
        IOUtil.replaceAllStr(FileNameConstants.TEMPLATE_GRADLE_WRAPPER_PROPERTIES, map, new File(wrapperDir, FileNameConstants.GRADLE_WRAPPER_PROPERTIES));
    }

    @Override
    public String getName() {
        return DirNameConstants.GRADLE;
    }
}

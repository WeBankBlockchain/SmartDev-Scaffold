package com.webank.scaffold.artifact;

import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.util.IOUtil;
import com.webank.scaffold.util.PackageNameUtil;

import java.io.File;
import java.io.InputStream;

/**
 * @author aaronchu
 * @Description
 * @data 2021/03/03
 */
public class SdkBeanConfigJava implements Artifact {
    private static final String TEMPLATE_RESOURCE
            = "templates/SdkBeanConfig.java";
    public static final String JAVA = "SdkBeanConfig.java";

    private File parentDir;
    private UserConfig config;

    public SdkBeanConfigJava(File parentDir, UserConfig config){
        this.parentDir = parentDir;
        this.config = config;
    }

    @Override
    public void generate() throws Exception {
        this.parentDir.mkdirs();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream is = classLoader.getResourceAsStream(TEMPLATE_RESOURCE)){
            this.generate(is, this.toFile());
        }
    }

    /**
     * Build a build.gradle. This file is simple, only have java-sdk dependencies. It is built from
     * a template.
     */
    public void generate(InputStream templateInput,File outputPath) throws Exception {
        /**
         * 1. Read template
         */
        String template = IOUtil.readAsString(templateInput);
        /**
         * 2. Replace vars in template with users configuration info
         */
        template = replaceAllVars(template);
        /**
         * 3. Output
         */
        IOUtil.writeString(outputPath, template);
    }

    @Override
    public File getParentDir() {
        return parentDir;
    }

    @Override
    public String getName() {
        return JAVA;
    }

    private String replaceAllVars(String template){
        String pkg = PackageNameUtil.getConfigPackageName(config);
        template = template.replace("${package}", pkg);
        return template;
    }
}

package com.webank.scaffold.core.artifact;

import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.util.IOUtil;
import com.webank.scaffold.core.util.PackageNameUtil;

import java.io.File;
import java.io.InputStream;

public class CommonResponseJava implements Artifact{
    private static final String TEMPLATE_COMMONRESPONSE
            = "templates/CommonResponse.java";
    public static final String COMMONRESPONSE_JAVA = "CommonResponse.java";

    private File parentDir;
    private UserConfig config;

    public CommonResponseJava(File parentDir, UserConfig config){
        this.parentDir = parentDir;
        this.config = config;
    }

    @Override
    public void generate() throws Exception {
        this.parentDir.mkdirs();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream is = classLoader.getResourceAsStream(TEMPLATE_COMMONRESPONSE)){
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
         * 3. Outpu
         */
        IOUtil.writeString(outputPath, template);
    }

    @Override
    public File getParentDir() {
        return parentDir;
    }

    @Override
    public String getName() {
        return COMMONRESPONSE_JAVA;
    }

    private String replaceAllVars(String template){
        String pkg = PackageNameUtil.getModelPackage(config);
        template = template.replace("${package}", pkg);
        return template;
    }
}

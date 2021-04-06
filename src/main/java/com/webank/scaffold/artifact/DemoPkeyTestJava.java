package com.webank.scaffold.artifact;

import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.util.IOUtil;
import com.webank.scaffold.util.PackageNameUtil;

import java.io.File;
import java.io.InputStream;

public class DemoPkeyTestJava implements Artifact {
    private static final String TEMPLATE_IOUTIL
            = "templates/DemoPkey.java";
    public static final String DEMO_PKEY_JAVA = "DemoPkey.java";

    private File parentDir;
    private UserConfig config;

    public DemoPkeyTestJava(File parentDir, UserConfig config){
        this.parentDir = parentDir;
        this.config = config;
    }

    @Override
    public void generate() throws Exception {
        this.parentDir.mkdirs();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream is = classLoader.getResourceAsStream(TEMPLATE_IOUTIL)){
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
        return DEMO_PKEY_JAVA;
    }

    private String replaceAllVars(String template){
        String pkg = PackageNameUtil.getRootPackageName(config);
        template = template.replace("${package}", pkg);
        return template;
    }
}

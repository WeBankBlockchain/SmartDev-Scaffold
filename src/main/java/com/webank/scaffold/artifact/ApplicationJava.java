package com.webank.scaffold.artifact;

import com.webank.scaffold.config.GeneratorOptions;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.util.IOUtil;
import com.webank.scaffold.util.PackageNameUtil;

import java.io.File;
import java.io.InputStream;

/**
 * @author aaronchu
 * @Description
 * @data 2021/03/02
 */
public class ApplicationJava implements Artifact {
    private static final String TEMPLATE_APPLICATION
            = "templates/Application.java";
    public static final String APPLICATION_JAVA = "Application.java";


    private File parentDir;
    private UserConfig userConfig;

    public ApplicationJava(File parent, UserConfig userConfig){
        this.parentDir = parent;
        this.userConfig = userConfig;
    }


    @Override
    public void generate() throws Exception {
        this.parentDir.mkdirs();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream is = classLoader.getResourceAsStream(TEMPLATE_APPLICATION)){
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

    private String replaceAllVars(String template){
        String group = this.userConfig.getProperty(GeneratorOptions.GENERATOR_GROUP);
        String artifact =  this.userConfig.getProperty(GeneratorOptions.GENERATOR_ARTIFACT);
        template = template.replace("${"+ GeneratorOptions.GENERATOR_GROUP+"}", group);
        String pkg = PackageNameUtil.getRootPackageName(group, artifact);
        template = template.replace("${package}", pkg);
        return template;
    }

    @Override
    public File getParentDir() {
        return this.parentDir;
    }

    @Override
    public String getName() {
        return "Application.java";
    }
}

package com.webank.scaffold.core.artifact;

import com.webank.scaffold.core.config.GeneratorOptions;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.util.IOUtil;
import com.webank.scaffold.core.util.PackageNameUtil;

import java.io.File;
import java.io.InputStream;

/**
 * @author aaronchu
 * @Description
 * @data 2021/02/24
 */
public class SettingsGradle implements Artifact {

    private static final String TEMPLATE_RESOURCE
            = "templates/settings.gradle";
    private static final String SETTINGS_GRADLE_FILE = "settings.gradle";

    private File parent;
    private UserConfig userConfig;

    public SettingsGradle(File parent, UserConfig userConfig){
        this.parent = parent;
        this.userConfig = userConfig;
    }


    @Override
    public void generate() throws Exception {
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
    public File toFile() {
        return new File(parent, SETTINGS_GRADLE_FILE);
    }

    @Override
    public File getParentDir() {
        return this.parent;
    }

    @Override
    public String getName() {
        return SETTINGS_GRADLE_FILE;
    }

    private String replaceAllVars(String template){
        String artifact =  this.userConfig.getProperty(GeneratorOptions.GENERATOR_ARTIFACT);
        template = template.replace("${"+ GeneratorOptions.GENERATOR_ARTIFACT+"}", artifact);
        return template;
    }
}

package com.webank.scaffold.core.artifact;

import com.webank.scaffold.core.config.GeneratorOptions;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.util.PackageNameUtil;
import com.webank.scaffold.core.util.IOUtil;

import java.io.File;
import java.io.InputStream;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class TestConnectionJava implements Artifact {

    private static final String TEST_FILE = "TestConnection.java";
    private static final String TEMPLATE_RESOURCE = "templates/TestConnection.java";

    private File parent;
    private UserConfig userConfig;

    public TestConnectionJava(File parent, UserConfig config ){
        this.parent = parent;
        this.userConfig = config;
    }

    @Override
    public void generate() throws Exception {
        /**
         * 1. Read template
         */
        String template = readTemplate();

        /**
         * 2. Replace vars
         */
        String fixedTemplate = replaceAllVars(template);

        /**
         * 3. Write to project
         */
        IOUtil.writeString(this.toFile(), fixedTemplate);
    }

    private String readTemplate() throws Exception{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream is = classLoader.getResourceAsStream(TEMPLATE_RESOURCE)){
            String template = IOUtil.readAsString(is);
            return template;
        }
    }

    private String replaceAllVars(String template){
        String group = this.userConfig.getProperty(GeneratorOptions.GENERATOR_GROUP);
        String artifact =  this.userConfig.getProperty(GeneratorOptions.GENERATOR_ARTIFACT);
        String packageName = PackageNameUtil.getRootPackageName(group, artifact);
        template = template.replace("${"+ GeneratorOptions.GENERATOR_GROUP+"}", group);
        template = template.replace("${"+ GeneratorOptions.GENERATOR_ARTIFACT+"}",artifact);
        template = template.replace("${package}",packageName);
        return template;
    }

    @Override
    public File getParentDir() {
        return this.parent;
    }

    @Override
    public String getName() {
        return TEST_FILE;
    }
}

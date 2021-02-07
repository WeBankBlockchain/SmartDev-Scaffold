package com.webank.scaffold.core.util;

import com.webank.scaffold.core.config.GeneratorOptions;
import com.webank.scaffold.core.config.UserConfig;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class PackageNameUtil {


    public static String getRootPackageName(String group, String artifact){
        return group + "." +artifact;
    }

    public static String getRootPackageName(UserConfig config){
        String group = config.getProperty(GeneratorOptions.GENERATOR_GROUP);
        String artifact = config.getProperty(GeneratorOptions.GENERATOR_ARTIFACT);
        return getRootPackageName(group, artifact);
    }

    public static String getBOPackageName(UserConfig config){
        return getRootPackageName(config) + ".model.bo";
    }

    public static String getServicePackageName(UserConfig config){
        return getRootPackageName(config) + ".service";
    }
}

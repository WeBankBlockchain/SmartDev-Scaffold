/*
 * Copyright 2014-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.webank.scaffold.constants;

/**
 * FileNameConstants
 *
 * @Description:
 * @Author: grayson
 * @Version 1.0
 * @Date: 2021-04-07 15:59
 **/
public class FileNameConstants {
    
    public static final String TEMPLATE_POSTFIX = ".tpl";

    //template file name
    public static final String TEMPLATE_BUILD_GRADLE = "templates/build.gradle" + TEMPLATE_POSTFIX;
    public static final String TEMPLATE_POM = "templates/pom.xml" + TEMPLATE_POSTFIX;
    public static final String TEMPLATE_SETTING_GRADLE = "templates/settings.gradle" + TEMPLATE_POSTFIX;
    public static final String TEMPLATE_MVNW = "templates/mvnw" + TEMPLATE_POSTFIX;
    public static final String TEMPLATE_MVNW_CMD = "templates/mvnw.cmd" + TEMPLATE_POSTFIX;
    public static final String TEMPLATE_APPLICATION = "templates/Application.java" + TEMPLATE_POSTFIX;
    public static final String TEMPLATE_COMMONRESPONSE = "templates/CommonResponse.java" + TEMPLATE_POSTFIX;
    public static final String TEMPLATE_DEMOS = "templates/Demos.java" + TEMPLATE_POSTFIX;
    public static final String TEMPLATE_SDK_CONFIG = "templates/SdkBeanConfig.java" + TEMPLATE_POSTFIX;
    public static final String TEMPLATE_GRADLE_WRAPPER_JAR = "templates/gradle/wrapper/gradle-wrapper.jar" + TEMPLATE_POSTFIX;
    public static final String TEMPLATE_GRADLE_WRAPPER_PROPERTIES = "templates/gradle/wrapper/gradle-wrapper.properties"+TEMPLATE_POSTFIX;
    public static final String TEMPLATE_BCOS_CONFIG = "templates/BcosConfig.java"+TEMPLATE_POSTFIX;

    //generated file name
    public static final String BUILD_GRADLE_FILE = "build.gradle";
    public static final String POM_FILE = "pom.xml";
    public static final String APP_PRO = "application.properties";
    public static final String SETTINGS_GRADLE_FILE = "settings.gradle";
    public static final String MVNW_FILE = "mvnw";
    public static final String MVNW_CMD_FILE = "mvnw.cmd";
    public static final String APPLICATION_JAVA = "Application.java";
    public static final String COMMON_RESPONSE_JAVA = "CommonResponse.java";
    public static final String DEMOS_JAVA = "Demos.java";
    public static final String SDK_CONFIG = "SdkBeanConfig.java";
    public static final String GRADLE_WRAPPER_JAR = "gradle-wrapper.jar";
    public static final String GRADLE_WRAPPER_PROPERTIES = "gradle-wrapper.properties";
    public static final String BCOS_CONFIG = "BcosConfig.java";
    public static final String SYSTEM_CONFIG = "SystemConfig";

    public static final String CONTRACT_CONFIG = "ContractConfig";
    public static final String CONTRACT_CONSTANTS = "ContractConstants";

    //package or file postfix
    public static final String CONFIG_PKG_POSTFIX = ".config";
    public static final String UTILS_PKG_POSTFIX = ".utils";
    public static final String ABI_FILE_POSTFIX = ".abi";
    public static final String MODEL_FILE_POSTFIX = ".model";
    public static final String BO_PKG_POSTFIX = ".model.bo";
    public static final String SERVICE_PKG_POSTFIX = ".service";
    public static final String CONSTANT_PKG_POSTFIX = ".constants";

    public final static String CTOR = "CtorBO";
    public final static String INPUT = "InputBO";
    public final static String ABI_POSTFIX = "Abi";
    public final static String BINARY_POSTFIX = "Binary";
    public final static String BINARY_GM_POSTFIX = "GmBinary";


}

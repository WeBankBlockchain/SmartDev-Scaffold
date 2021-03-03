package com.webank.scaffold.core.clhandler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.util.PackageNameUtil;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/03/03
 */
public class ServiceBeanConfigExporter {

    private List<String> contracts;
    private UserConfig config;

    public void export(){
        //
        String pkg = PackageNameUtil.getConfigPackageName(config);
        ClassName className = ClassName.get(pkg, "ServiceBeanConfig");
        TypeSpec configClass
                = TypeSpec.classBuilder(className)
                .addAnnotation(Configuration.class)


        for(String contract: contracts)
    }

    @Bean
    public HelloWorldService helloWorldService(Client client) throws Exception {
        String address = properties.getContract().getHelloWorld();
        if (address == null) {
            return new HelloWorldService(client);
        }
        return new HelloWorldService(address, client);
    }
}

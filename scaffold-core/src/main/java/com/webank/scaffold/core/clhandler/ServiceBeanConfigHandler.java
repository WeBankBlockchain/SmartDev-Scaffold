package com.webank.scaffold.core.clhandler;

import com.squareup.javapoet.*;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.util.CommonUtil;
import com.webank.scaffold.core.util.PackageNameUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.lang.model.element.Modifier;
import java.io.File;
import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/03/03
 */
public class ServiceBeanConfigHandler {
    private File javaRoot;
    private List<String> contracts;
    private UserConfig config;

    public ServiceBeanConfigHandler(File javaRoot, List<String> contracts, UserConfig config){
        this.javaRoot = javaRoot;
        this.contracts = contracts;
        this.config = config;
    }

    public void export() throws Exception{
        //1. Initializing
        String configPkg = PackageNameUtil.getConfigPackageName(config);
        TypeSpec.Builder configClassBuilder
                = initBuilder(configPkg);

        //2. Contract services beans
        String servicePkg = PackageNameUtil.getServicePackageName(config);
        for(String contract: contracts){
            addBean(configClassBuilder, servicePkg, contract);
        }

        //3. Output
        JavaFile javaFile = JavaFile.builder(configPkg, configClassBuilder.build()).build();
        javaFile.writeTo(javaRoot);
    }

    private TypeSpec.Builder initBuilder(String configPkg){

        ClassName className = ClassName.get(configPkg, "ServiceBeanConfig");
        TypeSpec.Builder configClassBuilder
                = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Configuration.class)
                .addAnnotation(Data.class)
                .addAnnotation(Slf4j.class)
                .addField(FieldSpec.builder(
                        ClassName.get(configPkg, SystemConfigHandler.SYSTEN_CONFIG), "config", Modifier.PRIVATE)
                        .addAnnotation(Autowired.class)
                        .build());
        return configClassBuilder;
    }

    private void addBean(TypeSpec.Builder builder, String servicePackage, String contract){
        String serviceName = contract+"Service";
        ClassName serviceClass = ClassName.get(servicePackage, serviceName);
        builder.addMethod(
                MethodSpec.methodBuilder(CommonUtil.makeFirstCharLowerCase(contract)+"Service")
                .addModifiers(Modifier.PUBLIC)
                        .returns(serviceClass)
                        .addParameter(ParameterSpec.builder(Client.class, "client").build())
                        .addException(Exception.class)
                        .addAnnotation(Bean.class)
                .addStatement("String address = config.getContract().get$LAddress()", contract)
                .addStatement("if (address == null) {log.warn(\"$L address not configured, so $L will not inject into spring\");}", contract, serviceName)
                .addStatement("return new $L(address, client)",serviceName)
                .build()
        );
    }

    /*
    @Bean
    public HelloWorldService helloWorldService(Client client) throws Exception {
        String address = config.getContract().getHelloWorld();
        if (address == null) {
            return new HelloWorldService(client);
        }
        return new HelloWorldService(address, client);
    }

     */
}

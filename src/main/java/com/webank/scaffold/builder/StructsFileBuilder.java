package com.webank.scaffold.builder;

import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.handler.ContractWrapper;
import org.fisco.bcos.sdk.v3.codec.wrapper.ABIDefinition;
import org.fisco.bcos.codegen.v3.utils.CodeGenUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Convert struct into bos
 * @author yuzhichu
 */
public class StructsFileBuilder implements JavaFileBuilder{

    private UserConfig config;
    private ContractWrapper handler;
    private List<ABIDefinition> abis;

    public StructsFileBuilder(String abiStr, UserConfig config) throws Exception{
        this.config = config;
        this.handler = new ContractWrapper(false);
        this.abis =  CodeGenUtils.loadContractAbiDefinition(abiStr);
    }

    //private List<TypeSpec> buildStructTypes(List<ABIDefinition> functionDefinitions)
    @Override
    public String getJavaFilePackage(String relativePackage) {
        return config.getGroup() + "." + config.getArtifact() + relativePackage;
    }

    @Override
    public List<TypeSpec> buildTypeSpec(String fullPkg) {
        try{
            return handler.buildStructTypes(abis);
        }
        catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}

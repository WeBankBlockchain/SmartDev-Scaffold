package com.webank.scaffold.builder;

import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.handler.ContractHandler;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;
import org.fisco.bcos.sdk.codegen.CodeGenUtils;


import java.util.List;

/**
 * Convert struct into bos
 * @author yuzhichu
 */
public class StructsFileBuilder implements JavaFileBuilder{

    private UserConfig config;
    private ContractHandler handler;
    private List<ABIDefinition> abis;

    public StructsFileBuilder(String abiStr, UserConfig config) throws Exception{
        this.config = config;
        this.handler = new ContractHandler();
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

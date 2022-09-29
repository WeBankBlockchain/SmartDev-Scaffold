package com.webank.scaffold;

import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.cmd.ScaffoldRunner;
import com.webank.scaffold.constants.FileNameConstants;
import com.webank.scaffold.handler.ContractWrapper;
import com.webank.scaffold.handler.SolidityTypeHandler;
import org.fisco.bcos.codegen.v3.utils.CodeGenUtils;
import org.fisco.bcos.sdk.v3.codec.wrapper.ABIDefinition;
import picocli.CommandLine;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
public class Main {
    public static void main(String[] args) throws Exception{
//        CommandLine cmd = new CommandLine(new ScaffoldRunner());
//        cmd.execute(args);
        //3.0.0
        List<ABIDefinition> abis = CodeGenUtils.loadContractAbiDefinition("[{\"inputs\":[{\"internalType\":\"string\",\"name\":\"tableName\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"valueFields\",\"type\":\"string\"}],\"name\":\"createTable\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"tableName\",\"type\":\"string\"}],\"name\":\"desc\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"tableName\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"}],\"name\":\"get\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"},{\"components\":[{\"components\":[{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"value\",\"type\":\"string\"}],\"internalType\":\"struct KVField[]\",\"name\":\"fields\",\"type\":\"tuple[]\"}],\"internalType\":\"struct Entry\",\"name\":\"entry\",\"type\":\"tuple\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"tableName\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"components\":[{\"components\":[{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"value\",\"type\":\"string\"}],\"internalType\":\"struct KVField[]\",\"name\":\"fields\",\"type\":\"tuple[]\"}],\"internalType\":\"struct Entry\",\"name\":\"entry\",\"type\":\"tuple\"}],\"name\":\"set\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]");
        //rc3
//        List<ABIDefinition> abis = CodeGenUtils.loadContractAbiDefinition("[{\"inputs\":[{\"internalType\":\"string\",\"name\":\"tableName\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"valueFields\",\"type\":\"string\"}],\"name\":\"createTable\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"tableName\",\"type\":\"string\"}],\"name\":\"desc\",\"outputs\":[{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"\",\"type\":\"string\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"tableName\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"}],\"name\":\"get\",\"outputs\":[{\"internalType\":\"bool\",\"name\":\"\",\"type\":\"bool\"},{\"components\":[{\"components\":[{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"value\",\"type\":\"string\"}],\"internalType\":\"struct KVField[]\",\"name\":\"fields\",\"type\":\"tuple[]\"}],\"internalType\":\"struct Entry\",\"name\":\"entry\",\"type\":\"tuple\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"string\",\"name\":\"tableName\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"components\":[{\"components\":[{\"internalType\":\"string\",\"name\":\"key\",\"type\":\"string\"},{\"internalType\":\"string\",\"name\":\"value\",\"type\":\"string\"}],\"internalType\":\"struct KVField[]\",\"name\":\"fields\",\"type\":\"tuple[]\"}],\"internalType\":\"struct Entry\",\"name\":\"entry\",\"type\":\"tuple\"}],\"name\":\"set\",\"outputs\":[{\"internalType\":\"int256\",\"name\":\"\",\"type\":\"int256\"}],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]");

        ContractWrapper c = new ContractWrapper(false);
        List<TypeSpec>  structs = c.buildStructTypes(abis);
//        System.out.println(structs);
    }
}


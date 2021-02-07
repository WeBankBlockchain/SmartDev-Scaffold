package com.webank.scaffold.core.artifact;

import com.squareup.javapoet.TypeSpec;
import com.webank.scaffold.core.ContractCompiler;
import com.webank.scaffold.core.DirectoryCopier;
import com.webank.scaffold.core.abi.BOBuilder;
import com.webank.scaffold.core.abi.ServiceBuilder;
import com.webank.scaffold.core.config.UserConfig;
import com.webank.scaffold.core.exception.ScaffoldException;
import com.webank.scaffold.core.util.IOUtil;
import org.apache.commons.io.FilenameUtils;
import org.fisco.bcos.sdk.abi.wrapper.ABIDefinition;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/19
 */
public class MainDir extends DirectoryArtifact {
    private static final String MAIN_DIR = "main";
    private BOBuilder bOBuilder;
    private ServiceBuilder srvBuilder;

    private static final String SOL_Dir = "contracts";
    private static final String JAVA_DIR = "java";
    private static final String ABI_DIR = "abi";

    private File srcSolDir;
    private UserConfig config;
    private String filter;

    public MainDir(String filter,File basePath, File srcContractDir, UserConfig config) {
        super(basePath);
        this.srcSolDir = srcContractDir;
        this.filter = filter;
        this.config = config;
        this.bOBuilder = new BOBuilder(config);
        this.srvBuilder = new ServiceBuilder(config);
    }

    /**
     * Generate sub directories: contracts，abi，bin，smbin，java
     * @throws Exception
     */
    @Override
    protected void doGenerateSubContents() throws Exception {
        /**
         * 1. Copy raw solidity contracts to "contracts" directory
         */
        File contractsDir = generateContractsDir();

        /**
         * 2. Compile contracts then generate "abi","bin","smbin","java" directory
         */
        compileContract(contractsDir);

        /**
         * 3. generates: BO | Service
         */
        File abiDir = new File(this.toFile(), ABI_DIR);
        File javaDir = new File(this.toFile(), JAVA_DIR);
        for(File abiFile :abiDir.listFiles()){
            String contractName = abiFile.getName().split("\\.")[0];
            if(!FilenameUtils.wildcardMatch(contractName, filter)){ continue;}
            String abiStr = IOUtil.readAsString(abiFile);
            //Build BO
            TypeSpec ctorBO = this.bOBuilder.buildCtorBO(contractName, abiStr);
            Map<ABIDefinition, TypeSpec> functionBos = this.bOBuilder.buildFunctionBO(contractName, abiStr);
            this.bOBuilder.exportBOs(javaDir, Arrays.asList(ctorBO));
            this.bOBuilder.exportBOs(javaDir, functionBos.values().stream().collect(Collectors.toList()));
            //Build service
            TypeSpec serviceType = srvBuilder.build(contractName, ctorBO, functionBos);
            this.srvBuilder.exportBO(serviceType, javaDir);
        }
    }

    @Override
    public String getName() {
        return MAIN_DIR;
    }


    private File generateContractsDir() throws Exception {
        File contractsDir = new File(this.toFile(), SOL_Dir);
        DirectoryCopier copier = new DirectoryCopier();
        copier.copy(this.srcSolDir.getAbsolutePath(),contractsDir.getAbsolutePath());
        return contractsDir;
    }

    /**
     * 编译contracts目录下的合约，编译并得到abi，smbin，bin，java代码
     */
    private void compileContract(File contractDir) throws Exception{
        ContractCompiler compiler = new ContractCompiler(contractDir, this.toFile(), this.config);
        try{
            compiler.compile();
        }
        catch (Exception ex){
            throw new ScaffoldException("Compilation failed");
        }
    }
}

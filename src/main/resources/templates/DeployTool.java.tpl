package ${package};

import lombok.extern.slf4j.Slf4j;
import org.example.demo.constants.CryptoConstants;
import org.fisco.bcos.sdk.abi.ABICodec;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author aaronchu
 * @Description
 * @data 2021/05/12
 */
@Component("deploy")
@Slf4j
public class DeployTool implements Tool {

    @Autowired
    private Client client;

    private AssembleTransactionProcessor txProcessor;

    @PostConstruct
    public void init() throws Exception {
        this.txProcessor = TransactionProcessorFactory.createAssembleTransactionProcessor(this.client, this.client.getCryptoSuite().getCryptoKeyPair());
    }

    @Override
    public void execute(String[] srcArgs) throws Exception{
        /**
         * 1. Load abi from contract name
         */
        if(srcArgs.length < 1){
            log.error("Invalid formats. Please refer to :deploy [ContractName] [argList]");
            return;
        }
        String contractName = srcArgs[1];
        String abi =org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("abi/"+contractName+".abi"));
        String eccBin =org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("bin/ecc/"+contractName+".bin"));
        String gmBin =org.apache.commons.io.IOUtils.toString(Thread.currentThread().getContextClassLoader().getResource("bin/sm/"+contractName+".bin"));
        String bin = client.getCryptoSuite().getCryptoTypeConfig() == CryptoConstants.ECC?eccBin:gmBin;
        /**
         * 2. Encode deploy args
         */
        ABICodec codec = new ABICodec(client.getCryptoSuite());
        List<String> deployArgs= new ArrayList<>();
        for(int i=2;i<srcArgs.length;i++){
            deployArgs.add(srcArgs[i]);
        }
        /**
         * 3. Deploy
         */
        String data = codec.encodeConstructorFromString(abi, bin, deployArgs);
        TransactionReceipt receipt = this.txProcessor.deployAndGetReceipt(data);
        if(receipt.isStatusOK()){
            log.info("deploy address:{}", receipt.getContractAddress());
        }
        else{
            log.info("deploy failed:{},{}", receipt.getStatus(), receipt.getStatusMsg());
        }
    }
}

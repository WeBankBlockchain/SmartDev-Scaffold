package ${package};

import lombok.extern.slf4j.Slf4j;
import org.example.demo.constants.CryptoConstants;
import org.fisco.bcos.sdk.abi.ABICodec;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.manager.AssembleTransactionProcessor;
import org.fisco.bcos.sdk.transaction.manager.TransactionProcessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author aaronchu
 * @Description
 * @data 2021/05/12
 */
@Component
@Slf4j
public class CommandHandler implements ApplicationRunner {

    @Autowired
    private Map<String, Tool> toolMap;

    //deploy HelloWorld 0x.. 0x..
    @Override
    public void run(ApplicationArguments args) throws Exception {
        String[] srcArgs = args.getSourceArgs();
        if(srcArgs.length == 0){
            return;
        }
        String toolName = srcArgs[0];
        Tool tool = toolMap.get(toolName);
        if(tool == null) {
            log.info("Unknown tool {}", toolName);
            System.exit(0);
            return;
        }
        tool.execute(srcArgs);
        System.exit(0);
    }
}

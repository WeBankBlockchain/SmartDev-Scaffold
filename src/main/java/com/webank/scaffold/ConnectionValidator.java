package com.webank.scaffold;

import org.fisco.bcos.sdk.config.model.ConfigProperty;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */
public class ConnectionValidator {

    /**
     * 确保连接能够正常进行
     */
    public void ensureOK(ConfigProperty configProperty) throws Exception{
        /**
         *         BcosSDK bcosSDK = new BcosSDK(new ConfigOption(configProperty));
         *         Client client = bcosSDK.getClient(1);
         *         BlockNumber blockNumber = client.getBlockNumber();
         *         System.out.println("Connection OK, current block number "+blockNumber.getBlockNumber());
         */

    }

}

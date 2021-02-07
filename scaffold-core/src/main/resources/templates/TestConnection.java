package ${package};

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.junit.Test;

public class TestConnection {

    /**
     * 确保连接能够正常进行
     */
    @Test
    public void connectionTest() throws Exception {

        BcosSDK bcosSDK = BcosSDK.build("conf/config.toml");
        Client client = bcosSDK.getClient(1);
        BlockNumber blockNumber = client.getBlockNumber();
        System.out.println("Connection OK, current block number " + blockNumber.getBlockNumber());
    }
}

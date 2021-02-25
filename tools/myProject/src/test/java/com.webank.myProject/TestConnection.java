package com.webank.myProject;

import com.webank.myProject.model.bo.HelloWorldSetInputBO;
import com.webank.myProject.service.HelloWorldService;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.transaction.model.dto.CallResponse;
import org.fisco.bcos.sdk.transaction.model.dto.TransactionResponse;
import org.junit.Test;

public class TestConnection {

    /**
     * 确保连接能够正常进行
     */
    @Test
    public void test() throws Exception {

        BcosSDK bcosSDK = BcosSDK.build("conf/config.toml");
        Client client = bcosSDK.getClient(1);

        //方式1：自动部署一个合约
        HelloWorldService service
                = new HelloWorldService(client);//替换成实际生成项目中的对应合约
        //方式2：从指定地址加载合约
        HelloWorldService service2
                = new HelloWorldService(service.getAddress(), client);//替换成实际生成项目中的对应合约
        TransactionResponse setResponse = service.set(new HelloWorldSetInputBO("hello"));
        CallResponse callResponse = service.get();
    }
}

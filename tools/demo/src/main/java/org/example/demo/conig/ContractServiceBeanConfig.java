package org.example.demo.conig;

import org.example.demo.service.HelloWorldService;
import org.fisco.bcos.sdk.client.Client;
import org.springframework.context.annotation.Bean;

/**
 * @author aaronchu
 * @Description
 * @data 2021/03/03
 */
public class ContractServiceBeanConfig {

    @Bean
    public HelloWorldService helloWorldService(Client client) throws Exception {
        String address = properties.getContract().getHelloWorldAddress();
        if (address == null || address.isEmpty()) {
            log.warn("HelloWorldService does not config addresss so not inject into spring context");
            return null;
        }
        return new HelloWorldService(address, client);
    }
}

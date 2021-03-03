package org.example.demo.conig;

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.config.ConfigOption;
import org.fisco.bcos.sdk.config.model.ConfigProperty;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.ECDSAKeyPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class SdkBeanConfig {

    @Autowired
    private SystemConfig config;

    @Bean
    public Client client() throws Exception {
        //从目录加载config.toml
        //从目录加载
        ConfigProperty property = new ConfigProperty();
        configNetwork(property);
        configCryptoMaterial(property);

        ConfigOption configOption = new ConfigOption(property);
        Client client = new BcosSDK(configOption).getClient(config.getGroupId());

        BigInteger blockNumber = client.getBlockNumber().getBlockNumber();
        log.info("Chain connect successful. Current block number {}", blockNumber);

        configCryptoKeyPair(client);
        log.info("is Gm:{}, address:{}", client.getCryptoSuite().cryptoTypeConfig == 1, client.getCryptoSuite().getCryptoKeyPair().getAddress());
        return client;
    }

    public void configNetwork(ConfigProperty configProperty){
        String peerStr = config.getPeers();
        List<String> peers = Arrays.stream(peerStr.split(",")).collect(Collectors.toList());
        Map<String, Object> networkConfig = new HashMap<>();
        networkConfig.put("peers", peers);

        configProperty.setNetwork(networkConfig);
    }

    public void configCryptoMaterial(ConfigProperty configProperty){
        Map<String, Object> cryptoMaterials = new HashMap<>();
        cryptoMaterials.put("certPath", config.getCertPath());
        configProperty.setCryptoMaterial(cryptoMaterials);
    }

    public void configCryptoKeyPair(Client client){
        if(config.getHexPrivateKey() == null){
            return;
        }
        client.getCryptoSuite().setCryptoKeyPair(client.getCryptoSuite().createKeyPair(config.getHexPrivateKey()));
    }
}

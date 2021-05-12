package ${package};

import lombok.extern.slf4j.Slf4j;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.ECDSAKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.SM2KeyPair;
import org.springframework.stereotype.Component;

/**
 * @author aaronchu
 * @Description
 * @data 2021/05/12
 */
@Component("key")
@Slf4j
public class KeyTool implements Tool {
    @Override
    public void execute(String[] args) throws Exception {
        //ECDSA key generation
        CryptoKeyPair ecdsaKeyPair = new ECDSAKeyPair().generateKeyPair();
        log.info("ecdsa private key :"+ecdsaKeyPair.getHexPrivateKey());
        log.info("ecdsa public key :"+ecdsaKeyPair.getHexPublicKey());
        log.info("ecdsa address :"+ecdsaKeyPair.getAddress());
        //SM2 key generation
        CryptoKeyPair sm2KeyPair = new SM2KeyPair().generateKeyPair();
        log.info("sm2 private key :"+sm2KeyPair.getHexPrivateKey());
        log.info("sm2 public key :"+sm2KeyPair.getHexPublicKey());
        log.info("sm2 address :"+sm2KeyPair.getAddress());
    }
}

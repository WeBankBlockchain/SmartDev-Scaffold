package com.webank.scaffold.core.artifact;

import com.moandjiezana.toml.TomlWriter;
import com.webank.scaffold.core.config.GeneratorOptions;
import com.webank.scaffold.core.config.UserConfig;
import org.fisco.bcos.sdk.config.model.ConfigProperty;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author aaronchu
 * @Description
 * @data 2021/01/15
 */
public class ConfigToml implements Artifact {
    private static final String CONFIG_FILE = "config.toml";

    private File parent;
    private UserConfig userConfig;

    public ConfigToml(File parent, UserConfig userConfig){
        this.parent = parent;
        this.userConfig = userConfig;
    }

    /**
     * Create config.toml, which contains network, cert info.
     */

    @Override
    public void generate() throws Exception {
        /**
         * 1. Create config property
         */
        ConfigProperty configProperty = new ConfigProperty();
        //network config
        setNetwork(configProperty);
        //crypto material config(cert)
        setCryptoMaterial(configProperty);

        /**
         * 2. Serialize this config property to config.toml
         */
        File target = this.toFile();
        new TomlWriter().write(configProperty, target);
    }

    private void setNetwork(ConfigProperty property){
        Map<String, Object> network = new HashMap<>();
        network.put("peers", this.userConfig.get(GeneratorOptions.GENERATOR_PEERS));
        property.setNetwork(network);
    }

    private void setCryptoMaterial(ConfigProperty property){
        Map<String, Object> cert = new HashMap<>();
        cert.put("certPath", "conf");
        property.setCryptoMaterial(cert);
    }

    @Override
    public File getParentDir() {
        return this.parent;
    }

    @Override
    public String getName() {
        return CONFIG_FILE;
    }
}

package com.webank.scaffold.core.artifact;

import com.webank.scaffold.core.DirectoryCopier;
import com.webank.scaffold.core.util.IOUtil;

import java.io.File;
import java.io.InputStream;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/19
 */
public class ConfDir extends DirectoryArtifact {

    private static final String CONF_DIR = "conf";

    private static final String TEMPLATE_RESOURCE
            = "templates/config.toml";
    private static final String CONFIG_TOML = "config.toml";

    public ConfDir(File path) {
        super(path);
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try(InputStream in = classLoader.getResourceAsStream(TEMPLATE_RESOURCE)){
            String configContent = IOUtil.readAsString( in );
            IOUtil.writeString(new File(this.toFile(), CONFIG_TOML),configContent);
        }
    }

    @Override
    public String getName() {
        return CONF_DIR;
    }
}

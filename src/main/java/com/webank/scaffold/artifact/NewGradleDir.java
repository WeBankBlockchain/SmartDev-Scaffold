package com.webank.scaffold.artifact;

import com.webank.scaffold.util.IOUtil;
import java.io.File;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * get source gradle dir
 * @author marsli
 */
@Getter
public class NewGradleDir extends GradleDir {
    private static final Logger logger = LoggerFactory.getLogger(NewGradleDir.class);

    private String sourceDir;
    public NewGradleDir(File parentDir, String sourceDir) {
        super(parentDir);
        this.sourceDir = sourceDir;
    }

    @Override
    protected void doGenerateSubContents() throws Exception {
        logger.info("doGenerateSubContents parentDir:{} sourceDir:{}", parentDir.getAbsolutePath(), sourceDir);
        IOUtil.copyFolder(new File(this.sourceDir), this.toFile());
    }

    @Override
    public String getName() {
        return "gradle";
    }
}

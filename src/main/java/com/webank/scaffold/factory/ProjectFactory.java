package com.webank.scaffold.factory;

import com.webank.scaffold.artifact.dir.RootDir;
import com.webank.scaffold.config.UserConfig;
import com.webank.scaffold.exception.ScaffoldException;
import java.io.File;

/**
 * @author aaronchu
 * @author grayson
 * @Description
 * @data 2021/01/20
 */
public class ProjectFactory {


    private UserConfig config;

    public ProjectFactory(String group, String artifact, String solidityDir,  String output, String need){
        this.config = UserConfig.getInstance(group, artifact, solidityDir, output, need);
    }

    public boolean createProject(){

        //1. build project root object
        RootDir project = new RootDir(new File(config.getOutputDir()), config);

        //2. create sub content in project
        try{
            project.generate();
            System.out.println("Project created:" + project.toFile());
        }
        catch (Exception ex){
            ex.printStackTrace();

            // 3.Delete all project data in case of exception
            try{
                project.clean();
            }
            catch (Exception e){}
        }
        return true;
    }
}

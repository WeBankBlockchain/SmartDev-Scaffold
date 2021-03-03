package com.webank.scaffold.cmd;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import picocli.CommandLine;

/**
 * @author aaronchu
 * @Description
 * @data 2021/01/20
 */
@SpringBootApplication
public class Main implements ApplicationRunner {

    public static void main(String[] args) throws Exception{
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        CommandLine cmd = new CommandLine(new ScaffoldRunner());
        cmd.execute(args.getSourceArgs());

        System.exit(0);
    }
}

package com.oop.project;

import com.oop.project.ioc.ContainerContext;
import com.oop.project.ioc.processors.LoggingBeanPostProcessor;
import com.oop.project.threads.PokemonThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(PokemonThread.class);

    public static void main(String[] args) {

        try {
            ContainerContext ctx = ContainerContext.getInstance()
                    .withComponentsToScan("com.oop.project")
                    .withBeanPostProcessors(new LoggingBeanPostProcessor())
                    .initContainer();

            ApplicationRunner applicationRunner = ContainerContext.getBean(ApplicationRunner.class);

            applicationRunner.run();
        } catch (Exception e) {
            LOGGER.error("Unexpected exception: ", e);
        }
    }

}

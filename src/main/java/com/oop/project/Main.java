package com.oop.project;

import com.oop.project.ioc.ApplicationContext;
import com.oop.project.ioc.processors.LoggingBeanPostProcessor;
import com.oop.project.services.ItemService;
import com.oop.project.threads.PokemonThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(PokemonThread.class);

    public static void main(String[] args) {
        try {
            ApplicationContext ctx = new ApplicationContext.ApplicationContextBuilder()
                    .withComponentsToScan("com.oop.project")
                    .build();

            ApplicationRunner applicationRunner = ctx.getBean(ApplicationRunner.class);

            // TODO to remove this code after interceptors are finished
            ctx.getBean(ItemService.class).throwException();

            applicationRunner.run();

        } catch (Exception e) {
            LOGGER.error("Unexpected exception: ", e);
            throw new RuntimeException(e);
        }
    }

}

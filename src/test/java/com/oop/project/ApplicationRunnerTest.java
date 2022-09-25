package com.oop.project;

import com.oop.project.ioc.ContainerContext;
import com.oop.project.services.TrainerService;
import org.junit.BeforeClass;
import org.junit.Test;


public class ApplicationRunnerTest {

    private static ApplicationRunner runner;

    private static TrainerService trainerService;

    @BeforeClass
    public static void prepareTest() {
        ContainerContext.getInstance().initContainer();

        trainerService = ContainerContext.getBean(TrainerService.class);
        runner = ContainerContext.getBean(ApplicationRunner.class);
    }

    @Test
    public void runTests() throws Exception {
        prepareTest();

        {
            trainerService.updateItems("trainers.json");
            runner.run();
        }
        {
            trainerService.updateItems("trainers1.json");
            runner.run();
        }
        {
            trainerService.updateItems("trainers2.json");
            runner.run();
        }
        {
            trainerService.updateItems("trainers3.json");
            runner.run();
        }
        {
            trainerService.updateItems("trainers4.json");
            runner.run();
        }
        {
            trainerService.updateItems("trainers5.json");
            runner.run();
        }
        {
            trainerService.updateItems("trainers6.json");
            runner.run();
        }
        {
            trainerService.updateItems("trainers7.json");
            runner.run();
        }
        {
            trainerService.updateItems("trainers8.json");
            runner.run();
        }
        {
            trainerService.updateItems("trainers9.json");
            runner.run();
        }
    }

}
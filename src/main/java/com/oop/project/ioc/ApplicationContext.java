package com.oop.project.ioc;

import com.oop.project.ioc.initialization.*;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ApplicationContext {

    private volatile static ApplicationContext instance;

    private final Logger LOGGER = LogManager.getLogger(ApplicationContext.class);

    private final Set<String> componentsToScan = new HashSet<>();

    private final Set<BeanPostProcessor> beanPostProcessors = new HashSet<>();


    private final ComponentsScanner componentsScanner = new ConfigurationFirstComponentScanner(new DependentComponentScanner());

    private final Set<InitializationRule> initializationRules;

    private final BeanInitializer beanInitializer = new DefaultBeanInitializer();


    private ApplicationContext() {
        initializationRules = new InitializationRulesBuilder().withInitRule(new SkipLazyBeanInitializationRule()).withInitRule(new SkipPrototypeBeanInitializationRule()).withInitRule(new SkipAnnotationBeanInitializationRule()).build();
    }

    public static ApplicationContext getContext() {
        if (instance == null) {
            throw new RuntimeException("Context wasn't yet created!");
        }

        return instance;
    }


    public <T> T getBean(Class<T> clazz, Object... args) {
        return Objects.requireNonNull(beanInitializer.getBean(clazz, args).orElse(null));
    }

    public static <T> T getBeanStatic(Class<T> clazz, Object... args) {
        return Objects.requireNonNull(getContext().beanInitializer.getBean(clazz, args).orElse(null));
    }


    public static class ApplicationContextBuilder {
        private final ApplicationContext ctx;


        public ApplicationContextBuilder() {
            ctx = new ApplicationContext();
        }


        public ApplicationContextBuilder withComponentsToScan(String... components) {
            ctx.componentsToScan.addAll(Arrays.asList(components));
            return this;
        }

        public ApplicationContextBuilder withBeanPostProcessors(BeanPostProcessor... beanPostProcessors) {
            ctx.beanPostProcessors.addAll(Arrays.asList(beanPostProcessors));
            return this;
        }


        public ApplicationContext build() {
            Set<Class<?>> classes = scanAllPrefixes(ctx.componentsToScan, ctx.initializationRules);

            Set<Object> objects = initClasses(classes);
            ctx.beanInitializer.applyBeanPostProcessors(objects, ctx.beanPostProcessors);

            ApplicationContext.instance = ctx;
            return ctx;
        }


        private Set<Class<?>> scanAllPrefixes(Set<String> componentsToScan, Set<InitializationRule> initializationRules) {
            Set<Class<?>> classes = new HashSet<>();

            for (String prefix : ctx.componentsToScan) {
                classes.addAll(ctx.componentsScanner.scanPrefixes(prefix));
            }

            classes = applyInitializationRules(classes, initializationRules);

            return classes;
        }


        private Set<Object> initClasses(Set<Class<?>> classes) {
            Set<Object> newBeans = new HashSet<>();

            for (Class<?> clazz : classes) {
                try {
                    Optional<?> bean = ctx.beanInitializer.getBean(clazz, null);
                    bean.ifPresent(newBeans::add);
                } catch (Exception e) {
                    ctx.LOGGER.info("Failed to instantiate bean: {}", clazz.getName(), e);
                }
            }

            return newBeans;
        }


        private Set<Class<?>> applyInitializationRules(Set<Class<?>> classes, Set<InitializationRule> initializationRules) {
            for (InitializationRule initializationRule : initializationRules) {
                classes = initializationRule.applyRule(classes);
            }

            return classes;
        }

    }


}

package com.oop.project.ioc;

import com.oop.project.ioc.initialization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

@SuppressWarnings("unchecked")
public class ApplicationContext {
    private static final Object mutex = new Object();
    private volatile static ApplicationContext instance;
    private final Logger LOGGER = LogManager.getLogger(ApplicationContext.class);
    private final Map<Class<?>, Object> dependencies = new HashMap<>();

    private final Set<String> componentsToScan = new HashSet<>();

    private final Set<BeanPostProcessor> beanPostProcessors = new HashSet<>();

    private final BeanInitializer beanInitializer = new DefaultBeanInitializer();

    private final ComponentsScanner componentsScanner = new ConfigurationFirstComponentScanner(new DependentComponentScanner());

    private final Set<InitializationRule> initializationRules;

    private ApplicationContext() {
        initializationRules = new InitializationRulesBuilder()
                .withInitRule(new SkipLazyBeanInitializationRule())
                .withInitRule(new SkipPrototypeBeanInitializationRule())
                .withInitRule(new SkipAnnotationBeanInitializationRule())
                .build();
    }

    public static ApplicationContext getContext() {
        ApplicationContext result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    result = instance = new ApplicationContext();
                }
            }
        }

        return result;
    }

    public ApplicationContext initContext() {
        Set<Class<?>> classes = scanAllPrefixes(componentsToScan, initializationRules);

        Set<Object> objects = initClasses(classes);

        applyBeanPostProcessors(objects);

        return instance;
    }

    private Set<Class<?>> scanAllPrefixes(Set<String> componentsToScan, Set<InitializationRule> initializationRules) {
        Set<Class<?>> classes = new HashSet<>();

        for (String prefix : this.componentsToScan) {
            classes.addAll(componentsScanner.scanPrefixes(prefix));
        }

        classes = applyInitializationRules(classes, initializationRules);

        return classes;
    }

    private Set<Class<?>> applyInitializationRules(Set<Class<?>> classes, Set<InitializationRule> initializationRules) {
        for (InitializationRule initializationRule : initializationRules) {
            classes = initializationRule.applyRule(classes);
        }

        return classes;
    }

    private void applyBeanPostProcessors(Set<Object> beans) {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            for (Object bean : beans) {
                Object processedBean = beanPostProcessor.processBeanAfterInitialization(bean, instance);
                dependencies.put(bean.getClass(), processedBean);
            }
        }
    }

    private Set<Object> initClasses(Set<Class<?>> classes) {
        Set<Object> newBeans = new HashSet<>();

        for (Class<?> clazz : classes) {
            try {
                beanInitializer.initBean(clazz, null).ifPresent(newBeans::add);
            } catch (Exception e) {
                LOGGER.info("Failed to instantiate bean: {}", clazz.getName(), e);
            }
        }

        return newBeans;
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz, Object... args) {
        if (!dependencies.containsKey(clazz)) { // don't use compute if absent because initBean already put new bean if necessary
            return Objects.requireNonNull(beanInitializer.initBean(clazz, args)).orElse(null);
        }
        return (T) dependencies.get(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBeanStatic(Class<T> clazz, Object... args) {
        if (!instance.dependencies.containsKey(clazz)) { // don't use compute if absent because initBean already put new bean if necessary
            return Objects.requireNonNull(instance.beanInitializer.initBean(clazz, args)).orElse(null);
        }
        return (T) instance.dependencies.get(clazz);
    }


    public ApplicationContext withComponentsToScan(String... components) {
        getContext().componentsToScan.addAll(Arrays.asList(components));
        return getContext();
    }

    public ApplicationContext withBeanPostProcessors(BeanPostProcessor... beanPostProcessors) {
        this.beanPostProcessors.addAll(Arrays.asList(beanPostProcessors));
        return instance;
    }

}

package com.oop.project.ioc;

import com.oop.project.ioc.annotations.Bean;
import com.oop.project.ioc.initialization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ContainerContext {
    private final Logger LOGGER = LogManager.getLogger(ContainerContext.class);

    private volatile static ContainerContext instance;
    private static final Object mutex = new Object();

    private final Map<Class<?>, Object> dependencies = new HashMap<>();

    private final Set<String> componentsToScan = new HashSet<>();

    private final Set<BeanPostProcessor> beanPostProcessors = new HashSet<>();

    private final BeanInitializer beanInitializer = new DefaultBeanInitializer();

    private final ComponentsScanner componentsScanner = new ConfigurationFirstComponentScanner(new DependentComponentScanner());

    private ContainerContext() {
    }

    public static ContainerContext getInstance() {
        ContainerContext result = instance;
        if (result == null) {
            synchronized (mutex) {
                result = instance;
                if (result == null) {
                    result = instance = new ContainerContext();
                }
            }
        }

        return result;
    }


    public ContainerContext initContainer() {
        Set<Class<?>> classes = scanAllPrefixes(componentsToScan);

        Set<Object> objects = getInstance().initClasses(classes);
        applyBeanPostProcessors(objects);

        return instance;
    }

    private Set<Class<?>> scanAllPrefixes(Set<String> componentsToScan) {
        Set<Class<?>> classes = new HashSet<>();

        for (String prefix : this.componentsToScan) {
            classes.addAll(componentsScanner.scanPrefixes(prefix));
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

        for (Class<?> aClass : classes) {
            try {
                beanInitializer.initBean(aClass, null).ifPresent(newBeans::add);
            } catch (Exception e) {
                LOGGER.info("Failed to instantiate bean: {}", aClass.getName(), e);
            }
        }

        return newBeans;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz, Object... args) {
        if (!getInstance().dependencies.containsKey(clazz)) { // don't use compute if absent because initBean already put new bean if necessary
            return Objects.requireNonNull(getInstance().beanInitializer.initBean(clazz, args)).orElse(null);
        }
        return (T) getInstance().dependencies.get(clazz);
    }


    public ContainerContext withComponentsToScan(String... components) {
        getInstance().componentsToScan.addAll(Arrays.asList(components));
        return getInstance();
    }

    public ContainerContext withBeanPostProcessors(BeanPostProcessor... beanPostProcessors) {
        this.beanPostProcessors.addAll(Arrays.asList(beanPostProcessors));
        return instance;
    }

}

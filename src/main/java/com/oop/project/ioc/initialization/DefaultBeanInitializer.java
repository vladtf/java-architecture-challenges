package com.oop.project.ioc.initialization;

import com.oop.project.ioc.ContainerContext;
import com.oop.project.ioc.annotations.Bean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultBeanInitializer implements BeanInitializer {
    private final Logger LOGGER = LogManager.getLogger(DefaultBeanInitializer.class);

    private final Set<Class<?>> instantiationStack = new HashSet<>();

    @Override
    public <T> Optional<T> initBean(Class<T> clazz, Object... args) {
        if (instantiationStack.contains(clazz)) {
            throw new IllegalStateException("Bean is already instantiating. This means that there might be a circular dependency, instantiation stack: " + instantiationStack);
        }

        if (clazz.isAnnotation()) {
            return Optional.empty();
        }

        instantiationStack.add(clazz);

        if (!clazz.isAnnotationPresent(Bean.class)) {
            throw new IllegalArgumentException(clazz.getName() + " is not a Bean annotated class and cannot be instantiated.");
        }

        try {

            T instance = getConstructor(clazz, args).newInstance(args);

            Bean annotation = clazz.getAnnotation(Bean.class);
            if (!annotation.prototype()) { // don't store prototype beans
                instance = putDependency(clazz, instance).orElse(null);
            }

            LOGGER.info("Registered new bean: {}", clazz.getName());

            instantiationStack.remove(clazz);
            return Optional.ofNullable(instance);
        } catch (Exception e) {
            LOGGER.info("Failed to instantiate bean: {}", clazz.getName(), e);
        }

        return Optional.empty();
    }

    private <T> Constructor<T> getConstructor(Class<T> clazz, Object... args) throws NoSuchMethodException {
        if (args == null || args.length == 0) {
            return clazz.getConstructor();
        }

        List<? extends Class<?>> argsType = Arrays.stream(args).map(Object::getClass)
                .collect(Collectors.toList());

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            if (parameterTypes.length != args.length) {
                continue;
            }

            boolean isRightConstructor = false;
            for (Class<?> parameterType : parameterTypes) {
                if (!argsType.contains(parameterType)) {
                    isRightConstructor = true;
                    break;
                }
            }
            if (isRightConstructor) {
                return (Constructor<T>) constructor;
            }
        }

        throw new NoSuchMethodException(clazz.getName() + ".<init>" + argsType);
    }

    private <T> Optional<T> putDependency(Class<T> clazz, Object obj) {
        if (obj == null) {
            return Optional.empty();
        }

        return Optional.of((T) obj);
    }

}

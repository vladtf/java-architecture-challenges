package com.oop.project.ioc.initialization;

import com.oop.project.ioc.annotations.Autowired;
import com.oop.project.ioc.annotations.Bean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.*;

public class DefaultBeanInitializer implements BeanInitializer {
    private final Logger LOGGER = LogManager.getLogger(DefaultBeanInitializer.class);

    private final Map<Class<?>, Object> dependencies = new HashMap<>();

    private final Set<Class<?>> instantiationStack = new HashSet<>();

    public <T> Optional<T> initBean(Class<T> clazz, Object... args) {
        if (instantiationStack.contains(clazz)) {
            throw new IllegalStateException("Bean is already instantiating. This means that there might be a circular dependency, instantiation stack: " + instantiationStack);
        }

        instantiationStack.add(clazz);

        try {

            T instance;

            if (isPrototype(clazz)) {
                instance = initPrototype(clazz, args);
            } else {
                instance = initSingleton(clazz);
            }

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

    private <T> T initSingleton(Class<T> clazz) {
        return null;
    }

    private <T> T initPrototype(Class<T> clazz, Object[] args) {
        return null;
    }

    private <T> Constructor<T> getConstructor(Class<T> clazz, Object... args) throws NoSuchMethodException {
        if (args == null || args.length == 0 || !isPrototype(clazz)) {
            return getConstructorWithNoRequiredArgs(clazz);
        }

        List<? extends Class<?>> argsType = Arrays.stream(args).map(Object::getClass).toList();

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

    private static <T> boolean isPrototype(Class<T> clazz) {
        return clazz.getAnnotation(Bean.class).prototype();
    }

    @SuppressWarnings("unchecked")
    private <T> Constructor<T> getConstructorWithNoRequiredArgs(Class<T> clazz) {
        Constructor<T>[] declaredConstructors = (Constructor<T>[]) clazz.getDeclaredConstructors();

        if (declaredConstructors.length == 1) {
            return declaredConstructors[0];
        }

        List<Constructor<T>> autowiredConstructors = Arrays.stream(declaredConstructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toList();

        if (autowiredConstructors.size() == 0) {
            throw new RuntimeException("Multiple constructors, but no @Autowired constructor for class [" + clazz + "]");
        }

        if (autowiredConstructors.size() > 1) {
            throw new RuntimeException("There are multiple constructors annotated with @Autowired for class [" + clazz + "]");
        }

        return autowiredConstructors.get(0);
    }

    private <T> Optional<T> putDependency(Class<T> clazz, Object obj) {
        if (obj == null) {
            return Optional.empty();
        }

        return Optional.of((T) obj);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getBean(Class<T> clazz, Object... args) {
        if (!dependencies.containsKey(clazz)) { // don't use compute if absent because initBean already put new bean if necessary
            return initBean(clazz, args);
        }
        return Optional.ofNullable((T) dependencies.get(clazz));
    }

    @Override
    public void applyBeanPostProcessors(Set<Object> beans, Set<BeanPostProcessor> beanPostProcessors) {
        for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
            for (Object bean : beans) {
                Object processedBean = beanPostProcessor.processBeanAfterInitialization(bean);
                dependencies.put(bean.getClass(), processedBean);
            }
        }
    }
}

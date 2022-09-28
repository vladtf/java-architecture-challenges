package com.oop.project.ioc.initialization;

import com.oop.project.ioc.annotations.Autowired;
import com.oop.project.ioc.utils.ReflectionUtils;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

public class DefaultBeanInitializer implements BeanInitializer {
    private final Logger LOGGER = LogManager.getLogger(DefaultBeanInitializer.class);

    private final Map<Class<?>, Object> registeredBeans = new HashMap<>();

    private final Set<Class<?>> instantiationStack = new HashSet<>();

    public <T> Optional<T> initBean(Class<T> clazz, Object... args) {
        if (instantiationStack.contains(clazz)) {
            throw new IllegalStateException("Bean " + clazz.getName() + " is already being instantiating. This means that there might be a circular dependency, instantiation stack: " + instantiationStack);
        }

        instantiationStack.add(clazz);

        try {

            T instance;

            // TODO this must be replaced with a factory because there be more categories of beans that has different types of instantiation
            //  and you mustn't include in init bean logic that will figure out what kind of installation is required
            if (isPrototype(clazz)) {
                instance = initPrototype(clazz, args);
            } else {
                instance = initSingleton(clazz);
                registeredBeans.put(clazz, instance); // don't store prototype beans
            }

            LOGGER.info("Registered new bean: {}", clazz.getName());

            instantiationStack.remove(clazz);
            return Optional.of(instance);
        } catch (Exception e) {
            LOGGER.info("Failed to instantiate bean: {}", clazz.getName(), e);
        }

        return Optional.empty();
    }

    @SneakyThrows
    private <T> T initSingleton(Class<T> clazz) {
        Constructor<T> constructor = getSingletonConstructor(clazz);
        checkConstructorIsFullyAutowired(constructor);

        List<Object> args = new ArrayList<>();
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            Optional<?> param = getBean(parameterType);
            args.add(Objects.requireNonNull(param.orElse(null)));
        }

        return constructor.newInstance(args.toArray());
    }

    private <T> void checkConstructorIsFullyAutowired(Constructor<T> constructor) {
        for (Class<?> parameterType : constructor.getParameterTypes()) {
            if (!ReflectionUtils.isBeanAnnotationPresent(parameterType)) {
                throw new RuntimeException("Constructor for clazz " + constructor.getName() + " have parameters that can't be injected because are not beans.");
            }
        }
    }

    @SneakyThrows
    private <T> T initPrototype(Class<T> clazz, Object[] args) {
        return getPrototypeConstructor(clazz, args).newInstance(args);
    }

    @SneakyThrows
    private <T> Constructor<T> getPrototypeConstructor(Class<T> clazz, Object... args) {
        if (args == null || args.length == 0 || !isPrototype(clazz)) {
            return getSingletonConstructor(clazz);
        }

        List<? extends Class<?>> argsType = Arrays.stream(args).map(Object::getClass).toList();

        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            Class<?>[] parameterTypes = constructor.getParameterTypes();

            if (parameterTypes.length != args.length) {
                continue;
            }

            // TODO must be sorted checking to avoid bug when there are 2 parameters of same type but it was provided only once
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
        return ReflectionUtils.getBeanAnnotation(clazz).prototype();
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private <T> Constructor<T> getSingletonConstructor(Class<T> clazz) {
        Constructor<T>[] declaredConstructors = (Constructor<T>[]) clazz.getDeclaredConstructors();

        if (declaredConstructors.length == 1) {
            return declaredConstructors[0];
        }

        List<Constructor<T>> autowiredConstructors = Arrays.stream(declaredConstructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .toList();

        if (autowiredConstructors.size() == 0) {
            throw new NoSuchMethodException("Multiple constructors, but no @Autowired constructor for class [" + clazz + "]");
        }

        if (autowiredConstructors.size() > 1) {
            throw new NoSuchMethodException("There are multiple constructors annotated with @Autowired for class [" + clazz + "]");
        }


        return autowiredConstructors.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> getBean(Class<T> clazz, Object... args) {
        if (!registeredBeans.containsKey(clazz)) { // don't use compute if absent because initBean already put new bean if necessary
            return initBean(clazz, args);
        }
        return Optional.ofNullable((T) registeredBeans.get(clazz));
    }

    @Override
    public void applyBeanPostProcessors(Set<Object> beans, Set<BeanPostProcessor> beanPostProcessors) {
        // TODO need to fix this implementation
        applyBeanPostProcessors(beans);
    }

    @Override
    public void applyBeanPostProcessors(Set<Object> beans) {
        for (BeanPostProcessor beanPostProcessor : findAllBeanPostProcessors()) {
            for (Object bean : beans) {
                Object processedBean = beanPostProcessor.processBeanAfterInitialization(bean);
                registeredBeans.put(bean.getClass(), processedBean);
            }
        }
    }

    // TODO don't think this method must be here
    private Set<BeanPostProcessor> findAllBeanPostProcessors() {
        return registeredBeans.values().stream().filter(o -> o instanceof BeanPostProcessor)
                .map(o -> (BeanPostProcessor) o)
                .collect(Collectors.toSet());
    }
}

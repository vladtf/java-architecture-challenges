package com.oop.project.ioc.initialization;

import com.oop.project.ioc.annotations.Autowired;
import com.oop.project.ioc.annotations.Bean;
import lombok.extern.log4j.Log4j2;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
public class DependentComponentScanner implements ComponentsScanner {

    private final Set<Class<?>> instantiationStack = new HashSet<>();


    @Override
    public <T> T nextElement() {
        return null;
    }

    @Override
    public Set<Class<?>> scanPrefixes(String... prefixes) {
        Set<Class<?>> foundClasses = new HashSet<>();

        for (String prefix : prefixes) {
            foundClasses.addAll(scanPrefix(prefix));
        }

        return foundClasses;
    }

    private Set<Class<?>> scanPrefix(String prefix) {
        Reflections reflections = new Reflections(prefix, new SubTypesScanner(false));

        return new HashSet<>(reflections.getSubTypesOf(Object.class)).stream().filter(aClass -> {
            Bean annotation = aClass.getAnnotation(Bean.class);

            if (annotation != null) { // filter not beans
                return !annotation.lazy() && !annotation.prototype(); // filter lazy beans and prototype
            }
            return false;
        }).collect(Collectors.toSet());

    }

    public Optional<Class<?>> scanClass(Class<?> clazz) {
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

            Constructor<?> instance = getConstructor(clazz, args);

            Bean annotation = clazz.getAnnotation(Bean.class);
            if (!annotation.prototype()) { // don't store prototype beans
                instance = putDependency(clazz, instance).orElse(null);
            }

            log.info("Registered new bean: {}", clazz.getName());

            instantiationStack.remove(clazz);
            return Optional.ofNullable(instance);
        } catch (Exception e) {
            log.info("Failed to instantiate bean: {}", clazz.getName(), e);
        }

        return Optional.empty();
    }

    private Constructor<?> getConstructor(Class<?> clazz) throws NoSuchMethodException {

        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();

        if (declaredConstructors.length == 1) {
            return declaredConstructors[0];
        }

        List<Constructor<?>> autowiredConstructors = Arrays.stream(declaredConstructors)
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

    private <T> Optional<T> putDependency(Class<T> clazz, Object obj) {
        // TODO what is this for
        if (obj == null) {
            return Optional.empty();
        }

        return Optional.of((T) obj);
    }

}

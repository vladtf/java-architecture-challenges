package com.oop.project.ioc.initialization;

import com.oop.project.ioc.annotations.Autowired;
import com.oop.project.ioc.annotations.Bean;
import com.oop.project.ioc.utils.ReflectionUtils;
import lombok.extern.log4j.Log4j2;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

// TODO this class may have some issues with prototype beans
@Log4j2
public class DependentComponentScanner implements ComponentsScanner {

    private final Set<Class<?>> instantiationStack = new HashSet<>();

    private static boolean isPrototypeOrLazyBean(Class<?> clazz) {
        Bean beanAnnotation = ReflectionUtils.getBeanAnnotation(clazz);
        return beanAnnotation.lazy() || beanAnnotation.prototype();
    }

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
        HashSet<Class<?>> classesInPrefix = new HashSet<>(reflections.getSubTypesOf(Object.class));

        Set<Class<?>> classesList = classesInPrefix.stream()
                .filter(this::isNotAnnotation)
                .filter(this::isBeanAnnotated).collect(Collectors.toSet());


        Set<Class<?>> classes = classesList.stream()
                .map(this::scanClass)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());

        return classes;
    }

    private boolean isBeanAnnotated(Class<?> aClass) {
        return ReflectionUtils.isBeanAnnotationPresent(aClass);
    }

    private boolean isNotAnnotation(Class<?> aClass) {
        return !aClass.isAnnotation();
    }

    public Set<Class<?>> scanClass(Class<?> clazz) {
        // TODO checking if is prototype/lazy bean don't seems to be ok (violation of SRP)
        if (instantiationStack.contains(clazz) || !isBeanAnnotated(clazz) || isPrototypeOrLazyBean(clazz)) {
            return Collections.emptySet();
        }

        HashSet<Class<?>> result = new HashSet<>();

        result.add(clazz);
        instantiationStack.add(clazz);

        Constructor<?> constructor = getConstructor(clazz);

        Class<?>[] constructorParameterTypes = constructor.getParameterTypes();

        for (Class<?> constructorParameterType : constructorParameterTypes) {
            result.addAll(scanClass(constructorParameterType));
        }

        log.debug("Finished scanning class: {}", clazz.getName());

        return result;
    }

    private Constructor<?> getConstructor(Class<?> clazz) throws RuntimeException {

        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();

        if (declaredConstructors.length == 1) {
            return declaredConstructors[0];
        }

        List<Constructor<?>> autowiredConstructors = Arrays.stream(declaredConstructors)
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
}

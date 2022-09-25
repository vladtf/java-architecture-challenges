package com.oop.project.ioc.initialization;

import java.util.Optional;

public interface BeanInitializer {

    <T> Optional<T> initBean(Class<T> clazz, Object... args);

}

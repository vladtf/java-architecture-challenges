package com.oop.project.ioc.initialization;


// todo this may become an alternative for builder pattern by chaining decorators instead of just passing previous implementation
//  to decorator class as an constructor argument
public interface Chained {
    <T> T nextElement();
}

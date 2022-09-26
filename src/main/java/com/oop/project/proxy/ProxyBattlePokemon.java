package com.oop.project.proxy;

import com.oop.project.ioc.ContainerContext;
import com.oop.project.models.BattlePokemon;
import com.oop.project.models.Item;
import com.oop.project.models.Pokemon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.InvocationHandler;

import java.util.Set;

// Proxy
public class ProxyBattlePokemon {
    private static final Logger LOGGER = LogManager.getLogger(ProxyBattlePokemon.class);

    private ProxyBattlePokemon() {
        throw new UnsupportedOperationException("Constructor must not be called in an util class.");
    }

    public static BattlePokemon preparePokemonWithLog(Pokemon pokemon, Set<Item> items) {
        BattlePokemon battlePokemon = ContainerContext.getInstance().getBeanNonStatic(BattlePokemon.class, pokemon, items);

        Callback handler = (InvocationHandler) (o, method, args) -> {
            if ("attackMe".equals(method.getName())) {
                LOGGER.debug("{} was attacked.", battlePokemon.getName());
            }
            if ("updateWin".equals(method.getName())) {
                LOGGER.debug("{} won a match.", battlePokemon.getName());
            }
            if ("updateLoose".equals(method.getName())) {
                LOGGER.debug("{} lost a match.", battlePokemon.getName());
            }
            return method.invoke(battlePokemon, args);
        };

        return (BattlePokemon) Enhancer.create(BattlePokemon.class, handler);
    }
}

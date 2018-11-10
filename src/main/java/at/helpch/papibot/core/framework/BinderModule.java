package at.helpch.papibot.core.framework;

import at.helpch.papibot.core.objects.tasks.Task;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

// ------------------------------
// Copyright (c) PiggyPiglet 2018
// https://www.piggypiglet.me
// ------------------------------
public final class BinderModule extends AbstractModule {
    private final Class clazz;

    public BinderModule(Class clazz) {
        this.clazz = clazz;
    }

    public Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void configure() {
        bind(clazz).toInstance(clazz);
        requestStaticInjection(Task.class);
    }
}

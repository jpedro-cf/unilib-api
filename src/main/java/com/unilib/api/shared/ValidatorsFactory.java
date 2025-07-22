package com.unilib.api.shared;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ValidatorsFactory {
    private final ApplicationContext context;
    public ValidatorsFactory(ApplicationContext context){
        this.context = context;
    }

    public <T extends Validator<?, ?>> T getValidator(Class<T> clazz) {
        return context.getBean(clazz);
    }
}

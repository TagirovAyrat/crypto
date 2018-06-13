package ru.airiva.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface  BotContextMethod {
    String step() default "initial";

    String [] ifInputTextEquals() default {};
}

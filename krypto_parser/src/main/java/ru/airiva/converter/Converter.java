package ru.airiva.converter;

public interface Converter<S,T> {

         void convert(T source, S dest);

}
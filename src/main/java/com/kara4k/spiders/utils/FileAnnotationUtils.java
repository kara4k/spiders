package com.kara4k.spiders.utils;

import lombok.extern.java.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Log
public class FileAnnotationUtils {

    public static String getFieldStringValue(
            final Object object,
            final Class<? extends Annotation> annotationClass,
            final String defaultValue) {
        return getFieldStringValue(object, annotationClass, false, defaultValue);
    }

    public static String getFieldStringValue(
            final Object object,
            final Class<? extends Annotation> annotationClass) {

        return getFieldStringValue(object, annotationClass, true, "");
    }

    private static String getFieldStringValue(
            final Object object,
            final Class<? extends Annotation> annotationClass,
            final boolean isMandatory,
            final String defaultValue) {

        final Class<?> aClass = object.getClass();
        final Field[] declaredFields = aClass.getDeclaredFields();

        for (final Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            final boolean isPresent = declaredField.isAnnotationPresent(annotationClass);

            if (isPresent) {
                try {
                    final Object o = declaredField.get(object);

                    if (o == null && isMandatory) {
                        throw new RuntimeException("Annotated field is null");
                    }

                    return o == null ? defaultValue : o.toString();
                } catch (final IllegalAccessException e) {
                    log.severe(e.getMessage());
                }
            }
        }

        if (isMandatory) {
            throw new RuntimeException("Mandatory field annotation not found: " + annotationClass.toString());
        }

        return defaultValue;
    }

}

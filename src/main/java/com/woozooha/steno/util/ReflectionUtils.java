package com.woozooha.steno.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class ReflectionUtils {

    public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != Object.class) {
            for (final Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            // Exit if candidates exist.
            if (!methods.isEmpty()) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        return methods;
    }

    public static List<Field> getFieldsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
        final List<Field> fields = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != Object.class) {
            for (final Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotation)) {
                    fields.add(field);
                }
            }
            // Exit if candidates exist.
            if (!fields.isEmpty()) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

    public static List<Method> getMethodsReturnWith(final Class<?> type, final Class<?> t) {
        final List<Method> methods = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != Object.class) {
            for (final Method method : clazz.getDeclaredMethods()) {
                if (t.isAssignableFrom(method.getReturnType())) {
                    methods.add(method);
                }
            }
            // Exit if candidates exist.
            if (!methods.isEmpty()) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        return methods;
    }

    public static List<Field> getFieldsTypeWith(final Class<?> type, final Class<?> t) {
        final List<Field> fields = new ArrayList<>();
        Class<?> clazz = type;
        while (clazz != Object.class) {
            for (final Field field : clazz.getDeclaredFields()) {
                if (t.isAssignableFrom(field.getType())) {
                    fields.add(field);
                }
            }
            // Exit if candidates exist.
            if (!fields.isEmpty()) {
                break;
            }
            clazz = clazz.getSuperclass();
        }

        return fields;
    }

}

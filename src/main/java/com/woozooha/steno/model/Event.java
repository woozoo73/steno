package com.woozooha.steno.model;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class Event {

    private Type type;

    private String targetName;

    private String methodName;

    private List<String> arguments;

    public Event() {
    }

    public static Event of(Type type, Object target, Method method, Object[] args) {
        Event event = new Event();
        event.setType(type);
        if (target != null) {
            event.setTargetName(target.getClass().getName());
        }
        if (method != null) {
            event.setMethodName(method.getName());
        }
        List<String> arguments = null;
        if (args != null) {
            arguments = Arrays.stream(args).map(Object::toString).collect(Collectors.toList());
        }
        event.setArguments(arguments);

        return event;
    }

    public static enum Type {
        Before, After,
    }

}

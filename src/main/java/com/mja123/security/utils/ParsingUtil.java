package com.mja123.security.utils;

import com.mja123.security.domain.dto.IDTO;
import com.mja123.security.persistence.entity.IEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ParsingUtil {
    /*
        Get method in an object starting with specific prefix
     */
    public static Optional<Method> getMethodWithPrefix(String methodPrefix, Class<?> objectClass) {
        return Arrays.stream(objectClass.getMethods())
                .filter(m -> m.getName().startsWith(methodPrefix))
                .findFirst();
    }

    /*
        Get not null values in attributes in an object
     */
    public static Map<String, Object> getNotEmptyAttribute(List<Method> getters, Class<?> objectClass) {
        Map<String, Object> notEmptyAttribute = new HashMap<>();
        getters.forEach(m -> {
            try {
                Object getterValue = m.invoke(objectClass);
                String attributeName = m.getName().split("^get")[1];
                if (getterValue != null) {
                    notEmptyAttribute.put(attributeName, getterValue);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        return notEmptyAttribute;
    }

    // Fix it
    public static Class<?> setMethodsFromEntity(String attribute, Object value, Class<?> objectClass) {
        String setter = "set".concat(attribute);
        try {
            objectClass.getMethod(setter).invoke(objectClass, value);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}

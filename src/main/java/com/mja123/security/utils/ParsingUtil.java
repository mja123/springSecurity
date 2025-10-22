package com.mja123.security.utils;

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

    /*
        Set value in a dto via setters
     */
    public static void setMethodsFromEntity(String attribute, Object value, Class<?> classObject) {
        String setter = "set".concat(attribute);
        try {
            classObject.getMethod(setter).invoke(classObject, value);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setAttributesFromEntityToEntity(IEntity origin, IEntity target) {
        List<Method> entityMethods = Arrays.stream(origin.getClass().getMethods()).toList();

        Map<String, Object> entityAttributes = getNotEmptyAttribute(entityMethods, origin.getClass());
        entityAttributes.forEach((a,v) -> setMethodsFromEntity(a, v, target.getClass()));
    }
}

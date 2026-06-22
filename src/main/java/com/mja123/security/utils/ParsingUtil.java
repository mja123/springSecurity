package com.mja123.security.utils;

import com.mja123.security.persistence.entity.IEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ParsingUtil {

    public static List<Method> getClassGetters(IEntity entity) {
        return Arrays.stream(entity.getClass().getMethods())
                .filter(m -> m.getName().startsWith("get"))
                .toList();
    }

    public static Map<String, Object> getNotEmptyAttribute(List<Method> getters, IEntity entity) {
        Map<String, Object> notEmptyAttribute = new HashMap<>();
        getters.forEach(m -> {
            try {
                Object getterValue = m.invoke(entity);
                String attributeName = m.getName().split("^get")[1];
                if (getterValue != null && !attributeName.equals("Class")) {
                    notEmptyAttribute.put(attributeName, getterValue);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        return notEmptyAttribute;
    }

    public static void setMethodsFromEntity(String attribute, Object value, IEntity entity) {
        String setter = "set".concat(attribute);
        try {
            Method setterMethod = entity.getClass().getMethod(setter, value.getClass());
            setterMethod.invoke(entity, value);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setAttributesFromEntityToEntity(IEntity origin, IEntity target) {
        List<Method> entityMethods = getClassGetters(target);
        Map<String, Object> entityAttributes = getNotEmptyAttribute(entityMethods, target);
        entityAttributes.forEach((a, v) -> setMethodsFromEntity(a, v, origin));
    }
}

package com.mja123.security.utils;

import com.mja123.security.persistence.entity.IEntity;
import com.mja123.security.persistence.entity.UserEntity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ParsingUtil {
    /*
        Get method in an object starting with specific prefix
     */
    public static List<Method> getClassGetters(UserEntity objectClass) {
        String getPrefix = "get";
        return Arrays.stream(objectClass.getClass().getMethods())
                .filter(m -> m.getName().startsWith(getPrefix))
                .toList();
    }

    /*
        Get not null values in attributes in an object
     */
    public static Map<String, Object> getNotEmptyAttribute(List<Method> getters, UserEntity objectClass) {
        Map<String, Object> notEmptyAttribute = new HashMap<>();
        getters.forEach(m -> {
            try {
                Object getterValue = m.invoke(objectClass);
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

    /*
        Set value in a dto via setters
     */
    public static void setMethodsFromEntity(String attribute, Object value, UserEntity classObject) {
        String setter = "set".concat(attribute);
        try {
            Method setterMethod = classObject.getClass().getMethod(setter, value.getClass());
            setterMethod.invoke(classObject, value);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setAttributesFromEntityToEntity(UserEntity origin, UserEntity target) {
        List<Method> entityMethods = getClassGetters(target);

        Map<String, Object> entityAttributes = getNotEmptyAttribute(entityMethods, target);
        entityAttributes.forEach((a,v) -> setMethodsFromEntity(a, v, origin));

    }
}

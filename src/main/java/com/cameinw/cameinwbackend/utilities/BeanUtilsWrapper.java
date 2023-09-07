package com.cameinw.cameinwbackend.utilities;

import org.springframework.beans.BeanUtils;

public class BeanUtilsWrapper {

    public static void copyNonNullProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static String[] getNullPropertyNames(Object source) {
        // Find the property names with null values in the source object.
        java.beans.BeanInfo beanInfo;

        try {
            beanInfo = java.beans.Introspector.getBeanInfo(source.getClass());
        } catch (java.beans.IntrospectionException e) {
            throw new RuntimeException(e);
        }

        java.util.List<String> nullPropertyNames = new java.util.ArrayList<>();
        for (java.beans.PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
            String propertyName = propertyDesc.getName();
            try {
                Object value = propertyDesc.getReadMethod().invoke(source);
                if (value == null) {
                    nullPropertyNames.add(propertyName);
                }
            } catch (Exception e) {
                // Handle any exceptions as needed.
                // You can log them or take other actions.
            }
        }

        return nullPropertyNames.toArray(new String[0]);
    }
}

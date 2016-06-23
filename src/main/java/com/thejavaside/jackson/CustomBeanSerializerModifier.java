package com.thejavaside.jackson;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created on 4/14/16.
 */
public class CustomBeanSerializerModifier extends BeanSerializerModifier {

    private final Set<String> includedProps;

    public CustomBeanSerializerModifier(Set<String> includedProperties) {
        this.includedProps = includedProperties;
    }

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config,
                                                     BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {

        if (includedProps == null) return beanProperties;

        List<BeanPropertyWriter> newList = new ArrayList<>();

        for (BeanPropertyWriter beanPropertyWriter: beanProperties) {

            if (includedProps.contains(beanPropertyWriter.getName())) {
                newList.add(beanPropertyWriter);
            }
        }

        return newList;

    }
}

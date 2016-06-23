package com.thejavaside.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

/**
 * Created on 4/14/16.
 */
public class PersonJSONFilter extends SimpleBeanPropertyFilter {

    @Override
    public void serializeAsField(Object pojo, JsonGenerator jgen,
                                 SerializerProvider provider, PropertyWriter writer) throws Exception {

        if ("name".equals(writer.getName())
                && "David".equals(((PersonWithFilter)pojo).getName())) {
            return;
        }
        super.serializeAsField(pojo, jgen, provider, writer);
    }
}

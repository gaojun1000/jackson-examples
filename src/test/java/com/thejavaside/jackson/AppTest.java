package com.thejavaside.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Unit test for simple App.
 */
public class AppTest {
    @Test
    public void testSerializeObject() throws JsonProcessingException {
        Person person = new Person("David", 39);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(person);
        System.out.println(jsonStr);

        jsonStr = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(person);
        System.out.println(jsonStr);
    }

    @Test
    public void testDeserializeObject() throws IOException {
        String jsonStr = "{\"name\":\"David\",\"age\":39}";
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue(jsonStr, Person.class);
        System.out.println(person.getName());
    }

    @Test
    public void testDeserializeObjectWithEnum() throws IOException {
        String jsonStr = "{\"name\":\"John\",\"age\":30,\"gender\":\"MALE\"}";
        ObjectMapper objectMapper = new ObjectMapper();
        Person person = objectMapper.readValue(jsonStr, Person.class);
        System.out.println(person.getName());
    }



    @Test
    public void testSerializeWithView() throws JsonProcessingException {
        Person person = new Person("David", 39);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writerWithView(Views.Internal.class).writeValueAsString(person);
        System.out.println(jsonStr);

        jsonStr = objectMapper.writerWithView(Views.External.class).writeValueAsString(person);
        System.out.println(jsonStr);
    }


    @Test
    public void testSerializeList() throws JsonProcessingException {
        Person david = new Person("David", 39);
        Person john = new Person("John", 30);

        List<Person> personList = new ArrayList<Person>();
        personList.add(david);
        personList.add(john);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonStr = objectMapper.writeValueAsString(personList);
        System.out.println(jsonStr);
    }

    @Test
    public void testDeserializeList() throws IOException {
        String jsonStr = "[{\"name\":\"David\",\"age\":39},{\"name\":\"John\",\"age\":30}]";

        ObjectMapper objectMapper = new ObjectMapper();
        List<Person> personList = objectMapper.readValue(jsonStr, new TypeReference<List<Person>>() {
        });
        System.out.println(personList);
    }

    @Test
    public void testWriteDateAsISO8601() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.registerModule(new JodaModule());
        DateTime dateTime = new DateTime();
        String jsonStr = objectMapper.writeValueAsString(dateTime);
        System.out.println(jsonStr);
    }

    @Test
    public void testSerializeObjectWithFilter() throws JsonProcessingException {

        PersonJSONFilter personJSONFilter = new PersonJSONFilter();
        FilterProvider filters = new SimpleFilterProvider().addFilter("personFilter", personJSONFilter);

        PersonWithFilter david = new PersonWithFilter("David", 39);
        PersonWithFilter john = new PersonWithFilter("John", 30);
        john.setGender(Gender.MALE);

        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = mapper.writer(filters).writeValueAsString(david);
        System.out.println(jsonStr);

        jsonStr = mapper.writer(filters).writeValueAsString(john);
        System.out.println(jsonStr);
    }

    @Test
    public void testSerializeObjectWithCustomSerializer() throws JsonProcessingException {

        Person david = new Person("David", 39);

        final Set<String> includedFields = new HashSet<>();
        includedFields.add("name");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                super.setupModule(context);
                context.addBeanSerializerModifier(new CustomBeanSerializerModifier(includedFields));
            }
        });
        String jsonStr = mapper.writeValueAsString(david);
        System.out.println(jsonStr);
    }

    @Test
    public void testDeserializePartialUpdate() throws IOException {

        Person david = new Person("David", 39);

        String jsonStr = "{\"age\":30}";

        ObjectMapper mapper = new ObjectMapper();
        david = mapper.readerForUpdating(david).readValue(jsonStr);
        System.out.println(david.getAge());
    }

}

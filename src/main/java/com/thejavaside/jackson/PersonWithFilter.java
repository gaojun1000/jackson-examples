package com.thejavaside.jackson;

import com.fasterxml.jackson.annotation.JsonFilter;

/**
 * Created on 4/14/16.
 */
@JsonFilter("personFilter")
public class PersonWithFilter extends Person {
    public PersonWithFilter(String name, int age) {
        super(name, age);
    }
}

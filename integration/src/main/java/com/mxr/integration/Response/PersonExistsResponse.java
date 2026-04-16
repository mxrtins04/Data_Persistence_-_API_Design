package com.mxr.integration.Response;

import com.mxr.integration.model.Person;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class PersonExistsResponse {
    public String status;
    public String message;
    public Person person;
}




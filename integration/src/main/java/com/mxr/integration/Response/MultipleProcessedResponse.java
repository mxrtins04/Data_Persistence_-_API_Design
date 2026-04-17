package com.mxr.integration.Response;

import com.mxr.integration.model.Person;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@Data
@Builder
@JsonPropertyOrder({"status", "data"})
public class MultipleProcessedResponse {
    public String status;
    public int count;
    public List<Person> data;
}
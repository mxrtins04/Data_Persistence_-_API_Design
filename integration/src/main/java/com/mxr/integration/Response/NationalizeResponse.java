package com.mxr.integration.Response;

import com.mxr.integration.model.CountryData;

import java.util.List;

import lombok.Data;

@Data
public class NationalizeResponse {
    int count;
    String name;
    List<CountryData> countries;
}
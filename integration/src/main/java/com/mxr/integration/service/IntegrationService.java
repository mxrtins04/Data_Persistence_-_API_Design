package com.mxr.integration.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.mxr.integration.Response.AgifyResponse;
import com.mxr.integration.Response.GenderizeResponse;
import com.mxr.integration.Response.NationalizeResponse;
import com.mxr.integration.exceptions.MissingGenderizeDataException;
import com.mxr.integration.exceptions.MissingOrEmptyNameException;
import com.mxr.integration.exceptions.AgifyExceptions.NullAgeException;
import com.mxr.integration.exceptions.NationalizeExceptions.MissingCountryDataException;
import com.mxr.integration.exceptions.InvalidNameException;
import com.mxr.integration.model.CountryData;


@Service
public class IntegrationService {
    RestTemplate restTemplate = new RestTemplate();
    

    public GenderizeResponse getGenderizeResponse(String name) {
        validateName(name);

        String genderizeUrl = "https://api.genderize.io/?name=" + name;
        GenderizeResponse genderizeResponse = restTemplate.getForObject(genderizeUrl, GenderizeResponse.class);

        if (genderizeResponse == null) throw new MissingGenderizeDataException("No prediction available for the provided name");
        
        String gender = genderizeResponse.getGender();
        int count = genderizeResponse.getSampleSize();
       
        if( gender == null || count == 0) throw new MissingGenderizeDataException("No prediction available for the provided name");
        return genderizeResponse;
    }

    public AgifyResponse getAgifyResponse(String name) {
        validateName(name);

        String agifyUrl = "https://api.agify.io?name=" + name;
        AgifyResponse agifyResponse = restTemplate.getForObject(agifyUrl, AgifyResponse.class);
        
        int age = agifyResponse.getAge();
    
        if( age == 0) throw new NullAgeException("Age cannot be null");
        return agifyResponse;
    }

    public NationalizeResponse getNationalizeResponse(String name) {
        validateName(name);

        String nationalizeUrl = "https://api.nationalize.io?name=" + name;
        NationalizeResponse nationalizeResponse = restTemplate.getForObject(nationalizeUrl, NationalizeResponse.class);
        List<CountryData> countries = nationalizeResponse.getCountries();
        if (countries.isEmpty()) throw new MissingCountryDataException("No country data available for the provided name");
        
        return nationalizeResponse;
    }

    private void validateName(String name) {
        if( name.isBlank()) throw new MissingOrEmptyNameException("Name cannot be empty");
        if( !name.matches("[a-zA-Z ]+")) throw new InvalidNameException("Name must contain only letters");
    }

}

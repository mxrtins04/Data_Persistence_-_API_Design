package com.mxr.integration.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.mxr.integration.Response.AgifyResponse;
import com.mxr.integration.Response.GenderizeResponse;
import com.mxr.integration.Response.NationalizeResponse;
import com.mxr.integration.exceptions.MissingGenderizeDataException;
import com.mxr.integration.exceptions.MissingOrEmptyNameException;
import com.mxr.integration.exceptions.PersonAlreadyExistsException;
import com.mxr.integration.exceptions.AgifyExceptions.NullAgeException;
import com.mxr.integration.exceptions.NationalizeExceptions.MissingCountryDataException;
import com.mxr.integration.exceptions.InvalidNameException;
import com.mxr.integration.model.CountryData;
import com.mxr.integration.model.Person;
import com.mxr.integration.repo.PersonRepoImpl;
import com.mxr.integration.spec.PersonSpecification;

@Service
public class IntegrationService {
    
    private final PersonRepoImpl repo;

    IntegrationService(PersonRepoImpl personRepoImpl) {
        this.repo = personRepoImpl;
    }
    
    RestTemplate restTemplate = new RestTemplate();

    public List<Person> savePerson(String name) {
        GenderizeResponse genderizeResponse = getGenderizeResponse(name);
        AgifyResponse agifyResponse = getAgifyResponse(name);
        NationalizeResponse nationalizeResponse = getNationalizeResponse(name);
        Person person = mapToPerson(genderizeResponse, agifyResponse, nationalizeResponse);
        
        Optional<Person> existingPerson = repo.findNameIgnoreCase(person.getName());
        if (existingPerson.isPresent()) {
            throw new PersonAlreadyExistsException();
        }

        Specification<Person> spec = Specification
                .where(PersonSpecification.hasGender(person.getGender()))
                .and(PersonSpecification.hasCountryId(person.getCountryId()))
                .and(PersonSpecification.hasAgeGroup(person.getAgeGroup()));

        return repo.findAll(spec);
    }

    private GenderizeResponse getGenderizeResponse(String name) {
        validateName(name);

        String genderizeUrl = "https://api.genderize.io/?name=" + name;
        GenderizeResponse genderizeResponse = restTemplate.getForObject(genderizeUrl, GenderizeResponse.class);

        if (genderizeResponse == null)
            throw new MissingGenderizeDataException("No prediction available for the provided name");

        String gender = genderizeResponse.getGender();
        int count = genderizeResponse.getSampleSize();

        if (gender == null || count == 0)
            throw new MissingGenderizeDataException("No prediction available for the provided name");
        return genderizeResponse;
    }

    private AgifyResponse getAgifyResponse(String name) {
        String agifyUrl = "https://api.agify.io?name=" + name;
        AgifyResponse agifyResponse = restTemplate.getForObject(agifyUrl, AgifyResponse.class);

        int age = agifyResponse.getAge();

        if (age == 0)
            throw new NullAgeException("Age cannot be null");
        return agifyResponse;
    }

    private NationalizeResponse getNationalizeResponse(String name) {
        String nationalizeUrl = "https://api.nationalize.io?name=" + name;
        NationalizeResponse nationalizeResponse = restTemplate.getForObject(nationalizeUrl, NationalizeResponse.class);
        List<CountryData> countries = nationalizeResponse.getCountries();
        if (countries.isEmpty())
            throw new MissingCountryDataException("No country data available for the provided name");

        return nationalizeResponse;
    }

    private Person mapToPerson(GenderizeResponse genderizeResponse, AgifyResponse agifyResponse,
            NationalizeResponse nationalizeResponse) {
        List<CountryData> countries = nationalizeResponse.getCountries();
        UUID id = UUID.randomUUID();
        return Person.builder()
                .id(id)
                .name(genderizeResponse.getName())
                .gender(genderizeResponse.getGender())
                .genderProbability(genderizeResponse.getProbability())
                .sampleSize(genderizeResponse.getSampleSize())
                .age(agifyResponse.getAge())
                .ageGroup(calculateAgeGroup(agifyResponse.getAge()))
                .countryId(getCountryWithHighestProbability(countries).getCountryId())
                .countryProbability(getCountryWithHighestProbability(countries).getProbability())
                .build();
    }

    
    
    private CountryData getCountryWithHighestProbability(List<CountryData> countries) {
        return countries.stream()
                .max((c1, c2) -> Double.compare(c1.getProbability(), c2.getProbability()))
                .orElseThrow(() -> new MissingCountryDataException("No country data available for the provided name"));
    }

    private String calculateAgeGroup(int age) {
        if (age <= 12 && age >= 0) return "child";
        if (age <= 19 && age >= 13) return "teenager";
        if (age <= 59 && age >= 20) return "adult";
        if (age >= 60) return "senior";
        return "senior";
    }
    
    private void validateName(String name) {
        if (name.isBlank())
            throw new MissingOrEmptyNameException("Name cannot be empty");
        if (!name.matches("[a-zA-Z ]+"))
            throw new InvalidNameException("Name must contain only letters");
    }

}

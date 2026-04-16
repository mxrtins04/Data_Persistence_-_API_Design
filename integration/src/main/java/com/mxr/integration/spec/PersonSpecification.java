package com.mxr.integration.spec;

import org.springframework.data.jpa.domain.Specification;

import com.mxr.integration.model.Person;

public class PersonSpecification {

    public static Specification<Person> hasGender(String gender) {
        return (root, query, cb) ->
            gender == null ? null : cb.equal(cb.lower(root.get("gender")), gender.toLowerCase());
    }

    public static Specification<Person> hasCountryId(String countryId) {
        return (root, query, cb) ->
            countryId == null ? null : cb.equal(cb.lower(root.get("countryId")), countryId.toLowerCase());
    }

    public static Specification<Person> hasAgeGroup(String ageGroup) {
        return (root, query, cb) ->
            ageGroup == null ? null : cb.equal(cb.lower(root.get("ageGroup")), ageGroup.toLowerCase());
    }
}
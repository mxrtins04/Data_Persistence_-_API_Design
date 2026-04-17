package com.mxr.integration.model;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;

import java.util.UUID;
import com.fasterxml.uuid.Generators;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Person {
    @Id
    UUID id;
    
    @PrePersist
    public void generateId() {
        this.id = Generators.timeBasedEpochGenerator().generate();
    }
    
    @NotNull
    private String name;

    @NotNull
    private String gender;

    private double genderProbability;

    private int sampleSize;

    private int age;

    private String ageGroup;

    private String countryId;

    private double countryProbability;

    @CreationTimestamp
    private Instant createdAt;


    
}

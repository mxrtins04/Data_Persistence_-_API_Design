package com.mxr.integration.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import com.mxr.integration.model.Person;

@Repository
public interface PersonRepoImpl extends JpaRepository<Person, UUID> {
    
}

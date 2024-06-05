package com.morris.opensquare.repositories;

import com.morris.opensquare.models.DropDownOptions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DropDownRepository extends MongoRepository<DropDownOptions, String> {

    @Query(value = "{}", fields = "{ 'organizations' : 1}")
    List<String> findAllOrganizations();

    @Query(value = "{}", fields = "{ 'professions' : 1}")
    List<String> findAllProfessions();

    @Query(value = "{}", fields = "{ 'ages' : 1}")
    List<String> findAllAges();
}

package com.morris.opensquare.repositories;

import com.morris.opensquare.models.DropDownOptions;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DropDownRepository extends MongoRepository<DropDownOptions, String> {
}

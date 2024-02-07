package com.morris.opensquare.repositories;

import com.morris.opensquare.models.DropDownOptions;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DropDownRepository extends MongoRepository<DropDownOptions, String> {
}

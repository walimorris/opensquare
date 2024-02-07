package com.morris.opensquare.repositories;

import com.morris.opensquare.models.validations.DisposableEmailDomain;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface DisposableEmailDomainRepository extends MongoRepository<DisposableEmailDomain, String> {

    @Query("{domainName :  '?0'}")
    DisposableEmailDomain findByDomainName(String domainName);
}

package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.DropDownOptions;
import com.morris.opensquare.repositories.DropDownRepository;
import com.morris.opensquare.services.DropDownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DropDownServiceImpl implements DropDownService {

    private final Optional<DropDownOptions> dropDownOptions;

    @Autowired
    public DropDownServiceImpl(DropDownRepository dropDownRepository) {
        DropDownOptions options = new DropDownOptions();
        this.dropDownOptions = dropDownRepository.findOne(Example.of(options));
    }

    @Override
    public List<String> getOrganizations() {
        return dropDownOptions.map(DropDownOptions::getOrganizations).orElse(null);
    }

    @Override
    public List<String> getProfessions() {
        return dropDownOptions.map(DropDownOptions::getProfessions).orElse(null);
    }

    @Override
    public List<String> getAgeRanges() {
        return dropDownOptions.map(DropDownOptions::getAges).orElse(null);
    }
}

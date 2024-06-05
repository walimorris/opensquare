package com.morris.opensquare.services.impl;

import com.morris.opensquare.repositories.DropDownRepository;
import com.morris.opensquare.services.DropDownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropDownServiceImpl implements DropDownService {

    private final DropDownRepository dropDownRepository;

    @Autowired
    public DropDownServiceImpl(DropDownRepository dropDownRepository) {
        this.dropDownRepository = dropDownRepository;
    }

    @Override
    public List<String> getOrganizations() {
        return dropDownRepository.findAllOrganizations();
    }

    @Override
    public List<String> getProfessions() {
        return dropDownRepository.findAllProfessions();
    }

    @Override
    public List<String> getAgeRanges() {
        return dropDownRepository.findAllAges();
    }
}

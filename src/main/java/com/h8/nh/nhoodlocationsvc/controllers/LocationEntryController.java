package com.h8.nh.nhoodlocationsvc.controllers;

import com.h8.nh.nhoodlocationsvc.domain.LocationEntry;
import com.h8.nh.nhoodlocationsvc.repositories.LocationEntryRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LocationEntryController implements InitializingBean {

    private LocationEntryRepository repository;

    @Autowired
    public LocationEntryController(LocationEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void afterPropertiesSet() {
        if (repository.count() == 0) {
            repository.save(LocationEntry.builder()
                    .message("Hello!")
                    .latitude(50.049683)
                    .longitude(19.944544)
                    .build());
        }
    }

    @GetMapping("/locations")
    @ResponseBody
    public ResponseEntity<Iterable<LocationEntry>> listAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}

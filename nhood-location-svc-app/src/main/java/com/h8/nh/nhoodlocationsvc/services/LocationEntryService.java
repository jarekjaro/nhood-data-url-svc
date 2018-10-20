package com.h8.nh.nhoodlocationsvc.services;

import com.h8.nh.nhoodlocationsvc.domain.LocationEntry;
import com.h8.nh.nhoodlocationsvc.repositories.LocationEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class LocationEntryService {

    private final LocationEntryRepository repository;

    @Autowired
    LocationEntryService(LocationEntryRepository repository) {
        this.repository = repository;
    }

    public List<LocationEntry> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<LocationEntry> findById(Long id) {
        return repository.findById(id);
    }

    public LocationEntry create(LocationEntry entry) {
        return repository.save(entry);
    }

    public Optional<LocationEntry> modify(Long id, LocationEntry entry) {
        return repository.findById(id)
                .map(l -> {
                    l.setMessage(entry.getMessage());
                    l.setLatitude(entry.getLatitude());
                    l.setLongitude(entry.getLongitude());
                    repository.save(l);
                    return l;
                });
    }

    public Optional<LocationEntry> delete(Long id) {
        return repository.findById(id)
                .map(l -> {
                    repository.delete(l);
                    return l;
                });
    }
}

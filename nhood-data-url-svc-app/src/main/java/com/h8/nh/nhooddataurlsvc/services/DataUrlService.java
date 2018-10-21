package com.h8.nh.nhooddataurlsvc.services;

import com.h8.nh.nhooddataurlsvc.domain.DataUrl;
import com.h8.nh.nhooddataurlsvc.repositories.DataUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DataUrlService {

    private final DataUrlRepository repository;

    @Autowired
    DataUrlService(DataUrlRepository repository) {
        this.repository = repository;
    }

    public List<DataUrl> findAll() {
        return StreamSupport.stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<DataUrl> findById(Long id) {
        return repository.findById(id);
    }

    public DataUrl create(DataUrl entry) {
        return repository.save(entry);
    }

    public Optional<DataUrl> modify(Long id, DataUrl entry) {
        return repository.findById(id)
                .map(l -> {
                    l.setKey(entry.getKey());
                    l.setUrl(entry.getUrl());
                    repository.save(l);
                    return l;
                });
    }

    public Optional<DataUrl> delete(Long id) {
        return repository.findById(id)
                .map(l -> {
                    repository.delete(l);
                    return l;
                });
    }
}

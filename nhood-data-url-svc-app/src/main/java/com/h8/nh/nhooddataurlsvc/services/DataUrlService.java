package com.h8.nh.nhooddataurlsvc.services;

import com.h8.nh.nhooddataurlsvc.domain.DataUrl;
import com.h8.nh.nhooddataurlsvc.repositories.DataUrlRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class DataUrlService {

    private final DataUrlRepository dataUrlRepository;

    DataUrlService(DataUrlRepository dataUrlRepository) {
        this.dataUrlRepository = dataUrlRepository;
    }

    public List<DataUrl> findAll() {
        return StreamSupport.stream(dataUrlRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public Optional<DataUrl> findById(Long id) {
        return dataUrlRepository.findById(id);
    }

    public DataUrl create(DataUrl incomingDataUrl) {
        return dataUrlRepository.save(incomingDataUrl);
    }

    public Optional<DataUrl> modify(Long id, DataUrl incomingDataUrl) {
        return dataUrlRepository.findById(id)
                .map(dataUrl -> {
                    dataUrl.setKey(incomingDataUrl.getKey());
                    dataUrl.setUrl(incomingDataUrl.getUrl());
                    dataUrlRepository.save(dataUrl);
                    return dataUrl;
                });
    }

    public Optional<DataUrl> delete(Long id) {
        return dataUrlRepository.findById(id)
                .map(dataUrl -> {
                    dataUrlRepository.delete(dataUrl);
                    return dataUrl;
                });
    }
}

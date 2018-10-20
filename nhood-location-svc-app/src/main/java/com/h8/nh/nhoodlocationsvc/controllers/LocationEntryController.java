package com.h8.nh.nhoodlocationsvc.controllers;

import com.h8.nh.nhoodlocationsvc.domain.LocationEntry;
import com.h8.nh.nhoodlocationsvc.dto.request.LocationEntryRequestDTO;
import com.h8.nh.nhoodlocationsvc.dto.response.LocationEntryResponseDTO;
import com.h8.nh.nhoodlocationsvc.services.LocationEntryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/locations")
public class LocationEntryController {

    private final ModelMapper mapper;

    private final LocationEntryService service;

    @Autowired
    public LocationEntryController(
            ModelMapper mapper,
            LocationEntryService service) {
        this.mapper = mapper;
        this.service = service;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Iterable<LocationEntryResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll()
                .stream()
                .map(l -> mapper.map(l, LocationEntryResponseDTO.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<LocationEntryResponseDTO> findById(
            @PathVariable Long id) {
        return service.findById(id)
                .map(l -> mapper.map(l, LocationEntryResponseDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity create(
            @Valid @RequestBody LocationEntryRequestDTO dto)
            throws URISyntaxException {
        var entry = mapper.map(dto, LocationEntry.class);
        var result = service.create(entry);
        return ResponseEntity.created(
                new URI("/locations/" + result.getId())).build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity modify(
            @PathVariable Long id, @Valid @RequestBody LocationEntryRequestDTO dto) {
        var entry = mapper.map(dto, LocationEntry.class);
        return service.modify(id, entry)
                .map(l -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity delete(
            @PathVariable Long id) {
        return service.delete(id)
                .map(l -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}

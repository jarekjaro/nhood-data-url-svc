package com.h8.nh.nhooddataurlsvc.controllers;

import com.h8.nh.nhooddataurlsvc.domain.DataUrl;
import com.h8.nh.nhooddataurlsvc.dto.request.DataUrlRequestDTO;
import com.h8.nh.nhooddataurlsvc.dto.response.DataUrlResponseDTO;
import com.h8.nh.nhooddataurlsvc.services.DataUrlService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/urls")
public class DataUrlController {

    private final ModelMapper mapper;

    private final DataUrlService dataUrlService;

    public DataUrlController(
            ModelMapper mapper,
            DataUrlService dataUrlService) {
        this.mapper = mapper;
        this.dataUrlService = dataUrlService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Iterable<DataUrlResponseDTO>> findAll() {
        return ResponseEntity.ok(dataUrlService.findAll()
                .stream()
                .map(dataUrl -> mapper.map(dataUrl, DataUrlResponseDTO.class))
                .collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DataUrlResponseDTO> findById(
            @PathVariable Long id) {
        return dataUrlService.findById(id)
                .map(dataUrl -> mapper.map(dataUrl, DataUrlResponseDTO.class))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity create(
            @Valid @RequestBody DataUrlRequestDTO dto)
            throws URISyntaxException {
        var entry = mapper.map(dto, DataUrl.class);
        var result = dataUrlService.create(entry);
        return ResponseEntity.created(
                new URI("/urls/" + result.getId())).build();
    }

    @PutMapping("/{id}")
    @ResponseBody
    public ResponseEntity modify(
            @PathVariable Long id, @Valid @RequestBody DataUrlRequestDTO dto) {
        var entry = mapper.map(dto, DataUrl.class);
        return dataUrlService.modify(id, entry)
                .map(dataUrl -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity delete(
            @PathVariable Long id) {
        return dataUrlService.delete(id)
                .map(dataUrl -> ResponseEntity.noContent().build())
                .orElse(ResponseEntity.notFound().build());
    }
}

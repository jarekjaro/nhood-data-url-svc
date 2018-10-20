package com.h8.nh.nhoodlocationsvc.dto.response;

import lombok.Data;

@Data
public class LocationEntryResponseDTO {

    private Long id;
    private String message;
    private Double longitude, latitude;
}

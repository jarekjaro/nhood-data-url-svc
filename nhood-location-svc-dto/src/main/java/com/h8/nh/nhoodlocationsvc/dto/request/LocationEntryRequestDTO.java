package com.h8.nh.nhoodlocationsvc.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class LocationEntryRequestDTO {

    @NotNull
    @Size(max=200, message="Field 'message' should not be longer than 200 characters")
    private String message;

    @NotNull
    private Double longitude, latitude;
}

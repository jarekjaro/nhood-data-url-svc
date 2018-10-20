package com.h8.nh.nhoodlocationsvc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class LocationEntry {

    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private Double longitude, latitude;
}

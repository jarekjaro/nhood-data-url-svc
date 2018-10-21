package com.h8.nh.nhooddataurlsvc.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class DataUrl {

    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection
    private List<String> key;
    private String url;
}

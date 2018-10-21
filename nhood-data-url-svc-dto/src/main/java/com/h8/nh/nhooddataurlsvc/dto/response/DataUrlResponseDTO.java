package com.h8.nh.nhooddataurlsvc.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class DataUrlResponseDTO {

    private Long id;
    private List<String> key;
    private String url;
}

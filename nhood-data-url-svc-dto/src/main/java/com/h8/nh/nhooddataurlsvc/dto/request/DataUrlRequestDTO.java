package com.h8.nh.nhooddataurlsvc.dto.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class DataUrlRequestDTO {

    @NotNull
    @NotEmpty
    private List<String> key;

    @NotNull
    @Size(max=200, message="Field 'url' should not be longer than 200 characters")
    private String url;
}

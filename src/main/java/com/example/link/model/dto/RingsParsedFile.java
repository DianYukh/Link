package com.example.link.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RingsParsedFile {
    private String fileName;
    private List<Ring> replicas;
    private Map<String, Integer> characterReplicasCount;
    private List<ParsingError> errors;
}

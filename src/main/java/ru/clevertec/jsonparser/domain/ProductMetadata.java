package ru.clevertec.jsonparser.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductMetadata {
    private UUID id;
    private String manufacturer;
    private Double weight;
}

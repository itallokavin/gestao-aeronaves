package com.sonda.gestao_aeronaves.web.dto;

import com.sonda.gestao_aeronaves.domain.Brand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AircraftDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Brand is required")
    private Brand brand;

    @NotNull(message = "Year is required")
    private Integer year;

    @NotBlank(message = "Description is required")
    private String description;

    private boolean sold;
    private LocalDateTime created;
    private LocalDateTime updated;
}
package com.sonda.gestao_aeronaves.mapper;

import com.sonda.gestao_aeronaves.web.dto.AircraftDTO;
import com.sonda.gestao_aeronaves.persistence.entity.Aircraft;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AircraftMapper {

    AircraftDTO toDTO(Aircraft entity);
    Aircraft toEntity(AircraftDTO dto);
}
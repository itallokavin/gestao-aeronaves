package com.sonda.gestao_aeronaves.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sonda.gestao_aeronaves.domain.Brand;
import com.sonda.gestao_aeronaves.domain.exception.AircraftNotFoundException;
import com.sonda.gestao_aeronaves.service.AircraftService;
import com.sonda.gestao_aeronaves.web.dto.AircraftDTO;
import com.sonda.gestao_aeronaves.web.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AircraftController.class)
@Import(GlobalExceptionHandler.class)
class AircraftControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @MockitoBean
    private AircraftService service;

    @Test
    @DisplayName("GET /aeronaves deve retornar 200 e lista de aeronaves")
    void listAll_DeveRetornar200ComLista() throws Exception {
        AircraftDTO dto = createAircraftDTO(1L, "E195", Brand.EMBRAER, 2020, "Descrição", false);
        when(service.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/aeronaves"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("E195"))
                .andExpect(jsonPath("$[0].brand").value("EMBRAER"));

        verify(service).findAll();
    }

    @Test
    @DisplayName("GET /aeronaves/find?term=EMBRAER deve retornar 200 com lista filtrada")
    void findByTerm_DeveRetornar200ComListaFiltrada() throws Exception {
        AircraftDTO dto = createAircraftDTO(1L, "E195", Brand.EMBRAER, 2020, "Descrição", false);
        when(service.findByTerm("EMBRAER")).thenReturn(List.of(dto));

        mockMvc.perform(get("/aeronaves/find").param("term", "EMBRAER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("E195"));

        verify(service).findByTerm("EMBRAER");
    }

    @Test
    @DisplayName("GET /aeronaves/{id} quando existe deve retornar 200")
    void getById_QuandoExiste_DeveRetornar200() throws Exception {
        AircraftDTO dto = createAircraftDTO(1L, "E195", Brand.EMBRAER, 2020, "Descrição", false);
        when(service.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/aeronaves/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("E195"));

        verify(service).findById(1L);
    }

    @Test
    @DisplayName("GET /aeronaves/{id} quando não existe deve retornar 404")
    void getById_QuandoNaoExiste_DeveRetornar404() throws Exception {
        when(service.findById(999L)).thenThrow(new AircraftNotFoundException(999L));

        mockMvc.perform(get("/aeronaves/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Aircraft not found with ID: 999"));

        verify(service).findById(999L);
    }

    @Test
    @DisplayName("POST /aeronaves deve retornar 201 com aeronave criada")
    void create_DeveRetornar201ComAeronaveCriada() throws Exception {
        AircraftDTO inputDTO = createAircraftDTO(null, "B737", Brand.BOEING, 2019, "Boeing 737", false);
        AircraftDTO outputDTO = createAircraftDTO(1L, "B737", Brand.BOEING, 2019, "Boeing 737", false);
        when(service.save(any(AircraftDTO.class))).thenReturn(outputDTO);

        mockMvc.perform(post("/aeronaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("B737"));

        verify(service).save(any(AircraftDTO.class));
    }

    @Test
    @DisplayName("POST /aeronaves com dados inválidos deve retornar 400")
    void create_ComDadosInvalidos_DeveRetornar400() throws Exception {
        AircraftDTO invalidDTO = createAircraftDTO(null, "", null, null, "", false);

        mockMvc.perform(post("/aeronaves")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));

        verify(service, never()).save(any());
    }

    @Test
    @DisplayName("PUT /aeronaves/{id} deve retornar 200 com aeronave atualizada")
    void update_DeveRetornar200ComAeronaveAtualizada() throws Exception {
        AircraftDTO inputDTO = createAircraftDTO(null, "E195-E2", Brand.EMBRAER, 2022, "Atualizado", false);
        AircraftDTO outputDTO = createAircraftDTO(1L, "E195-E2", Brand.EMBRAER, 2022, "Atualizado", false);
        when(service.update(eq(1L), any(AircraftDTO.class))).thenReturn(outputDTO);

        mockMvc.perform(put("/aeronaves/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("E195-E2"));

        verify(service).update(eq(1L), any(AircraftDTO.class));
    }

    @Test
    @DisplayName("DELETE /aeronaves/{id} quando existe deve retornar 204")
    void delete_QuandoExiste_DeveRetornar204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/aeronaves/1"))
                .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

    @Test
    @DisplayName("DELETE /aeronaves/{id} quando não existe deve retornar 404")
    void delete_QuandoNaoExiste_DeveRetornar404() throws Exception {
        doThrow(new AircraftNotFoundException("Aircraft not found for deletion with ID: 999"))
                .when(service).delete(999L);

        mockMvc.perform(delete("/aeronaves/999"))
                .andExpect(status().isNotFound());

        verify(service).delete(999L);
    }

    @Test
    @DisplayName("GET /aeronaves/statistics/unsold deve retornar 200 com contagem")
    void countUnsold_DeveRetornar200ComContagem() throws Exception {
        when(service.countUnsold()).thenReturn(5L);

        mockMvc.perform(get("/aeronaves/statistics/unsold"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(service).countUnsold();
    }

    @Test
    @DisplayName("GET /aeronaves/statistics/by-decade deve retornar 200 com mapa")
    void listByDecade_DeveRetornar200ComMapa() throws Exception {
        when(service.listByDecade()).thenReturn(Map.of(2020, 3L, 2010, 2L));

        mockMvc.perform(get("/aeronaves/statistics/by-decade"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.2020").value(3))
                .andExpect(jsonPath("$.2010").value(2));

        verify(service).listByDecade();
    }

    @Test
    @DisplayName("GET /aeronaves/statistics/last-week deve retornar 200 com lista")
    void findLastWeek_DeveRetornar200ComLista() throws Exception {
        AircraftDTO dto = createAircraftDTO(1L, "E195", Brand.EMBRAER, 2020, "Descrição", false);
        when(service.findLastWeek()).thenReturn(List.of(dto));

        mockMvc.perform(get("/aeronaves/statistics/last-week"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("E195"));

        verify(service).findLastWeek();
    }

    private AircraftDTO createAircraftDTO(Long id, String name, Brand brand, Integer year, String description, boolean sold) {
        AircraftDTO dto = new AircraftDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setBrand(brand);
        dto.setYear(year);
        dto.setDescription(description);
        dto.setSold(sold);
        dto.setCreated(LocalDateTime.now());
        dto.setUpdated(LocalDateTime.now());
        return dto;
    }
}

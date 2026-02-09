package com.sonda.gestao_aeronaves.service;

import com.sonda.gestao_aeronaves.domain.Brand;
import com.sonda.gestao_aeronaves.domain.exception.AircraftNotFoundException;
import com.sonda.gestao_aeronaves.mapper.AircraftMapper;
import com.sonda.gestao_aeronaves.persistence.entity.Aircraft;
import com.sonda.gestao_aeronaves.persistence.repository.AircraftRepository;
import com.sonda.gestao_aeronaves.web.dto.AircraftDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AircraftServiceTest {

    @Mock
    private AircraftRepository repository;

    @Mock
    private AircraftMapper mapper;

    @InjectMocks
    private AircraftService service;

    private Aircraft aircraft;
    private AircraftDTO aircraftDTO;

    @BeforeEach
    void setUp() {
        aircraft = createAircraft(1L, "E195", Brand.EMBRAER, 2020, "Aircraft description", false);
        aircraftDTO = createAircraftDTO(1L, "E195", Brand.EMBRAER, 2020, "Aircraft description", false);
    }

    @Test
    @DisplayName("findAll deve retornar lista de aeronaves")
    void findAll_DeveRetornarListaDeAeronaves() {
        when(repository.findAll()).thenReturn(List.of(aircraft));
        when(mapper.toDTO(aircraft)).thenReturn(aircraftDTO);

        List<AircraftDTO> result = service.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("E195");
        assertThat(result.get(0).getBrand()).isEqualTo(Brand.EMBRAER);
        verify(repository).findAll();
        verify(mapper).toDTO(aircraft);
    }

    @Test
    @DisplayName("findByTerm com termo deve buscar por termo")
    void findByTerm_ComTermo_DeveBuscarPorTermo() {
        when(repository.findByTerm("EMBRAER")).thenReturn(List.of(aircraft));
        when(mapper.toDTO(aircraft)).thenReturn(aircraftDTO);

        List<AircraftDTO> result = service.findByTerm("EMBRAER");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBrand()).isEqualTo(Brand.EMBRAER);
        verify(repository).findByTerm("EMBRAER");
        verify(repository, never()).findAll();
    }

    @Test
    @DisplayName("findByTerm sem termo deve retornar findAll")
    void findByTerm_SemTermo_DeveRetornarFindAll() {
        when(repository.findAll()).thenReturn(List.of(aircraft));
        when(mapper.toDTO(aircraft)).thenReturn(aircraftDTO);

        List<AircraftDTO> result = service.findByTerm(null);

        assertThat(result).hasSize(1);
        verify(repository).findAll();
        verify(repository, never()).findByTerm(any());
    }

    @Test
    @DisplayName("findByTerm com termo vazio deve retornar findAll")
    void findByTerm_ComTermoVazio_DeveRetornarFindAll() {
        when(repository.findAll()).thenReturn(List.of(aircraft));
        when(mapper.toDTO(aircraft)).thenReturn(aircraftDTO);

        List<AircraftDTO> result = service.findByTerm("   ");

        assertThat(result).hasSize(1);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("findById quando existe deve retornar aeronave")
    void findById_QuandoExiste_DeveRetornarAeronave() {
        when(repository.findById(1L)).thenReturn(Optional.of(aircraft));
        when(mapper.toDTO(aircraft)).thenReturn(aircraftDTO);

        AircraftDTO result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("E195");
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("findById quando não existe deve lançar AircraftNotFoundException")
    void findById_QuandoNaoExiste_DeveLancarExcecao() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(AircraftNotFoundException.class)
                .hasMessageContaining("Aircraft not found with ID: 999");

        verify(repository).findById(999L);
        verify(mapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("save deve salvar e retornar aeronave criada")
    void save_DeveSalvarERetornarAeronaveCriada() {
        AircraftDTO inputDTO = createAircraftDTO(null, "B737", Brand.BOEING, 2019, "Boeing 737", false);
        Aircraft savedEntity = createAircraft(1L, "B737", Brand.BOEING, 2019, "Boeing 737", false);
        AircraftDTO outputDTO = createAircraftDTO(1L, "B737", Brand.BOEING, 2019, "Boeing 737", false);

        when(mapper.toEntity(inputDTO)).thenReturn(new Aircraft());
        when(repository.save(any(Aircraft.class))).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(outputDTO);

        AircraftDTO result = service.save(inputDTO);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("B737");
        verify(repository).save(any(Aircraft.class));
    }

    @Test
    @DisplayName("update quando existe deve atualizar aeronave")
    void update_QuandoExiste_DeveAtualizarAeronave() {
        AircraftDTO inputDTO = createAircraftDTO(null, "E195-E2", Brand.EMBRAER, 2022, "Updated", false);
        Aircraft updatedEntity = createAircraft(1L, "E195-E2", Brand.EMBRAER, 2022, "Updated", false);
        AircraftDTO outputDTO = createAircraftDTO(1L, "E195-E2", Brand.EMBRAER, 2022, "Updated", false);

        when(repository.existsById(1L)).thenReturn(true);
        when(mapper.toEntity(inputDTO)).thenReturn(new Aircraft());
        when(repository.save(any(Aircraft.class))).thenReturn(updatedEntity);
        when(mapper.toDTO(updatedEntity)).thenReturn(outputDTO);

        AircraftDTO result = service.update(1L, inputDTO);

        assertThat(result.getName()).isEqualTo("E195-E2");
        verify(repository).existsById(1L);
        verify(repository).save(any(Aircraft.class));
    }

    @Test
    @DisplayName("update quando não existe deve lançar AircraftNotFoundException")
    void update_QuandoNaoExiste_DeveLancarExcecao() {
        AircraftDTO inputDTO = createAircraftDTO(null, "E195", Brand.EMBRAER, 2020, "Desc", false);
        when(repository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.update(999L, inputDTO))
                .isInstanceOf(AircraftNotFoundException.class)
                .hasMessageContaining("Aircraft not found for update with ID: 999");

        verify(repository).existsById(999L);
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("delete quando existe deve remover aeronave")
    void delete_QuandoExiste_DeveRemoverAeronave() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository).existsById(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("delete quando não existe deve lançar AircraftNotFoundException")
    void delete_QuandoNaoExiste_DeveLancarExcecao() {
        when(repository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(999L))
                .isInstanceOf(AircraftNotFoundException.class)
                .hasMessageContaining("Aircraft not found for deletion with ID: 999");

        verify(repository).existsById(999L);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("countUnsold deve retornar quantidade de aeronaves não vendidas")
    void countUnsold_DeveRetornarQuantidadeNaoVendidas() {
        when(repository.countBySoldFalse()).thenReturn(5L);

        long result = service.countUnsold();

        assertThat(result).isEqualTo(5L);
        verify(repository).countBySoldFalse();
    }

    @Test
    @DisplayName("findLastWeek deve retornar aeronaves criadas na última semana")
    void findLastWeek_DeveRetornarAeronavesCriadasNaUltimaSemana() {
        when(repository.findByCreatedAfter(any(LocalDateTime.class))).thenReturn(List.of(aircraft));
        when(mapper.toDTO(aircraft)).thenReturn(aircraftDTO);

        List<AircraftDTO> result = service.findLastWeek();

        assertThat(result).hasSize(1);
        verify(repository).findByCreatedAfter(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("listByDecade deve agrupar aeronaves por década")
    void listByDecade_DeveAgruparPorDecada() {
        Aircraft a1 = createAircraft(1L, "E195", Brand.EMBRAER, 2020, "Desc", false);
        Aircraft a2 = createAircraft(2L, "E190", Brand.EMBRAER, 2015, "Desc", false);
        when(repository.findAll()).thenReturn(List.of(a1, a2));

        Map<Integer, Long> result = service.listByDecade();

        assertThat(result).containsEntry(2020, 1L).containsEntry(2010, 1L);
        verify(repository).findAll();
    }

    private Aircraft createAircraft(Long id, String name, Brand brand, int year, String description, boolean sold) {
        Aircraft a = new Aircraft();
        a.setId(id);
        a.setName(name);
        a.setBrand(brand);
        a.setYear(year);
        a.setDescription(description);
        a.setSold(sold);
        a.setCreated(LocalDateTime.now());
        a.setUpdated(LocalDateTime.now());
        return a;
    }

    private AircraftDTO createAircraftDTO(Long id, String name, Brand brand, int year, String description,
            boolean sold) {
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

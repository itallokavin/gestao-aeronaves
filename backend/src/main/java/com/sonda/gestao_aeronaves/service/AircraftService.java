package com.sonda.gestao_aeronaves.service;

import com.sonda.gestao_aeronaves.web.dto.AircraftDTO;
import com.sonda.gestao_aeronaves.domain.exception.AircraftNotFoundException;
import com.sonda.gestao_aeronaves.persistence.entity.Aircraft;
import com.sonda.gestao_aeronaves.persistence.repository.AircraftRepository;
import com.sonda.gestao_aeronaves.mapper.AircraftMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class for Aircraft business logic.
 * Handles validation, data manipulation, and interaction with the repository.
 */
@Service
@RequiredArgsConstructor
public class AircraftService {

    private final AircraftRepository repository;
    private final AircraftMapper mapper;

    /**
     * Retrieves all aircraft.
     * 
     * @return List of AircraftDTO.
     */
    @Transactional(readOnly = true)
    public List<AircraftDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds aircraft matching a term.
     * 
     * @param term Search term.
     * @return List of matching AircraftDTO.
     */
    @Transactional(readOnly = true)
    public List<AircraftDTO> findByTerm(String term) {
        if (!StringUtils.hasText(term)) {
            return findAll();
        }
        return repository.findByTerm(term)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Finds an aircraft by ID.
     * 
     * @param id The ID.
     * @return AircraftDTO.
     * @throws AircraftNotFoundException if not found.
     */
    @Transactional(readOnly = true)
    public AircraftDTO findById(Long id) {
        Aircraft entity = repository.findById(id)
                .orElseThrow(() -> new AircraftNotFoundException(id));
        return mapper.toDTO(entity);
    }

    /**
     * Saves a new aircraft.
     * 
     * @param dto Data to save.
     * @return Saved AircraftDTO.
     */
    @Transactional
    public AircraftDTO save(AircraftDTO dto) {
        validateYear(dto.getYear());
        Aircraft entity = mapper.toEntity(dto);
        Aircraft saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    /**
     * Updates an existing aircraft.
     * 
     * @param id  ID to update.
     * @param dto New data.
     * @return Updated AircraftDTO.
     * @throws AircraftNotFoundException if not found.
     */
    @Transactional
    public AircraftDTO update(Long id, AircraftDTO dto) {
        if (!repository.existsById(id)) {
            throw new AircraftNotFoundException("Aircraft not found for update with ID: " + id);
        }
        validateYear(dto.getYear());
        Aircraft entity = mapper.toEntity(dto);
        entity.setId(id);
        return mapper.toDTO(repository.save(entity));
    }

    /**
     * Deletes an aircraft.
     * 
     * @param id ID to delete.
     * @throws AircraftNotFoundException if not found.
     */
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new AircraftNotFoundException("Aircraft not found for deletion with ID: " + id);
        }
        repository.deleteById(id);
    }

    /**
     * Counts unsold aircraft.
     * 
     * @return Count.
     */
    @Transactional(readOnly = true)
    public long countUnsold() {
        return repository.countBySoldFalse();
    }

    /**
     * Finds aircraft created in the last week.
     * 
     * @return List of AircraftDTO.
     */
    @Transactional(readOnly = true)
    public List<AircraftDTO> findLastWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return repository.findByCreatedAfter(oneWeekAgo).stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Groups aircraft count by decade.
     * 
     * @return Map of Decade -> Count.
     */
    @Transactional(readOnly = true)
    public Map<Integer, Long> listByDecade() {
        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        a -> (a.getYear() / 10) * 10,
                        Collectors.counting()));
    }

    private void validateYear(Integer year) {
        if (year != null && year > java.time.Year.now().getValue()) {
            throw new IllegalArgumentException("O ano de fabricação não pode ser maior que o ano atual.");
        }
    }
}
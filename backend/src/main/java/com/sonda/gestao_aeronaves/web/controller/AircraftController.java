package com.sonda.gestao_aeronaves.web.controller;

import com.sonda.gestao_aeronaves.web.dto.AircraftDTO;
import com.sonda.gestao_aeronaves.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for managing Aircraft resources.
 * Provides endpoints for CRUD operations and statistics.
 */
@RestController
@RequestMapping("/aeronaves")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AircraftController {

    private final AircraftService service;

    /**
     * Lists all aircraft.
     * 
     * @return List of all aircraft DTOs.
     */
    @GetMapping
    public ResponseEntity<List<AircraftDTO>> listAll() {
        return ResponseEntity.ok(service.findAll());
    }

    /**
     * Finds aircraft by a search term (ID, name, or brand).
     * 
     * @param term Search term.
     * @return List of matching aircraft.
     */
    @GetMapping("/find")
    public ResponseEntity<List<AircraftDTO>> findByTerm(@RequestParam(required = false) String term) {
        return ResponseEntity.ok(service.findByTerm(term));
    }

    /**
     * Gets an aircraft by its ID.
     * 
     * @param id The ID of the aircraft.
     * @return The aircraft DTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AircraftDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    /**
     * Creates a new aircraft.
     * 
     * @param dto The aircraft data.
     * @return The created aircraft.
     */
    @PostMapping
    public ResponseEntity<AircraftDTO> create(@Valid @RequestBody AircraftDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dto));
    }

    /**
     * Updates an existing aircraft.
     * 
     * @param id  The ID of the aircraft to update.
     * @param dto The new data.
     * @return The updated aircraft.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AircraftDTO> update(@PathVariable Long id, @Valid @RequestBody AircraftDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    /**
     * Deletes an aircraft by ID.
     * 
     * @param id The ID of the aircraft to delete.
     * @return No content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Counts the number of unsold aircraft.
     * 
     * @return Count of available aircraft.
     */
    @GetMapping("/statistics/unsold")
    public ResponseEntity<Long> countUnsold() {
        return ResponseEntity.ok(service.countUnsold());
    }

    /**
     * Groups aircraft by decade of manufacture.
     * 
     * @return Map of decade -> count.
     */
    @GetMapping("/statistics/by-decade")
    public ResponseEntity<Map<Integer, Long>> listByDecade() {
        return ResponseEntity.ok(service.listByDecade());
    }

    /**
     * Lists aircraft created in the last week.
     * 
     * @return List of recent aircraft.
     */
    @GetMapping("/statistics/last-week")
    public ResponseEntity<List<AircraftDTO>> findLastWeek() {
        return ResponseEntity.ok(service.findLastWeek());
    }
}
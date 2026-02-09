package com.sonda.gestao_aeronaves.persistence.entity;

import com.sonda.gestao_aeronaves.domain.Brand;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aeronave")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "marca", nullable = false)
    private Brand brand;

    @Column(name = "ano", nullable = false)
    private Integer year;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String description;

    @Column(name = "vendido")
    private boolean sold;

    @Column(name = "created", updatable = false)
    private LocalDateTime created;

    @Column(name = "updated")
    private LocalDateTime updated;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
        updated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }
}
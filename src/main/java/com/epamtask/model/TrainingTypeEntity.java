package com.epamtask.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import org.hibernate.annotations.Proxy;
@Entity
@Proxy(lazy = false)
@Table(name = "training_type")
public class TrainingTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TrainingType type;

    public TrainingTypeEntity() {}

    public TrainingTypeEntity(Long id, TrainingType type) {
        this.id = id;
        this.type = type;
    }
    public TrainingTypeEntity(TrainingType type) {
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public TrainingType getType() {
        return type;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setType(TrainingType type) {
        this.type = type;
    }
}
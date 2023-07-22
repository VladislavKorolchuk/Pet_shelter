package com.example.pet_shelter.repository;

import com.example.pet_shelter.model.Shelters;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends JpaRepository<Shelters, Long> {
}

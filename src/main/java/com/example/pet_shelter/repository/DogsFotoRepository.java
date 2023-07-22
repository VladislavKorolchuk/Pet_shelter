package com.example.pet_shelter.repository;

import com.example.pet_shelter.model.DogsFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogsFotoRepository extends JpaRepository<DogsFoto, Long> {

}

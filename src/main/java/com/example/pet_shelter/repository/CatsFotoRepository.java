package com.example.pet_shelter.repository;

import com.example.pet_shelter.model.CatsFoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatsFotoRepository extends JpaRepository<CatsFoto, Long> {

}

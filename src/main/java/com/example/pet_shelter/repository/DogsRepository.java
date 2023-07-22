package com.example.pet_shelter.repository;

import com.example.pet_shelter.model.Dogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogsRepository extends JpaRepository <Dogs,Long> {

}

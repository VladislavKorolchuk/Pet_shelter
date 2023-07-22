package com.example.pet_shelter.repository;

import com.example.pet_shelter.model.Cats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatsRepository extends JpaRepository <Cats,Long> {

}

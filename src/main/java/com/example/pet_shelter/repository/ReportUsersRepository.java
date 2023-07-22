package com.example.pet_shelter.repository;

import com.example.pet_shelter.model.ReportUsers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportUsersRepository extends JpaRepository<ReportUsers,Long> {

    List<ReportUsers> getAllBy();

}

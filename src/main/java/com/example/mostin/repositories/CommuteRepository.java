package com.example.mostin.repositories;

import com.example.mostin.models.Commute;
import com.example.mostin.models.CommuteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommuteRepository extends JpaRepository<Commute, CommuteId> {
    List<Commute> findByEmployeeIdAndCommuteDayBetween(String employeeId, LocalDate startDate, LocalDate endDate);
    List<Commute> findByEmployeeIdAndCommuteDay(String employeeId, LocalDate commuteDay);
    Optional<Commute> findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc(String employeeId, String employeeName);
}

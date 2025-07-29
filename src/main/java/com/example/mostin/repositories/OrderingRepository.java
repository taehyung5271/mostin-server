package com.example.mostin.repositories;

import com.example.mostin.models.Ordering;
import com.example.mostin.models.OrderingId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderingRepository extends JpaRepository<Ordering, OrderingId> {
    List<Ordering> findByEmployeeId(String employeeId);
    List<Ordering> findByEmployeeIdOrderByOrderingDayDesc(String employeeId);
    List<Ordering> findByEmployeeIdAndOrderingDay(String employeeId, LocalDate orderingDay);
    void deleteByEmployeeIdAndOrderingDay(String employeeId, LocalDate orderingDay);
}

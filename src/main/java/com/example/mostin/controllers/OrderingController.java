package com.example.mostin.controllers;

import com.example.mostin.models.Ordering;
import com.example.mostin.repositories.OrderingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderingController {

    @Autowired
    private OrderingRepository orderingRepository;

    @PostMapping
    public Ordering createOrder(@RequestBody Ordering order) {
        order.setOrderingDay(LocalDate.now());
        return orderingRepository.save(order);
    }

    @GetMapping
    public List<Ordering> getOrdersByEmployee(@RequestParam String employeeId) {
        return orderingRepository.findByEmployeeIdOrderByOrderingDayDesc(employeeId);
    }

    @GetMapping("/details")
    public List<Ordering> getOrderDetailsByDate(@RequestParam String employeeId, @RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        return orderingRepository.findByEmployeeIdAndOrderingDay(employeeId, localDate);
    }

    @Transactional
    @DeleteMapping
    public ResponseEntity<?> deleteOrdersByDate(@RequestParam String employeeId, @RequestParam String date) {
        LocalDate today = LocalDate.now(); // Use server's current date
        orderingRepository.deleteByEmployeeIdAndOrderingDay(employeeId, today);
        return ResponseEntity.ok().build();
    }
}

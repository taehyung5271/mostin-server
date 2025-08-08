package com.example.mostin.controllers;

import com.example.mostin.models.Commute;
import com.example.mostin.repositories.CommuteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/commute")
public class CommuteController {

    @Autowired
    private CommuteRepository commuteRepository;

    @PostMapping("/clock-in")
    public Commute clockIn(@RequestBody Map<String, String> payload) {
        Commute commute = new Commute();
        commute.setEmployeeId(payload.get("employeeId"));
        commute.setEmployeeName(payload.get("employeeName"));
        commute.setWorkPlaceName(payload.get("workPlaceName"));
        commute.setCommuteDay(LocalDate.parse(payload.get("commuteDay")));
        commute.setStartTime(LocalTime.parse(payload.get("startTime")));
        return commuteRepository.save(commute);
    }

    @PutMapping("/clock-out")
    public ResponseEntity<Commute> clockOut(@RequestBody Map<String, String> payload) {
        String employeeId = payload.get("employeeId");
        LocalDate today = LocalDate.now();

        return commuteRepository.findByEmployeeIdAndCommuteDay(employeeId, today).stream().findFirst()
                .map(commute -> {
                    commute.setEndTime(LocalTime.parse(payload.get("endTime")));
                    return ResponseEntity.ok(commuteRepository.save(commute));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/today")
    public ResponseEntity<Commute> getTodayCommute(@RequestParam String employeeId) {
        return commuteRepository.findByEmployeeIdAndCommuteDay(employeeId, LocalDate.now()).stream().findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/monthly")
    public List<Commute> getMonthlyCommute(@RequestParam String employeeId, @RequestParam int year, @RequestParam int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        return commuteRepository.findByEmployeeIdAndCommuteDayBetween(employeeId, startDate, endDate);
    }

    @GetMapping("/recent")
    public ResponseEntity<Commute> getRecentCommute(@RequestParam String employeeId, @RequestParam String employeeName) {
        return commuteRepository.findTopByEmployeeIdAndEmployeeNameOrderByCommuteDayDescStartTimeDesc(employeeId, employeeName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

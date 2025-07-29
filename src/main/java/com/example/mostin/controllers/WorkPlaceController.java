package com.example.mostin.controllers;

import com.example.mostin.models.WorkPlace;
import com.example.mostin.repositories.WorkPlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workplaces")
public class WorkPlaceController {

    @Autowired
    private WorkPlaceRepository workPlaceRepository;

    @GetMapping
    public List<WorkPlace> getAllWorkPlaces() {
        return workPlaceRepository.findAll();
    }

    @PostMapping
    public WorkPlace createWorkPlace(@RequestBody WorkPlace workPlace) {
        return workPlaceRepository.save(workPlace);
    }

    @GetMapping("/{name}")
    public ResponseEntity<WorkPlace> getWorkPlaceByName(@PathVariable String name) {
        return workPlaceRepository.findById(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

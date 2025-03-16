package com.mg.apidaaalumni.alumnus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/alumnus")
public class AlumnusController {
    private final AlumnusService alumnusService;

    @Autowired
    public AlumnusController(AlumnusService alumnusService) {
        this.alumnusService = alumnusService;
    }

    @GetMapping
    public List<Alumnus> getAllAlums(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String pastRoles,
            @RequestParam(required = false) String industry) {

        if (name != null) {
            return alumnusService.getAlumsByName(name);
        }
        else if (year != null) {
            return alumnusService.getAlumsByYear(year);
        }
        else if (pastRoles != null) {
            return alumnusService.getAlumsByPastRoles(pastRoles);
        }
        else if (industry != null) {
            return alumnusService.getAlumsByIndustry(industry);
        }
        else {
            return alumnusService.getAllAlums();
        }
    }

    @PostMapping
    public ResponseEntity<Alumnus> addAlumnus(@RequestBody Alumnus alumnus) {
        Alumnus createdAlumnus = alumnusService.addAlumnus(alumnus);
        return new ResponseEntity<>(createdAlumnus, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<Alumnus> updateAlumnus(@RequestBody Alumnus alumnus) {
        Alumnus updatedAlumnus = alumnusService.updateAlumnus(alumnus);

        if (updatedAlumnus != null) {
            return new ResponseEntity<>(updatedAlumnus, HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{alumnusId}")
    public ResponseEntity<String> deleteAlumnus(@PathVariable Integer alumnusId) {
        alumnusService.deleteAlumnus(alumnusId);
        return new ResponseEntity<>("Alumnus deleted successfully", HttpStatus.OK);
    }
}

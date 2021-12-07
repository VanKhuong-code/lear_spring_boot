package com.example.sqljpasqlserver.controller;

import com.example.sqljpasqlserver.model.response.ResponseList;
import com.example.sqljpasqlserver.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/car")
public class CarController {
    @Autowired
    private CarService carService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllCar() {
        ResponseList<?> result = carService.getAllCars();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/year/{year}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getAllCar(@PathVariable("year") int year) {
        ResponseList<?> result = carService.getCarByYear(year);
        return ResponseEntity.ok().body(result);
    }
}

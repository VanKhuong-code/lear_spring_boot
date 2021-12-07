package com.example.sqljpasqlserver.service;

import com.example.sqljpasqlserver.model.response.ResponseList;
import org.springframework.stereotype.Service;

@Service
public interface CarService {
    ResponseList<?> getAllCars();

    ResponseList<?> getCarByYear(Integer year);
}

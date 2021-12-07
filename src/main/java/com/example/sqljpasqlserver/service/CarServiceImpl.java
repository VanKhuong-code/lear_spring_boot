package com.example.sqljpasqlserver.service;

import com.example.sqljpasqlserver.entity.Car;
import com.example.sqljpasqlserver.model.response.ResponseList;
import com.example.sqljpasqlserver.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CarServiceImpl implements CarService {
    @Autowired
    CarRepository carRepository;


    @Override
    public ResponseList<?> getAllCars() {
        List<Car> result = carRepository.getAllCar();
        return new ResponseList<>(true,result,null,null);
    }

    @Override
    public ResponseList<?> getCarByYear(Integer year) {
        List<Car>result=carRepository.findCarsByYear(year);
        return new ResponseList<>(true,result,null,null);
    }
}

package com.example.sqljpasqlserver.repository;

import com.example.sqljpasqlserver.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    /*    @Procedure("findAllCar")*/


   @Query(value = "CALL FIND_CARS_AFTER_YEAR",nativeQuery = true)
    List<Car> getAllCar();


    //goi stored sql

    @Query(value = "CALL selectCarByGroupYear(:year);", nativeQuery = true)
    List<Car> findCarsByYear(@Param("year") Integer year);
}


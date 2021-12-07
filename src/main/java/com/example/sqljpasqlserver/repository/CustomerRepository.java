package com.example.sqljpasqlserver.repository;

import com.example.sqljpasqlserver.entity.Customers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customers, Long> {

    @Query(value = "SELECT * FROM tbl_customers WHERE date_create>=:dateStart AND date_create<=:dateEnd", nativeQuery = true)
    List<Customers> selectCustomerByDate(@Param("dateStart") String dateStart, @Param("dateEnd") String dateEnd);


    @Query(value = "SELECT * FROM tbl_customers ORDER BY id ASC OFFSET 10* =:page ROWS FETCH NET 10 ROWS ONLY", nativeQuery = true)
    List<Customers> selectCustomerByPage(@Param("page") int page);


    Page<Customers> findAll(Pageable pageable);

}

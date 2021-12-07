package com.example.sqljpasqlserver.service;

import com.example.sqljpasqlserver.entity.Customers;
import com.example.sqljpasqlserver.model.response.ResponseList;
import com.example.sqljpasqlserver.model.response.ResponseObject;
import com.example.sqljpasqlserver.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public ResponseList<?> getCustomerByDateCreate(String DateCreate, String DateCreate2) {
        List<Customers> listCustomer = customerRepository.selectCustomerByDate(DateCreate, DateCreate2);
        return new ResponseList<>(true, listCustomer, null, null);
    }

    @Override
    public ResponseList<?> getCustomerByPage(int page, int size) {
        try {
            List<Customers> customers = new ArrayList<>();
            Pageable paging = PageRequest.of(page, size);
            Page<Customers> pages = customerRepository.findAll(paging);
            customers = pages.getContent();
            return new ResponseList<>(true, customers, null, null, pages.getNumber(), pages.getTotalElements(), pages.getTotalPages());


        } catch (Exception e) {
            return new ResponseList<>(false, null, 500, "Có lỗi xảy ra");
        }
    }

    @Override
    public ResponseObject<?> getDetailsCustomerBy(long id) {

        Optional<Customers> customers = customerRepository.findById(id);
        if (customers == null) {
            return new ResponseObject<>(false, null, 404, "Not customer by id :" + id);
        }
        return new ResponseObject<>(true, customers, null, null);
    }


}

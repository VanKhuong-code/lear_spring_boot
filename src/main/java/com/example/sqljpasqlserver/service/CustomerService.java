package com.example.sqljpasqlserver.service;

import com.example.sqljpasqlserver.model.response.ResponseList;
import com.example.sqljpasqlserver.model.response.ResponseObject;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public interface CustomerService {
    ResponseList<?> getCustomerByDateCreate(String DateCreate, String DateCreate2);

    ResponseList<?> getCustomerByPage(int page, int size);

    ResponseObject<?> getDetailsCustomerBy(long id);


}

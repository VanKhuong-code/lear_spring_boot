package com.example.sqljpasqlserver.controller;

import com.example.sqljpasqlserver.model.response.ResponseList;
import com.example.sqljpasqlserver.model.response.ResponseObject;
import com.example.sqljpasqlserver.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("api/work")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customer1")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getListCustomer(@RequestParam("dateStart") String date1, @RequestParam("dateEnd") String date2) {
        ResponseList<?> result = customerService.getCustomerByDateCreate(date1, date2);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getCustomerByPage(@RequestParam("page") int page, @RequestParam("size") int size) {
        ResponseList<?> result = customerService.getCustomerByPage(page, size);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/customer/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> getDetailsCustomer(@PathVariable("id") long id) {
        ResponseObject<?> result = customerService.getDetailsCustomerBy(id);
        return ResponseEntity.ok(result);

    }


}

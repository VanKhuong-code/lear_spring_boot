package com.example.sqljpasqlserver.controller;

import com.example.sqljpasqlserver.model.response.ResponseList;
import com.example.sqljpasqlserver.model.response.ResponseObject;
import com.example.sqljpasqlserver.model.request.CreateUserReq;
import com.example.sqljpasqlserver.model.request.LoginReq;
import com.example.sqljpasqlserver.model.request.UpdateUserReq;
import com.example.sqljpasqlserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@CrossOrigin(origins = "*",maxAge = 3600)
@RequestMapping("/api/auth")
@Controller
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/user")
    public ResponseEntity<?> getListUser() {
        ResponseList<?> responseList = userService.getListUser();
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody CreateUserReq req) {
        ResponseObject<?> result = userService.registerUser(req);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateUserReq req, @PathVariable int id) {
        ResponseObject<?> result = userService.updateUser(req, id);
        return ResponseEntity.ok(result);

    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        ResponseObject<?> result = userService.deleteUser(id);
        return ResponseEntity.ok(new ResponseObject<>(true, result, null, null));
    }

    @PostMapping("/user/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("userId") int userId, @RequestParam("name") String nameAvata) {
        try {
            //save file to a foler
            ResponseObject<?> generatedFileName = userService.storeFile(file, userId, nameAvata);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject<>(true, generatedFileName, null, null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new ResponseObject<>(false, null, null, "Có lỗi sảy ra"));

        }
    }

    @GetMapping("/user/upload/{fileName}")
    public ResponseEntity<?> readDetailFile(@PathVariable String fileName) {
        try {
            byte[] bytes = userService.readFileContent(fileName);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(bytes);

        } catch (Exception e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginReq req) {
        ResponseObject<?> result = userService.loginUser(req);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


}

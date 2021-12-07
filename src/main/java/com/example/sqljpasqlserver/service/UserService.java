package com.example.sqljpasqlserver.service;

import com.example.sqljpasqlserver.model.response.ResponseList;
import com.example.sqljpasqlserver.model.response.ResponseObject;
import com.example.sqljpasqlserver.model.request.CreateUserReq;
import com.example.sqljpasqlserver.model.request.LoginReq;
import com.example.sqljpasqlserver.model.request.UpdateUserReq;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface UserService {
    public ResponseList<?> getListUser();

    public ResponseObject<?> registerUser(CreateUserReq req);

    public ResponseObject<?> updateUser(UpdateUserReq req, long id);

    public ResponseObject<?> deleteUser(long id);

    public ResponseObject<?> storeFile(MultipartFile file, long userId, String nameAvata);

    public byte[] readFileContent(String fileName);

    public ResponseObject<?> loginUser(LoginReq req);

}

package com.example.sqljpasqlserver.service;

import com.example.sqljpasqlserver.entity.ERole;
import com.example.sqljpasqlserver.entity.Role;
import com.example.sqljpasqlserver.model.response.ResponseList;
import com.example.sqljpasqlserver.model.response.ResponseObject;
import com.example.sqljpasqlserver.entity.User;
import com.example.sqljpasqlserver.model.dto.UserDto;
import com.example.sqljpasqlserver.model.mapper.UserMapper;
import com.example.sqljpasqlserver.model.request.CreateUserReq;
import com.example.sqljpasqlserver.model.request.LoginReq;
import com.example.sqljpasqlserver.model.request.UpdateUserReq;
import com.example.sqljpasqlserver.model.response.JwtResponse;
import com.example.sqljpasqlserver.repository.RoleRepository;
import com.example.sqljpasqlserver.repository.UserRepository;
import com.example.sqljpasqlserver.security.jwt.AuthEntryPointJwt;
import com.example.sqljpasqlserver.security.jwt.JwtUtils;
import com.example.sqljpasqlserver.security.services.UserDetailsImpl;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);


    //cung project tham chieu thu muc upload anh
    private final Path UPLOAD_DIR = Paths.get("uploads");


    public UserServiceImpl() {
        try {
            Files.createDirectories(UPLOAD_DIR);
        } catch (Exception exception) {
            System.out.println("ImageServiceImpl ex:" + exception.getMessage());
            throw new RuntimeException("cannot initialize storage," + exception);
        }
    }

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public ResponseList<?> getListUser() {
        List<User> listUser = userRepository.findAll();

        List<UserDto> result = new ArrayList<>();
        for (User user : listUser) {
            result.add(UserMapper.toUserDto(user));
        }

        return new ResponseList<>(true, result, null, null);
    }

    @Override
    public ResponseObject<?> registerUser(CreateUserReq req) {

        if (userRepository.existsByUsername(req.getUsername())) {
            return new ResponseObject<>(false, null, 0, "Username is already taken !");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            return new ResponseObject<>(false, null, 0, "Email is already in use !");

        }


        //tao user
        User user = UserMapper.toUser(req);

        Set<String> strRoles = req.getRole();
        Set<Role> roles = new HashSet<>();

        //mac dinh user
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;

                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }

            });
        }

        user.setRoles(roles);
        /* user = UserMapper.toUser(req);*/
        userRepository.save(user);
        return new ResponseObject<>(true, "User registered successfully!", null, null);
    }

    @Override
    public ResponseObject<?> updateUser(UpdateUserReq req, long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return new ResponseObject<>(false, null, 404, "No user found");
        }
        User updateUser = UserMapper.toUser(req, id);
        try {
            userRepository.save(updateUser);

        } catch (Exception exception) {
            return new ResponseObject<>(false, null, 404, "Có lỗi xảy ra");
        }

        return new ResponseObject<>(true, UserMapper.toUserDto(updateUser), null, null);
    }

    @Override
    public ResponseObject<?> deleteUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return new ResponseObject<>(false, null, 404, "User not exit");
        }
        try {
            userRepository.deleteById(id);
            return new ResponseObject<>(true, "Success", null, null);
        } catch (Exception exception) {
            return new ResponseObject<>(false, null, 404, "Có lỗi xảy ra");
        }
    }

    private boolean isImageFile(MultipartFile file) {
        //check fileNameUtils
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        return Arrays.asList(new String[]{"png", "jpg", "jpeg", "bmp"})
                .contains(fileExtension.trim().toLowerCase());

    }

    @Override
    public ResponseObject<?> storeFile(MultipartFile file, long userId, String nameAvata) {
        try {
            if (file.isEmpty()) {
                return new ResponseObject<>(false, null, null, "Failed to save empty file");
            }
            //check file is image
            if (!isImageFile(file)) {
                return new ResponseObject<>(false, null, null, "You can only upload image file");

            }
            //check file <=5M
            float fileSize = file.getSize() / 1_000_000;
            if (fileSize > 5.0f) {
                return new ResponseObject<>(false, null, null, "File must be <= 5Mb");

            }

            //file must be rename
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = "userId_" + userId + "_" + nameAvata.trim();
            generatedFileName = generatedFileName + "." + fileExtension;

            Path destinationFilePath = this.UPLOAD_DIR.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();
            if (!destinationFilePath.getParent().equals(this.UPLOAD_DIR.toAbsolutePath())) {
                return new ResponseObject<>(false, null, null, "Có lỗi xảy ra");

            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

            }
            Optional<User> user = userRepository.findById(userId);
            if (!user.isEmpty()) {
                try {
                    /*  userRepository.selectAll();*/
                    userRepository.updateAvatar(generatedFileName, userId);
                } catch (Exception e) {
                    System.out.println("updateAvatar:" + e.getMessage());
                    return new ResponseObject<>(false, null, null, "Có lỗi xảy ra");
                }
            }
            return new ResponseObject<>(true, generatedFileName, null, null);

        } catch (Exception exception) {
            return new ResponseObject<>(false, null, null, "Có lỗi xảy ra");

        }
    }

    @Override
    public byte[] readFileContent(String fileName) {
        try {
            Path file = UPLOAD_DIR.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                byte[] bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            } else {
                throw new RuntimeException("Could not read file:" + fileName);
            }

        } catch (Exception exception) {
            throw new RuntimeException("Could not read file:" + fileName);
        }
    }

    @Override
    public ResponseObject<?> loginUser(LoginReq req) {
        try {
            // Xác thực từ username và password.
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    req.getUsername(),
                    req.getPassword()
            ));
            // Nếu không xảy ra exception tức là thông tin hợp lệ
            // Set thông tin authentication vào Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
            //gen token
            String token = jwtUtils.generateJwtToken(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            return new ResponseObject<>(true, new JwtResponse(token, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles), null, null);
        } catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            return new ResponseObject<>(false, null, 404, "Xác thực không thành công !");

        }

    }





}

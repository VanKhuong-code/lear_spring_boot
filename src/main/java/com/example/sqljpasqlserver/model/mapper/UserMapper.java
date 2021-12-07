package com.example.sqljpasqlserver.model.mapper;

import com.example.sqljpasqlserver.entity.ERole;
import com.example.sqljpasqlserver.entity.Role;
import com.example.sqljpasqlserver.entity.User;
import com.example.sqljpasqlserver.model.dto.UserDto;
import com.example.sqljpasqlserver.model.request.CreateUserReq;
import com.example.sqljpasqlserver.model.request.UpdateUserReq;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashSet;
import java.util.Set;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto tmp = new UserDto();
        tmp.setId(user.getId());
        tmp.setName(user.getUsername());
        tmp.setPhone(user.getPhone());
        tmp.setAvatar(user.getAvatar());
        tmp.setEmail(user.getEmail());
        tmp.setBirthday(user.getBirthday());

        return tmp;
    }


    public static User toUser(CreateUserReq req) {
        Set<Role> roles = new HashSet<>();
        Role role=new Role();
        role.setName(ERole.ROLE_USER);
        roles.add(role);
        User user = new User();
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        user.setPhone(req.getPhone());
        // Hash password using BCrypt
        String hash = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hash);
        user.setRoles(roles);

        return user;
    }

    public static User toUser(UpdateUserReq req, long id) {
        User user = new User();
        user.setId(id);
        user.setAvatar(req.getAvatar());
        user.setEmail(req.getEmail());
        user.setUsername(req.getName());
        Set<Role> roles = new HashSet<>();
        Role role=new Role();
        role.setName(ERole.ROLE_USER);
        user.setRoles(roles);
        user.setBirthday(req.getBirthday());
        // Hash password using BCrypt
        String hash = BCrypt.hashpw(req.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hash);

        return user;
    }

    public static User updateAvatar(User user) {
        User userUpdate = new User();
        userUpdate.setId(user.getId());
        userUpdate.setEmail(user.getEmail());
        userUpdate.setPhone(user.getPhone());
        // Hash password using BCrypt
        String hash = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(hash);
        userUpdate.setUsername(user.getUsername());
        userUpdate.setAvatar(user.getAvatar());
        userUpdate.setBirthday(user.getBirthday());
        userUpdate.setRoles(user.getRoles());
        return userUpdate;
    }

}

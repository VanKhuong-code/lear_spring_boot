package com.example.sqljpasqlserver.repository;

import com.example.sqljpasqlserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
     User findByEmail(String email);

    @Transactional
    @Modifying()
    @Query(value = "update tbl_user set avatar=:avatar where id=:id", nativeQuery = true)
    void updateAvatar(String avatar, long id);


    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);






   /*
    có nhiều cách viết:
    -Derived Query Methods in Spring Data JPA Repositories
    -Spring Data JPA @Modifying Annotation
    -học cách thứ 2

    1.JPQL

    */
}

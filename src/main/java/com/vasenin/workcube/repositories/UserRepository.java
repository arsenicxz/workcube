package com.vasenin.workcube.repositories;

import com.vasenin.workcube.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByName(String name);
    User findByNickname(String nickname);
}

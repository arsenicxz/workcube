package com.vasenin.workcube.repositories;

import com.vasenin.workcube.domains.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Badge findByName(String name);
}

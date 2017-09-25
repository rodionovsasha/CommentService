package com.github.rodionovsasha.commentservice.repositories;

import com.github.rodionovsasha.commentservice.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

package com.github.rodionovsasha.commentservice.repositories;

import com.github.rodionovsasha.commentservice.entities.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
}

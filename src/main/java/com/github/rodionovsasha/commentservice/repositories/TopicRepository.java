package com.github.rodionovsasha.commentservice.repositories;

import com.github.rodionovsasha.commentservice.entities.Topic;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByOwnerId(long ownerId, Sort sort);

    List<Topic> findByTitleContainingOrderByDateDesc(String titleFragment, PageRequest pageRequest);
}

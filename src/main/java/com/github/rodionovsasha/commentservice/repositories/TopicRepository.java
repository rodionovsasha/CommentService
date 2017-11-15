package com.github.rodionovsasha.commentservice.repositories;

import com.github.rodionovsasha.commentservice.entities.Topic;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends BaseRepository<Topic> {
    List<Topic> findByOwnerId(int ownerId, Sort sort);

    List<Topic> findByTitleContainingIgnoreCaseOrderByDateDesc(String titleFragment, Pageable pageable);
}

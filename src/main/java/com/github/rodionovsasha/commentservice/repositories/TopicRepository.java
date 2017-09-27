package com.github.rodionovsasha.commentservice.repositories;

import com.github.rodionovsasha.commentservice.entities.Topic;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends BaseRepository<Topic> {
    @Override
    Optional<Topic> findOne(long id);

    List<Topic> findByOwnerId(long ownerId, Sort sort);

    List<Topic> findByTitleContainingOrderByDateDesc(String titleFragment, PageRequest pageRequest);
}

package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.Topic;

import org.springframework.data.domain.Sort;

import java.util.List;

public interface TopicService {
    Topic start(String title, long userId);

    void archive(long topicId, long userId);

    List<Topic> listForUser(long userId, Sort sort);

    List<Topic> search(String titleFragment, int limit);

    Topic getById(long id);
}

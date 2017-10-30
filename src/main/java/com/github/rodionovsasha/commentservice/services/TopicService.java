package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.Topic;

import org.springframework.data.domain.Sort;

import java.util.List;

public interface TopicService {
    Topic start(String title, int userId);

    void archive(int topicId, int userId);

    List<Topic> listForUser(int userId, Sort sort);

    List<Topic> search(String titleFragment, int size);

    Topic getById(int id);

    Topic getActiveTopic(int id);

    void checkTopicExists(int id);
}

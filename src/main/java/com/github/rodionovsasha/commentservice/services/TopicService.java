package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException;

public interface TopicService {
    Topic addTopic(Topic topic);
    Topic updateTopic(Topic topic) throws TopicNotFoundException;
    void deleteTopic(long id);
    Topic getTopicById(long id) throws TopicNotFoundException;
}

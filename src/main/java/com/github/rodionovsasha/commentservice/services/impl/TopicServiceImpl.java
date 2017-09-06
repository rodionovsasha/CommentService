package com.github.rodionovsasha.commentservice.services.impl;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException;
import com.github.rodionovsasha.commentservice.repositories.TopicRepository;
import com.github.rodionovsasha.commentservice.services.TopicService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository repository;

    @Override
    @Transactional
    public Topic addTopic(final Topic topic) {
        return repository.saveAndFlush(topic);
    }

    @Override
    @Transactional
    public Topic updateTopic(final Topic topic) throws TopicNotFoundException {
        val currentTopic = getTopicById(topic.getId());
        currentTopic.setName(topic.getName());
        currentTopic.setUser(topic.getUser());
        return repository.saveAndFlush(currentTopic);
    }

    @Override
    @Transactional
    public void deleteTopic(final long id) {
        repository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Topic getTopicById(final long id) throws TopicNotFoundException {
        val topic = repository.findOne(id);
        if (topic == null) {
            throw new TopicNotFoundException("Topic with id '" + id + "' not found");
        }
        return topic;
    }
}

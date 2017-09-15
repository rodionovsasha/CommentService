package com.github.rodionovsasha.commentservice.services.impl;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException;
import com.github.rodionovsasha.commentservice.exceptions.PermissionException;
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;
import com.github.rodionovsasha.commentservice.repositories.TopicRepository;
import com.github.rodionovsasha.commentservice.services.TopicService;
import com.github.rodionovsasha.commentservice.services.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@AllArgsConstructor
@Service @Transactional
public class TopicServiceImpl implements TopicService {
    private final TopicRepository repository;
    private final UserService userService;

    @Override
    public Topic start(String title, long userId) throws UserNotFoundException, InactiveUserException {
        return repository.saveAndFlush(new Topic(title, userService.getActive(userId)));
    }

    @Override
    public void delete(long topicId, long userId) throws Exception {
        val topic = getById(topicId);
        userService.getActive(userId);
        if (topic.getStarter().getId() != userId) {
            throw new PermissionException(String.format("Non-topic starter with id=%d is trying to delete the topic", userId));
        }
        repository.delete(topicId);
    }

    @Override
    public List<Topic> listForUser(long userId, Sort sort) throws UserNotFoundException {
        userService.getById(userId);
        return repository.findByStarterId(userId, sort);
    }

    @Override
    public List<Topic> search(String titleFragment, int limit) {
        return repository.findByTitleContaining(titleFragment, new PageRequest(0, limit));
    }

    @Override
    @Transactional(readOnly = true)
    public Topic getById(long id) throws TopicNotFoundException {
        val topic = repository.findOne(id);
        if (topic == null) {
            throw new TopicNotFoundException(String.format("Topic with id '%d' not found", id));
        }
        return topic;
    }
}

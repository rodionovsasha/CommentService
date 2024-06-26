package com.github.rodionovsasha.commentservice.services.impl;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.exceptions.ArchivedTopicException;
import com.github.rodionovsasha.commentservice.exceptions.TopicAccessException;
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException;
import com.github.rodionovsasha.commentservice.repositories.TopicRepository;
import com.github.rodionovsasha.commentservice.services.TopicService;
import com.github.rodionovsasha.commentservice.services.UserService;
import lombok.AllArgsConstructor;
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
    public Topic start(String title, int userId) {
        return repository.save(new Topic(title, userService.getActiveUser(userId)));
    }

    @Override
    public void archive(int topicId, int userId) {
        userService.checkUserActive(userId);
        var topic = getById(topicId);
        if (topic.getOwner().getId() != userId) {
            throw TopicAccessException.forId(userId);
        }
        topic.setArchived(true);
        repository.save(topic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Topic> listForUser(int userId, Sort sort) {
        userService.checkUserActive(userId);
        return repository.findByOwnerId(userId, sort);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Topic> search(String titleFragment, int size) {
        return repository.findByTitleContainingIgnoreCaseOrderByDateDesc(titleFragment, PageRequest.of(0, size));
    }

    @Override
    @Transactional(readOnly = true)
    public Topic getById(int id) {
        return repository.findById(id).orElseThrow(() -> TopicNotFoundException.forId(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Topic getActiveTopic(int id) {
        var topic = getById(id);
        if (topic.isArchived()) {
            throw ArchivedTopicException.forId(id);
        }
        return topic;
    }

    @Override
    public void checkTopicExists(int id) {
        getById(id);
    }
}

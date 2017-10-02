package com.github.rodionovsasha.commentservice.services.impl;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.exceptions.TopicAccessException;
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException;
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
    public Topic start(String title, long userId) {
        return repository.save(new Topic(title, userService.getActiveUser(userId)));
    }

    @Override
    public void archive(long topicId, long userId) {
        userService.checkUserActive(userId);
        val topic = getById(topicId);
        if (topic.getOwner().getId() != userId) {
            throw TopicAccessException.forId(userId);
        }
        topic.setArchived(true);
        repository.save(topic);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Topic> listForUser(long userId, Sort sort) {
        userService.checkUserActive(userId);
        return repository.findByOwnerId(userId, sort);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Topic> search(String titleFragment, int size) {
        return repository.findByTitleContainingIgnoreCaseOrderByDateDesc(titleFragment, new PageRequest(0, size));
    }

    @Override
    @Transactional(readOnly = true)
    public Topic getById(long id) {
        return repository.findOne(id).orElseThrow(() -> TopicNotFoundException.forId(id));
    }
}

package com.github.rodionovsasha.commentservice.services.impl;

import com.github.rodionovsasha.commentservice.entities.Comment;
import com.github.rodionovsasha.commentservice.repositories.CommentRepository;
import com.github.rodionovsasha.commentservice.services.CommentService;
import com.github.rodionovsasha.commentservice.services.TopicService;
import com.github.rodionovsasha.commentservice.services.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service @Transactional
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final TopicService topicService;
    private final CommentRepository repository;

    @Override
    public Comment add(String content, long topicId, long userId) {
        val user = userService.getActiveUser(userId);
        val topic = topicService.getActiveTopic(topicId);
        return repository.save(new Comment(content, user, topic));
    }
}

package com.github.rodionovsasha.commentservice.services.impl;

import com.github.rodionovsasha.commentservice.entities.Comment;
import com.github.rodionovsasha.commentservice.exceptions.CommentAccessException;
import com.github.rodionovsasha.commentservice.exceptions.CommentNotFoundException;
import com.github.rodionovsasha.commentservice.repositories.CommentRepository;
import com.github.rodionovsasha.commentservice.services.CommentService;
import com.github.rodionovsasha.commentservice.services.TopicService;
import com.github.rodionovsasha.commentservice.services.UserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Consumer;

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

    @Override
    public void updateContent(long commentId, long userId, String content) {
        update(commentId, userId, comment -> comment.setContent(content));
    }

    @Override
    public void archive(long commentId, long userId) {
        update(commentId, userId, comment -> comment.setArchived(true));
    }

    private Comment getById(long id) {
        return repository.findOne(id).orElseThrow(() -> CommentNotFoundException.forId(id));
    }

    private void update(long commentId, long userId, Consumer<Comment> consumer) {
        userService.checkUserActive(userId);
        val comment = getById(commentId);
        if (comment.getUser().getId() != userId) {
            throw CommentAccessException.forId(userId);
        }
        consumer.accept(comment);
        repository.save(comment);
    }
}

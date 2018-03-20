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

import java.util.List;
import java.util.function.Consumer;

@AllArgsConstructor
@Service @Transactional
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final TopicService topicService;
    private final CommentRepository repository;

    @Override
    public Comment add(String content, int topicId, int userId) {
        val user = userService.getActiveUser(userId);
        val topic = topicService.getActiveTopic(topicId);
        return repository.save(new Comment(content, user, topic));
    }

    @Override
    public void update(int commentId, int userId, String content) {
        update(commentId, userId, comment -> comment.setContent(content));
    }

    @Override
    public void archive(int commentId, int userId) {
        update(commentId, userId, comment -> comment.setArchived(true));
    }

    @Override
    public List<Comment> findByTopic(int topicId) {
        topicService.checkTopicExists(topicId);
        return repository.findByTopicIdAndArchivedFalseOrderByDateAsc(topicId);
    }

    private Comment getById(int id) {
        return repository.findById(id).orElseThrow(() -> CommentNotFoundException.forId(id));
    }

    private void update(int commentId, int userId, Consumer<Comment> consumer) {
        userService.checkUserActive(userId);
        val comment = getById(commentId);
        if (comment.getUser().getId() != userId) {
            throw CommentAccessException.forId(userId);
        }
        consumer.accept(comment);
        repository.save(comment);
    }
}

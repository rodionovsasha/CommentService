package com.github.rodionovsasha.commentservice.services.impl;

import com.github.rodionovsasha.commentservice.entities.Comment;
import com.github.rodionovsasha.commentservice.exceptions.CommentNotFoundException;
import com.github.rodionovsasha.commentservice.repositories.CommentRepository;
import com.github.rodionovsasha.commentservice.services.CommentService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;

    @Override
    @Transactional
    public Comment addComment(final Comment comment) {
        return repository.saveAndFlush(comment);
    }

    @Override
    @Transactional
    public Comment updateComment(final Comment comment) throws CommentNotFoundException {
        val currentComment = getCommentById(comment.getId());
        currentComment.setContent(comment.getContent());
        currentComment.setUser(comment.getUser());
        currentComment.setTopic(comment.getTopic());
        return repository.saveAndFlush(currentComment);
    }

    @Override
    @Transactional
    public void deleteComment(final long id) {
        repository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getCommentById(final long id) throws CommentNotFoundException {
        val comment = repository.findOne(id);
        if (comment == null) {
            throw new CommentNotFoundException("Comment with id '" + id + "' not found");
        }
        return comment;
    }
}

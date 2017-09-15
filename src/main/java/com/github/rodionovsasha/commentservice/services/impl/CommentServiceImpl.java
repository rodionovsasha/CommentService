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
@Service @Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;

    @Override
    public Comment add(final Comment comment) {
        return repository.saveAndFlush(comment);
    }

    @Override
    public Comment update(final Comment comment) throws CommentNotFoundException {
        val currentComment = getById(comment.getId());
        currentComment.setContent(comment.getContent());
        currentComment.setUser(comment.getUser());
        currentComment.setTopic(comment.getTopic());
        return repository.saveAndFlush(currentComment);
    }

    @Override
    public void delete(final long id) {
        repository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Comment getById(final long id) throws CommentNotFoundException {
        val comment = repository.findOne(id);
        if (comment == null) {
            throw new CommentNotFoundException(String.format("Comment with id '%d' not found", id));
        }
        return comment;
    }
}

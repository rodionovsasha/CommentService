package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.Comment;
import com.github.rodionovsasha.commentservice.exceptions.CommentNotFoundException;

public interface CommentService {
    Comment add(Comment comment);

    Comment update(Comment comment) throws CommentNotFoundException;

    void delete(long id);

    Comment getById(long id) throws CommentNotFoundException;
}

package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.Comment;
import com.github.rodionovsasha.commentservice.exceptions.CommentNotFoundException;

public interface CommentService {
    Comment addComment(Comment comment);
    Comment updateComment(Comment comment) throws CommentNotFoundException;
    void deleteComment(long id);
    Comment getCommentById(long id) throws CommentNotFoundException;
}

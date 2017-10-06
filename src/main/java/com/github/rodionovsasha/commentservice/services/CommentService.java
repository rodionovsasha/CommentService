package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.Comment;

import java.util.List;

public interface CommentService {
    Comment add(String content, long topicId, long userId);

    void update(long commentId, long userId, String content);

    void archive(long commentId, long userId);

    List<Comment> findByTopic(long topicId);
}

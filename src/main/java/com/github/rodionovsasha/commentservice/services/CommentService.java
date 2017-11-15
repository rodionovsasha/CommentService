package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.Comment;

import java.util.List;

public interface CommentService {
    Comment add(String content, int topicId, int userId);

    void update(int commentId, int userId, String content);

    void archive(int commentId, int userId);

    List<Comment> findByTopic(int topicId);
}

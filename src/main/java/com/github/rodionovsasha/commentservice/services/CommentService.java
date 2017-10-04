package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.Comment;

public interface CommentService {
    Comment add(String content, long topicId, long userId);
}

package com.github.rodionovsasha.commentservice.repositories;

import com.github.rodionovsasha.commentservice.entities.Comment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends BaseRepository<Comment> {
    List<Comment> findByTopicIdAndArchivedFalseOrderByDateAsc(int topicId);
}

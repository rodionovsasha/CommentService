package com.github.rodionovsasha.commentservice.services;

import com.github.rodionovsasha.commentservice.entities.Topic;
import com.github.rodionovsasha.commentservice.exceptions.InactiveUserException;
import com.github.rodionovsasha.commentservice.exceptions.PermissionException;
import com.github.rodionovsasha.commentservice.exceptions.TopicNotFoundException;
import com.github.rodionovsasha.commentservice.exceptions.UserNotFoundException;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface TopicService {
    /**
     * Creates a new topic unless the user is inactive
     *
     * @throws UserNotFoundException when user was not found by id
     * @throws InactiveUserException when user is found, but it is not active
     */
    Topic start(String title, long userId) throws UserNotFoundException, InactiveUserException;

    /**
     * Deletes a topic. The deletion of already deleted topic does not do anything.
     *
     * @throws TopicNotFoundException when topic was not found by id
     * @throws UserNotFoundException  when user was not found by id
     * @throws InactiveUserException  when user is found, but it is not active
     * @throws PermissionException    when a non-topic starter is trying to delete the topic
     */
    void delete(long topicId, long userId) throws Exception;

    /**
     * Lists topics which belong to a user
     * Each topic entity does not get comments from DB nor serialize them to JSON
     * Getting topics of inactive users is allowed
     * Only not deleted topic are returned
     *
     * @throws UserNotFoundException when user was not found by id
     */
    List<Topic> listForUser(long userId, Sort sort) throws UserNotFoundException;

    /**
     * Searched for non-deleted topics if title includes a fragment(case insensitive)
     * Results are limited by `limit`
     * Each topic entity does not get comments from DB nor serialize them to JSON
     */
    List<Topic> search(String titleFragment, int limit);

    Topic getById(long id) throws TopicNotFoundException;
}

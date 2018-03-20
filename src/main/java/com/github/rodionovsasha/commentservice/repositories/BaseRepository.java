package com.github.rodionovsasha.commentservice.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
interface BaseRepository<T> extends CrudRepository<T, Integer> {
}

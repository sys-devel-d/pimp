package com.pimp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pimp.domain.Calendar;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by julianfink on 04/12/16.
 */
public interface CalendarRepository extends MongoRepository<Calendar, String> {

  @Query("{subscribers:{$elemMatch:{$eq: ?0}}}")
  public List<Calendar> findBySubscriber(String subscriber);
}

package com.pimp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.pimp.domain.Calendar;

/**
 * Created by julianfink on 04/12/16.
 */
public interface CalendarRepository extends MongoRepository<Calendar, String> {

  @Query("{subscribers:{$elemMatch:{$eq: ?0}}}")
  public List<Calendar> findBySubscriber(String subscriber);

  @Query("{$and: [{isPrivate: true}, {owner: {$eq: ?0}}]}")
  public List<Calendar> findPrivateCalendarByOwner(String owner);
}

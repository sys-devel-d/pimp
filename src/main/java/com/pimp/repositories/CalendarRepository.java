package com.pimp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.pimp.domain.Calendar;

/**
 * Created by julianfink on 04/12/16.
 */
public interface CalendarRepository extends MongoRepository<Calendar, String> {
}

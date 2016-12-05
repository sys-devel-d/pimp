package com.pimp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import com.pimp.commons.exceptions.EntityValidationException;
import com.pimp.domain.Calendar;
import com.pimp.repositories.CalendarRepository;

/**
 * Created by julianfink on 04/12/16.
 */
@Service
public class CalendarService {

  private CalendarRepository calendarRepository;
  @Autowired
  private MongoOperations mongoOperations;

  @Autowired
  public CalendarService(CalendarRepository calendarRepository) {
    this.calendarRepository = calendarRepository;
  }

  public Calendar createCalendar(Calendar calendar) throws EntityValidationException {
    String title = calendar.getTitle();
    if (title.isEmpty()) {
      throw new EntityValidationException("The title of the calendar should not be empty");
    }
    return calendarRepository.save(calendar);
  }

  public List<Calendar> getCalendarsByUser(String username) {
    return calendarRepository
      .findAll()
      .stream()
      .filter(calendar -> calendar.getSubscribers().contains(username))
      .collect(Collectors.toList());
  }

  public Calendar save(Calendar calendar) {
    return calendarRepository.save(calendar);
  }

  public Calendar getCalendarByKey(String key) {
    return calendarRepository.findOne(key);
  }

}

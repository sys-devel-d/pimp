package com.pimp.services;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.pimp.commons.exceptions.EntityValidationException;
import com.pimp.domain.Calendar;
import com.pimp.domain.Event;
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
    return calendarRepository.findBySubscriber(username);
  }

  public List<Calendar> query(String query, List<String> queryParameter) {
    Query mongoQuery = new Query();
    Criteria criteria = new Criteria();
    Criteria[] criterias = queryParameter.stream()
      .map(param -> Criteria.where(param).regex(Pattern.compile(query, Pattern.CASE_INSENSITIVE)))
      .toArray(Criteria[]::new);
    criteria.orOperator(criterias);
    mongoQuery.addCriteria(criteria);
    return mongoOperations.find(mongoQuery, Calendar.class)
      .stream().collect(Collectors.toList());
  }

  public Calendar save(Calendar calendar) {
    return calendarRepository.save(calendar);
  }

  public Calendar getCalendarByKey(String key) {
    return calendarRepository.findOne(key);
  }

  public void replaceEvent(Event event) {
    Calendar calendar = getCalendarByKey(event.getCalendarKey());

    List<Event> events = calendar.getEvents()
        .stream()
        .map(aEvent -> aEvent.getKey().equals(event.getKey()) ? event : aEvent)
        .collect(Collectors.toList());
    calendar.setEvents(events);
    save(calendar);
  }

}

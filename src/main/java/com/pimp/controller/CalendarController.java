package com.pimp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.commons.exceptions.EntityValidationException;
import com.pimp.commons.exceptions.ForbiddenException;
import com.pimp.domain.Calendar;
import com.pimp.domain.Event;
import com.pimp.services.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

/**
 * Created by julianfink on 04/12/16.
 */
@RestController
@RequestMapping("/calendar")
@PreAuthorize("#oauth2.hasScope('user_actions')")
public class CalendarController {

  private CalendarService calendarService;

  @Autowired
  public CalendarController(CalendarService calendarService) {
    this.calendarService = calendarService;
  }

  @RequestMapping(method = POST)
  public Calendar createCalendar(@Valid @RequestBody Calendar calendar,
      Principal principal) throws EntityValidationException {
    if (calendar.getSubscribers().isEmpty()) {
      calendar.getSubscribers().add(principal.getName());
    }
    if(calendar.getOwner() == null) {
      calendar.setOwner(principal.getName());
    }
    return calendarService.createCalendar(calendar);
  }

  @RequestMapping(method = GET)
  public List<Calendar> getSubscribedCalendars(Principal principal) {
    return calendarService.getCalendarsByUser(principal.getName());
  }

  @RequestMapping(method = POST, path = "/{calendarKey}")
  public Event addEvent(@Valid @RequestBody Event event, @PathVariable String calendarKey,
      Principal principal) {
    Calendar calendar = calendarService.getCalendarByKey(calendarKey);
    if (calendar == null) {
      throw new EntityNotFoundException("A calendar with the key " + calendarKey + " does not exist");
    } else if (!calendar.getSubscribers().contains(principal.getName())) {
      throw new ForbiddenException("You can't add an event to a unsubscribed calendar");
    } else {
      if (event.getCalendarKey() == null) {
        event.setCalendarKey(calendar.getKey());
      }
      calendar.getEvents().add(event);
      calendarService.save(calendar);
      return event;
    }
  }

  @RequestMapping(method = PUT, path = "/event/{eventKey}")
  public void editEvent(@Valid @RequestBody Event event, Principal principal) {
    Calendar calendar = calendarService.getCalendarByKey(event.getCalendarKey());

    if (calendar == null) {
      throw new EntityNotFoundException("An event with the key " + event.getKey() +
          " does not exist");
    } else if (!calendar.getSubscribers().contains(principal.getName())) {
      throw new ForbiddenException("You can't edit an event of a unsubscribed calendar");
    } else {
      List<Event> events = calendar.getEvents()
        .stream()
        .map(aEvent -> aEvent.getKey().equals(event.getKey()) ? event : aEvent)
        .collect(Collectors.toList());
      calendar.setEvents(events);
      calendarService.save(calendar);
    }
  }

  @RequestMapping(method = DELETE, path = "/event/{eventKey}")
  public void deleteEvent(@Valid @RequestBody Event event, Principal principal) {
    Calendar calendar = calendarService.getCalendarByKey(event.getCalendarKey());

    if (calendar == null) {
      throw new EntityNotFoundException("An event with the key " + event.getKey() +
          " does not exist");
    } else if (!calendar.getSubscribers().contains(principal.getName())) {
      throw new ForbiddenException("You can't delete an event of a unsubscribed calendar");
    } else {
      List<Event> events = calendar.getEvents()
        .stream()
        .filter(aEvent -> !aEvent.getKey().equals(event.getKey()))
        .collect(Collectors.toList());
      calendar.setEvents(events);
      calendarService.save(calendar);
    }
  }

  @RequestMapping(method = GET, path = "/search/{query}")
  public List<Calendar> searchCalendar(@PathVariable String query, Principal principal) {
    if (query.length() < 3) {
      throw new IllegalArgumentException("Search string should have a length >= 3.");
    }
    List<Calendar> cals = calendarService.query(query, Arrays.asList("title", "subscribers"));
    return cals
      .stream()
      //filter out already subscribed and private
      .filter(cal -> !cal.isPrivate() || cal.getOwner().equals(principal.getName()))
      .filter(cal -> calendarService.getCalendarsByUser(principal.getName())
        .stream()
        .noneMatch(calendar -> calendar.getKey().equals(cal.getKey())))
      .collect(Collectors.toList());
  }

  @RequestMapping(method = PATCH, path = "/subscribe/{key}")
  public List<Calendar> subscribe(@PathVariable String key, Principal principal) {
    Calendar calendar = calendarService.getCalendarByKey(key);
    List<String> subscribers = calendar.getSubscribers();
    if (subscribers.contains(principal.getName())) {
      throw new EntityAlreadyExistsException(principal.getName() + " has already subscribed.");
    }
    subscribers.add(principal.getName());
    calendar.setSubscribers(subscribers);
    calendarService.save(calendar);
    return calendarService.getCalendarsByUser(principal.getName());
  }

  @RequestMapping(method = PATCH, path = "/unsubscribe/{key}")
  public List<Calendar> unsubscribe(@PathVariable String key, Principal principal) {
    Calendar calendar = calendarService.getCalendarByKey(key);
    List<String> subscribers = calendar.getSubscribers();
    if (!subscribers.contains(principal.getName())) {
      throw new EntityAlreadyExistsException(
        principal.getName() + " has no subscribed calendar with name " + calendar.getTitle());
    }
    subscribers.remove(principal.getName());
    calendar.setSubscribers(subscribers);
    calendarService.save(calendar);
    return calendarService.getCalendarsByUser(principal.getName());
  }
}


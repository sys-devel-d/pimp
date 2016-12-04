package com.pimp.controller;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.commons.exceptions.EntityValidationException;
import com.pimp.commons.exceptions.ForbiddenException;
import com.pimp.domain.Calendar;
import com.pimp.domain.Event;
import com.pimp.services.CalendarService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by julianfink on 04/12/16.
 */
@RestController
@RequestMapping("/calendar")
public class CalendarController {

  private CalendarService calendarService;

  @Autowired
  public CalendarController(CalendarService calendarService) {
    this.calendarService = calendarService;
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = POST)
  public void createCalendar(@Valid @RequestBody Calendar calendar,
      Principal principal) throws EntityValidationException {
    if (calendar.getSubscribers().isEmpty()) {
      calendar.getSubscribers().add(principal.getName());
    }
    calendarService.createCalendar(calendar);
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = GET)
  public List<Calendar> getSubscribedCalendars(Principal principal) {
    return calendarService.getCalendarsByUser(principal.getName());
  }

  @PreAuthorize("#oauth2.hasScope('user_actions')")
  @RequestMapping(method = POST, path = "/{calendarKey}")
  public void addEvent(@Valid @RequestBody Event event, @PathVariable String calendarKey,
      Principal principal) {
    Calendar calendar = calendarService.getCalendarByKey(calendarKey);
    if (calendar == null) {
      throw new EntityNotFoundException("A calendar with the key " + calendarKey + " does not exist");
    } else if (!calendar.getSubscribers().contains(principal.getName())) {
      throw new ForbiddenException("You can't add an event to a unsubscribed calendar");
    } else {
      calendar.getEvents().add(event);
      calendarService.save(calendar);
    }
  }

}

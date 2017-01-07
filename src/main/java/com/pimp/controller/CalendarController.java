package com.pimp.controller;

<<<<<<< HEAD
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.Arrays;
=======
import java.security.Principal;
>>>>>>> f6b9697... Refactors notification services.
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

<<<<<<< HEAD
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.commons.exceptions.EntityValidationException;
import com.pimp.commons.exceptions.ForbiddenException;
import com.pimp.domain.Calendar;
import com.pimp.domain.Event;
import com.pimp.services.CalendarService;
=======
>>>>>>> f6b9697... Refactors notification services.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

<<<<<<< HEAD
import com.pimp.commons.exceptions.EntityAlreadyExistsException;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
=======
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.commons.exceptions.EntityValidationException;
import com.pimp.commons.exceptions.ForbiddenException;
import com.pimp.domain.Calendar;
import com.pimp.domain.Event;
import com.pimp.domain.InvitationResponse;
import com.pimp.services.CalendarService;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
>>>>>>> f6b9697... Refactors notification services.
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
    }
    if (!calendar.getOwner().equals(principal.getName())) {
      throw new ForbiddenException("You are not allowed to add events to this calendar");
    }
    if (!calendar.getSubscribers().contains(principal.getName())) {
      throw new ForbiddenException("You can't add an event to a unsubscribed calendar");
    }
    if (event.getCalendarKey() == null) {
      event.setCalendarKey(calendar.getKey());
    }
    calendar.getEvents().add(event);
    calendarService.save(calendar);
    return event;
  }

  @RequestMapping(method = PUT, path = "/event/{eventKey}")
  public void editEvent(@Valid @RequestBody Event event, Principal principal) {
    Calendar calendar = calendarService.getCalendarByKey(event.getCalendarKey());
    if (calendar == null) {
      throw new EntityNotFoundException("An event with the key " + event.getKey() +
          " does not exist");
    }
    if (!calendar.getOwner().equals(principal.getName())) {
      throw new ForbiddenException("You are not allowed to edit this event");
    }
    if (!calendar.getSubscribers().contains(principal.getName())) {
      throw new ForbiddenException("You can't edit an event of a unsubscribed calendar");
<<<<<<< HEAD
=======
    } else {
      calendarService.replaceEvent(event);
>>>>>>> f6b9697... Refactors notification services.
    }
    List<Event> events = calendar.getEvents()
      .stream()
      .map(aEvent -> aEvent.getKey().equals(event.getKey()) ? event : aEvent)
      .collect(Collectors.toList());
    calendar.setEvents(events);
    calendarService.save(calendar);
  }

  @RequestMapping(method = DELETE, path = "/event/{eventKey}")
  public void deleteEvent(@Valid @RequestBody Event event, Principal principal) {
    Calendar calendar = calendarService.getCalendarByKey(event.getCalendarKey());
    if (calendar == null) {
      throw new EntityNotFoundException("An event with the key " + event.getKey() +
          " does not exist");
    }
    if (!calendar.getOwner().equals(principal.getName())) {
      throw new ForbiddenException("Your are not allowed to delete this event");
    }
    if (!calendar.getSubscribers().contains(principal.getName())) {
      throw new ForbiddenException("You can't delete an event of a unsubscribed calendar");
    }
<<<<<<< HEAD
    List<Event> events = calendar.getEvents()
      .stream()
      .filter(aEvent -> !aEvent.getKey().equals(event.getKey()))
      .collect(Collectors.toList());
    calendar.setEvents(events);
    calendarService.save(calendar);
  }

  @RequestMapping(method = GET, path = "/search/{query}")
  public List<Calendar> searchCalendar(@PathVariable String query, Principal principal) {
    if (query.length() < 3) {
      throw new IllegalArgumentException("Search string should have a length >= 3.");
    }
    List<Calendar> cals = calendarService.query(query, Arrays.asList("title", "subscribers"));
    if(cals != null && !cals.isEmpty()) {
      List<String> subscribedTo = calendarService.getCalendarsByUser(principal.getName())
        .stream().map(Calendar::getKey).collect(Collectors.toList());
      return cals.stream().filter(cal ->
        !cal.getOwner().equals(principal.getName()) && !subscribedTo.contains(cal.getKey())
      ).collect(Collectors.toList());
    }
    return cals;
  }

  @RequestMapping(method = PATCH, path = "/subscribe/{key}")
  public Calendar subscribe(@PathVariable String key, Principal principal) {
    Calendar calendar = calendarService.getCalendarByKey(key);
    List<String> subscribers = calendar.getSubscribers();
    if (subscribers.contains(principal.getName())) {
      throw new EntityAlreadyExistsException(principal.getName() + " is already subscribed.");
    }
    subscribers.add(principal.getName());
    calendar.setSubscribers(subscribers);
    return calendarService.save(calendar);
  }

  @RequestMapping(method = PATCH, path = "/unsubscribe/{key}")
  public void unsubscribe(@PathVariable String key, Principal principal) {
    Calendar calendar = calendarService.getCalendarByKey(key);
    List<String> subscribers = calendar.getSubscribers();
    if (!subscribers.contains(principal.getName())) {
      throw new EntityAlreadyExistsException(
        principal.getName() + " has no subscribed calendar with name " + calendar.getTitle());
    }
    if(calendar.getOwner().equals(principal.getName())) {
      throw new ForbiddenException("You cannot unsubscribe from your own calendars");
    }
    subscribers.remove(principal.getName());
    calendar.setSubscribers(subscribers);
    calendarService.save(calendar);
=======


  @RequestMapping(method = POST, path = "/invitation")
  public void acceptOrDeclineInvitation(@Valid @RequestBody InvitationResponse response,
    Principal principal) {



    Calendar calendar = calendarService.getCalendarByKey(response.getCalendarKey());

    if (calendar == null) {
      throw new EntityNotFoundException("An event with the key " + response.getEventKey() +
        "does not exist");
    }
    // since we do not have the separation between invited, declined and accepted user for a event,
    // we can't make the transitition invited => declined
    // for now, we could only delete the user in the participant list in case of a decline
    if (response.getState().equals(InvitationResponse.DECLINED)) {
      Event newEvent =
        calendar.getEvents()
          .stream()
          .filter(event -> event.getKey().equals(response.getEventKey()))
          .findFirst()
          .get();
      newEvent.getParticipants().remove(principal.getName());
      calendarService.replaceEvent(newEvent);
    }

>>>>>>> f6b9697... Refactors notification services.
  }
}

}


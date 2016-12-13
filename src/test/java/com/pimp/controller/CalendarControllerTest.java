package com.pimp.controller;

import com.pimp.domain.Calendar;
import com.pimp.domain.Event;
import com.pimp.services.CalendarService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CalendarControllerTest {

    private MockMvc server;
    private Principal principal;

    @Mock
    CalendarService calendarService;

    @Before
    public void setUp() throws Exception {
        server = MockMvcBuilders
                .standaloneSetup(new CalendarController(calendarService))
                .setControllerAdvice(new RestAdvice())
                .build();
        principal = () -> "foo";
    }
    
    @Test
    public void testGetSubscribedCalendars() throws Exception {

        String expectedResponse = "[{\"title\": \"DevOps\", \"owner\": \"foo\", \"isPrivate\": true, " +
                "\"events\": [{\"title\": \"Meeting\", \"place\": \"Meeting Room\", \"isPrivate\": true, " +
                "\"description\": null, \"allDay\": false, \"creator\": \"foo\"}]}]";

        Event evt = new Event();
        Calendar cal = new Calendar();
        evt.setPrivate(true).setPlace("Meeting Room").setCreator("foo").setTitle("Meeting");
        cal.setTitle("DevOps").setPrivate(true).setOwner("foo").setEvents(Arrays.asList(evt));

        Mockito.when(calendarService.getCalendarsByUser(any())).thenReturn(Arrays.asList(cal));

        server.perform(get("/calendar/").principal(principal))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }
}

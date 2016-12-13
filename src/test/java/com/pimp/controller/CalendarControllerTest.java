package com.pimp.controller;

import com.pimp.domain.Calendar;
import com.pimp.domain.Event;
import com.pimp.services.CalendarService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class CalendarControllerTest {

    private MockMvc server;

    @Mock
    CalendarService calendarService;

    @Before
    public void setUp() throws Exception {
        server = MockMvcBuilders
                .standaloneSetup(new CalendarController(calendarService))
                .setControllerAdvice(new RestAdvice())
                .build();
    }

    // TODO: inspect the exception thrown and make this work
    //@Ignore
    @Test
    public void testGetSubscribedCalendars() throws Exception {
        Event evt = new Event();
        Calendar cal = new Calendar();
        cal.setEvents(Arrays.asList(evt));
        Mockito.when(calendarService.getCalendarsByUser(any())).thenReturn(Arrays.asList(cal));

        server.perform(get("/calendar/"))
                .andExpect(status().isOk());
    }
}

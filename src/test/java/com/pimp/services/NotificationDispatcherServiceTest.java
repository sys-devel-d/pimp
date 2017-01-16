package com.pimp.services;

import com.pimp.repositories.NotificationChannelRepo;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

// Ignoring this for now. Remove this later when there are tests.
@Ignore
@RunWith(MockitoJUnitRunner.class)
public class NotificationDispatcherServiceTest {

    private NotificationDispatcherService service;

    @Mock
    private NotificationChannelRepo repo;

    @Before
    public void setUp() throws Exception {
        service = new NotificationDispatcherService(repo);
    }

}

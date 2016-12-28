package com.pimp.services;

import com.pimp.domain.Notification;
import com.pimp.domain.NotificationChannelDocument;
import com.pimp.domain.NotificationType;
import com.pimp.repositories.NotificationChannelRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NotificationDispatcherServiceTest {

    private NotificationDispatcherService service;

    @Mock
    private NotificationChannelRepo repo;

    @Before
    public void setUp() throws Exception {
        service = new NotificationDispatcherService(repo);
    }

    @Test
    public void testAcked() throws Exception {
        NotificationChannelDocument document = new NotificationChannelDocument();
        Notification notification = new Notification();
        notification.setAcknowledged(false);
        notification.setKey("foo");
        notification.setType(NotificationType.NEW_MESSAGE);

        document.setMessages(Arrays.asList(notification));

        when(repo.findByRoomName(any())).thenReturn(document);

        ArgumentCaptor<NotificationChannelDocument> captor = ArgumentCaptor.forClass(NotificationChannelDocument.class);
        when(repo.save(captor.capture())).thenReturn(document);

        service.process(notification.setAcknowledged(true));

        Notification assertion = (Notification) captor.getValue().getMessages().get(0);
        assertThat(assertion.isAcknowledged()).isTrue();
    }

    @Test
    public void testUnackedIsSaved() throws Exception {
        NotificationChannelDocument document = new NotificationChannelDocument();
        document.setRoomName("bar");
        document.setMessages(new LinkedList<>());

        when(repo.findByRoomName("bar")).thenReturn(document);

        Notification notification = new Notification();
        notification.setAcknowledged(false);
        notification.setKey("foo");
        notification.setType(NotificationType.NEW_MESSAGE);
        notification.setRoomId("bar");

        ArgumentCaptor<NotificationChannelDocument> captor = ArgumentCaptor.forClass(NotificationChannelDocument.class);
        when(repo.save(captor.capture())).thenReturn(document);

        service.process(notification);

        assertThat(captor.getValue().getMessages()).hasSize(1);
    }
}
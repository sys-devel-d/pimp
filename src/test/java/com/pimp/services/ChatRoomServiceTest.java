package com.pimp.services;

import com.pimp.commons.exceptions.EntityAlreadyExistsException;
import com.pimp.commons.exceptions.EntityNotFoundException;
import com.pimp.domain.ChatRoom;
import com.pimp.domain.ChatRoomDocument;
import com.pimp.domain.Message;
import com.pimp.domain.User;
import com.pimp.repositories.ChatRoomRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.Instant;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;
    private ChatRoomService chatRoomService;

    @Before
    public void setUp() throws Exception {
        this.chatRoomService = new ChatRoomService(chatRoomRepository);
    }

    @Test
    public void testCreateChatRoom() throws Exception {
        String roomId = "general";
        User user1 = new User().setUserName("foo").setEmail("foo@bar.de").setPassword("blablabla");
        User user2 = new User().setUserName("foos").setEmail("foos@bar.de").setPassword("blablablas");
        Message message = new Message("Hi foos", roomId, user1.getUserName(), Instant.now());
        ChatRoom chatRoom = new ChatRoom()
                .setRoomName("general")
                .setParticipants(Arrays.asList(user1, user2))
                .setMessages(Arrays.asList(message));

        ChatRoomDocument chatRoomDocument = chatRoomService.createChatRoom(chatRoom);

        assertThat(chatRoomDocument.getMessages()).isEqualTo(Arrays.asList(message));
        assertThat(chatRoomDocument.getParticipants()).isEqualTo(Arrays.asList(user1, user2));
        assertThat(chatRoomDocument.getRoomName()).isEqualTo(roomId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testFindNotExistingChatRoom() throws Exception {
        when(chatRoomRepository.findByRoomName(any())).thenReturn(null);

        chatRoomService.findByRoomName("the_police");
    }

    @Test(expected = EntityAlreadyExistsException.class)
    public void testCreateAlreadyExistingChatRoom() throws Exception {
        when(chatRoomRepository.findByRoomName(any())).thenReturn(new ChatRoomDocument());

        ChatRoom chatRoom = new ChatRoom().setRoomName("foobart");
        chatRoomService.createChatRoom(chatRoom);
    }
}

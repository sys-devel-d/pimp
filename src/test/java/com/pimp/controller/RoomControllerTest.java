package com.pimp.controller;


import com.pimp.domain.ChatRoom;
import com.pimp.domain.Message;
import com.pimp.domain.User;
import com.pimp.services.ChatRoomService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(MockitoJUnitRunner.class)
public class RoomControllerTest {

    private MockMvc server;

    @Mock
    private ChatRoomService chatRoomService;

    @Before
    public void setUp() throws Exception {
        server = MockMvcBuilders
                .standaloneSetup(new RoomController(chatRoomService))
                .setControllerAdvice(new RestAdvice())
                .build();
    }

    @Test
    public void testGetUserRoomsIfHasNoRooms() throws Exception {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getPrincipal()).thenReturn(null);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(chatRoomService.findUsersRooms(any())).thenReturn(new ArrayList<>());

        server.perform(get("/rooms/"))
                .andExpect(content().json("[]"));
    }

    @Test
    public void testGetUserRooms() throws Exception {
        User user1 = new User().setUserName("foo");
        User user2 = new User().setUserName("bar");
        String expectedResponse = "[{\"roomName\": \"general\", \"roomType\": \"PRIVATE\", " +
                "\"participants\": [{\"userName\": \"foo\"}, {\"userName\": \"bar\"}], " +
                "\"messages\": [{\"message\": \"Hi bar\", \"userName\": \"foo\", \"roomId\": \"general\"}]}]";

        Message message = new Message()
                .setMessage("Hi bar")
                .setUserName("foo")
                .setRoomId("general");

        ChatRoom chatRoom = new ChatRoom()
                .setRoomName("general")
                .setRoomType(ChatRoom.ROOM_TYPE_PRIVATE)
                .setParticipants(Arrays.asList(user1, user2))
                .setMessages(Arrays.asList(message));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(authentication.getPrincipal()).thenReturn(new User().setUserName("bolz"));
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(chatRoomService.findUsersRooms(any())).thenReturn(Arrays.asList(chatRoom));

        server.perform(get("/rooms/"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(expectedResponse));

    }

}

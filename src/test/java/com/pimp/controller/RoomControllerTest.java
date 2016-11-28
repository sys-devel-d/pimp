package com.pimp.controller;


import com.pimp.domain.ChatRoom;
import com.pimp.domain.User;
import com.pimp.services.ChatRoomService;
import com.pimp.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class RoomControllerTest {

    private MockMvc server;

    @Mock
    private UserService userService;
    @Mock
    private ChatRoomService chatRoomService;

    @Before
    public void setUp() throws Exception {
        server = MockMvcBuilders
                .standaloneSetup(new RoomController(userService, chatRoomService))
                .setControllerAdvice(new RestAdvice())
                .build();
    }

    /*
    Hm userService is not mocked. It seems to be null. TODO: fix tests
    userService is not mocked??
     */
    /*
    @Test
    public void testGetUserRoomsIfHasNoRooms() throws Exception {
        User user = new User().setUserName("noRoomsGuy");
        when(userService.findByUserName("noRoomsGuy")).thenReturn(user);
        when(chatRoomService.findUsersRooms(user)).thenReturn(null);

        server.perform(get("/rooms/noRoomsGuy/rooms"))
                .andExpect(content().json("[]"));
    }

    @Test
    public void testGetUserRooms() throws Exception {
        User user = new User().setUserName("icke");
        when(userService.findByUserName("icke")).thenReturn(user);
        when(chatRoomService.findUsersRooms(user)).thenReturn(
                Arrays.asList(
                        new ChatRoom().setRoomName("general"),
                        new ChatRoom().setRoomName("lobby")
                )
        );
        server.perform(get("/rooms/icke/rooms"))
                .andExpect(content().json("[\"general\", \"lobby\"]"));

    }
    */

}

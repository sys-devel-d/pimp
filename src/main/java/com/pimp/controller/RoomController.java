package com.pimp.controller;

import com.pimp.domain.ChatRoom;
import com.pimp.domain.User;
import com.pimp.services.ChatRoomService;
import com.pimp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private UserService userService;
    private ChatRoomService chatRoomService;

    @Autowired
    public RoomController(UserService userService, ChatRoomService chatRoomService) {
        this.userService = userService;
        this.chatRoomService = chatRoomService;
    }

    @PreAuthorize("#oauth2.hasScope('user_actions')")
    @RequestMapping(method = POST, path="/init-private")
    public ResponseEntity<ChatRoom> initPrivateChatRoom(@RequestBody User invited) throws NoSuchAlgorithmException {

        User invitee = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(invited != null && !invitee.equals(invited)) {
            ChatRoom chatRoom = chatRoomService
                    .initUniqueRoom(Arrays.asList(invitee, invited), ChatRoom.ROOM_TYPE_PRIVATE);
            if(chatRoom != null) {
                return ResponseEntity.ok(chatRoom);
            }
            // chat room has already been initialized
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        // Bad parameters
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PreAuthorize("#oauth2.hasScope('user_actions')")
    @RequestMapping(method = GET, path = "/{userName}/rooms")
    public List<String> getUsersRooms(@PathVariable String userName) {
        User user = userService.findByUserName(userName);
        // Maybe return a list of ChatRooms instead of sending only the room names
        List<String> rooms = chatRoomService.findUsersRooms(user)
                .stream().map(ChatRoom::getRoomName).collect(Collectors.toList());
        if(rooms == null) return new ArrayList<>();
        return rooms;
    }

}

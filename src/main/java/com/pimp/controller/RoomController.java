package com.pimp.controller;

import com.pimp.domain.ChatRoom;
import com.pimp.domain.User;
import com.pimp.services.ChatRoomService;
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

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private ChatRoomService chatRoomService;

    @Autowired
    public RoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @PreAuthorize("#oauth2.hasScope('user_actions')")
    @RequestMapping(method = POST, path="/init-private")
    public ResponseEntity<ChatRoom> initPrivateChatRoom(@RequestBody User invited) throws NoSuchAlgorithmException {

        User invitee = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(invited != null && !invitee.equals(invited)) {
            HashMap<String, String> displayNames = new HashMap<>();
            displayNames.put(invited.getUserName(), invitee.getUserName());
            displayNames.put(invitee.getUserName(), invited.getUserName());
            ChatRoom chatRoom = chatRoomService
                    .initializeRoom(Arrays.asList(invitee, invited), ChatRoom.ROOM_TYPE_PRIVATE, displayNames);
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
    @RequestMapping(method = POST, path="/init-group/{displayName}")
    public ResponseEntity<ChatRoom> initGroupChatRoom(@RequestBody List<User> users, @PathVariable String displayName)
            throws NoSuchAlgorithmException {
        User founder = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(users != null) {
            if(!users.contains(founder)) {
                users.add(founder);
            }
            HashMap<String, String> displayNames = new HashMap<>();
            displayNames.put(ChatRoom.HASH_KEY_GROUP_DISPLAY_NAME, displayName);
            ChatRoom chatRoom = chatRoomService
                    .initializeRoom(users, ChatRoom.ROOM_TYPE_GROUP, displayNames);
            if(chatRoom != null) {
                return ResponseEntity.ok(chatRoom);
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    @PreAuthorize("#oauth2.hasScope('user_actions')")
    @RequestMapping(method = GET, path = "/")
    public List<ChatRoom> getUsersRooms() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ChatRoom> rooms = chatRoomService.findUsersRooms(user);
        if(rooms == null) return new ArrayList<>();
        return rooms;
    }

}

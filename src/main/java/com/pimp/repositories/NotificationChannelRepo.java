package com.pimp.repositories;

import com.pimp.domain.NotificationChannelDocument;

public interface NotificationChannelRepo extends ChatRoomRepository {
    NotificationChannelDocument save(NotificationChannelDocument notificationChannelDocument);
    NotificationChannelDocument findByRoomName(String roomName);
}

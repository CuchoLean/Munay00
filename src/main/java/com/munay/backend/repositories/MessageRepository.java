package com.munay.backend.repositories;

import com.munay.backend.models.Message;
import com.munay.backend.models.Usuario;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySenderNameAndReceiverName(String user1, String user2);
}

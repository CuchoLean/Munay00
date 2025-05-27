package com.munay.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {
    private String senderName;
    private  String receiverName;
    private String message;
    private String date;
}

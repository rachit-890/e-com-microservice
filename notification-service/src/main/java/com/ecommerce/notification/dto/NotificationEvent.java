package com.ecommerce.notification.dto;

import lombok.Data;

@Data
public class NotificationEvent {

    private String email;
    private String message;
    private String subject;
    private String type;
    private String mobile;
}

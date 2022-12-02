package com.aip.commerce_e.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notifications {

    private String title;
    private String content;
    private String app;
    private String type;

    public Notifications(String title, String content, String app, String type) {
        this.title = title;
        this.content = content;
        this.app = app;
        this.type = type;
    }
}

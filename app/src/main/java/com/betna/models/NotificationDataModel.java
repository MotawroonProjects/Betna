package com.betna.models;

import java.io.Serializable;
import java.util.List;

public class NotificationDataModel extends StatusResponse implements Serializable  {

    private List<NotificationModel> data;

    public List<NotificationModel> getData() {
        return data;
    }

    public class NotificationModel implements Serializable
    {
       private String id;
       private String message;
       private String title;
       private int user_id;
       private int rev_id;
       private String created_at;
       private String updated_at;

        public String getId() {
            return id;
        }

        public String getMessage() {
            return message;
        }

        public String getTitle() {
            return title;
        }

        public int getUser_id() {
            return user_id;
        }

        public int getRev_id() {
            return rev_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}

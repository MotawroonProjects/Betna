package com.betna.models;

import java.io.Serializable;

public class UserModel extends StatusResponse{
    private User data;

    public User getUser() {
        return data;
    }



    public static class User implements Serializable {
        private int id;
        private String first_name;
        private String last_name;
        private String photo;
        private String phone;
        private String is_active;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getPhoto() {
            return photo;
        }

        public String getPhone() {
            return phone;
        }

        public String getIs_active() {
            return is_active;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }


}

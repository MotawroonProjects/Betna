package com.betna.models;

import java.io.Serializable;

public class SettingDataModel extends StatusResponse implements Serializable {
    private Settings data;

    public Settings getData() {
        return data;
    }

    public static class Settings implements Serializable{
      private String about_us;

        public String getAbout_us() {
            return about_us;
        }
    }
}

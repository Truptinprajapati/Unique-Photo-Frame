package com.vaktech.uniqueartphotoframe.Response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by vaksys-android-52 on 3/8/17.
 */

public class AppOnlineDialogResponse extends CommonResponse {

    @SerializedName("row")
    private List<AppList> row;

    public List<AppList> getRow() {
        return row;
    }

    public class AppList {
        @SerializedName("name")
        private String name;

        @SerializedName("link")
        private String link;

        @SerializedName("image")
        private String image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}

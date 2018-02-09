package com.ge.grahamelliott.sharkfeed.common.network;

import com.ge.grahamelliott.sharkfeed.common.models.Photo;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author graham.elliott
 */
public class SearchPhotosResponse {

    private PageDetails photos;

    @SerializedName("stat")
    private String status;

    public static class PageDetails {
        private int page;

        private int pages;

        @SerializedName("perpage")
        private int perPage;

        private String total;

        private List<Photo> photo;

        public int getPage() {
            return page;
        }

        public int getPages() {
            return pages;
        }

        public int getPerPage() {
            return perPage;
        }

        public String getTotal() {
            return total;
        }

        public List<Photo> getPhotoList() {
            return photo;
        }
    }

    public PageDetails getPhotos() {
        return photos;
    }

    public String getStatus() {
        return status;
    }
}

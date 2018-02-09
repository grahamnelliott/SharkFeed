package com.ge.grahamelliott.sharkfeed.common.models;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Model of Photo details object.
 *
 * @author graham.elliott
 */
public class PhotoDetail {

    private static final String PHOTO_PAGE_URL_TYPE = "photopage";

    private String id;

    private String secret;

    private String server;

    private String farm;

    @SerializedName("dateuploaded")
    private String dateUploaded;

    @SerializedName("isfavorite")
    private int isFavorite;

    private String license;

    @SerializedName("safety_level")
    private String safetyLevel;

    private int rotation;

    private Owner owner;

    private Content title;

    private Content description;

    private Visibility visibility;

    private String views;

    private UrlsResponse urls;

    private static class UrlsResponse {
        private List<UrlResponse> url;

        public List<UrlResponse> getUrls() {
            return url;
        }
    }

    private static class UrlResponse extends Content {

        private String type;

        public String getType() {
            return type;
        }
    }

    public static class Owner {

        private String nsid;

        private String username;

        @SerializedName("realname")
        private String realName;

        private String location;

        @SerializedName("iconserver")
        private String iconServer;

        @SerializedName("iconfarm")
        private String iconFarm;

        @SerializedName("path_alias")
        private String pathAlias;

        public String getUsername() {
            return username;
        }
    }

    private static class Content {

        @SerializedName("_content")
        private String content;

        public String getContent() {
            return content;
        }
    }

    private static class Visibility {

        @SerializedName("ispublic")
        private int isPublic;

        @SerializedName("isfriend")
        private int isFriend;

        @SerializedName("isfamily")
        private int isFamily;

        public boolean isPublic() {
            return isPublic == 1;
        }

        public boolean isFriend() {
            return isFriend == 1;
        }

        public boolean isFamily() {
            return isFamily == 1;
        }
    }

    public String getId() {
        return id;
    }

    public String getDateUploaded() {
        return dateUploaded;
    }

    public Owner getOwner() {
        return owner;
    }

    public String getTitle() {
        return title.getContent();
    }

    public String getDescription() {
        return description.getContent();
    }

    public String getViews() {
        return views;
    }

    @Nullable
    public String getPhotoUrl() {
        for (UrlResponse url : urls.getUrls()) {
            if (PHOTO_PAGE_URL_TYPE.equals(url.getType())) {
                return url.getContent();
            }
        }
        return null;
    }
}

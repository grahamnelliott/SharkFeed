package com.ge.grahamelliott.sharkfeed.common.models;

import android.support.v4.util.Pair;

import com.google.gson.annotations.SerializedName;

/**
 * Model of Photo object
 *
 * @author graham.elliott
 */
public class Photo {

    // TODO: add PhotoDetails as member variable (Consider Room)

    private String id;

    private String owner;

    private String secret;

    private String server;

    private String farm;

    private String title;

    @SerializedName("ispublic")
    private int isPublic;

    @SerializedName("isfriend")
    private int isFriend;

    @SerializedName("isfamily")
    private int isFamily;

    @SerializedName("url_t")
    private String tUrl;

    @SerializedName("url_c")
    private String cUrl;

    @SerializedName("url_l")
    private String lUrl;

    @SerializedName("url_o")
    private String oUrl;

    @SerializedName("height_t")
    private String tHeight;

    @SerializedName("height_c")
    private String cHeight;

    @SerializedName("height_l")
    private String lHeight;

    @SerializedName("height_o")
    private String oHeight;

    @SerializedName("width_t")
    private String tWidth;

    @SerializedName("width_c")
    private String cWidth;

    @SerializedName("width_l")
    private String lWidth;

    @SerializedName("width_o")
    private String oWidth;

    public String getId() {
        return id;
    }

    public String getOwner() {
        return owner;
    }

    public String getSecret() {
        return secret;
    }

    public String getServer() {
        return server;
    }

    public String getFarm() {
        return farm;
    }

    public String getTitle() {
        return title;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public int getIsFriend() {
        return isFriend;
    }

    public int getIsFamily() {
        return isFamily;
    }

    public String getSmallUrl() {
        return tUrl;
    }

    public String getMediumUrl() {
        return cUrl;
    }

    public String getLargeUrl() {
        return lUrl;
    }

    public String getOriginalUrl() {
        return oUrl;
    }

    public Pair<String, String> getTUrlSize() {
        return new Pair<>(tHeight, tWidth);
    }

    public Pair<String, String> getCUrlSize() {
        return new Pair<>(cHeight, cWidth);
    }

    public Pair<String, String> getLUrlSize() {
        return new Pair<>(lHeight, lWidth);
    }

    public Pair<String, String> getOUrlSize() {
        return new Pair<>(oHeight, oWidth);
    }
}

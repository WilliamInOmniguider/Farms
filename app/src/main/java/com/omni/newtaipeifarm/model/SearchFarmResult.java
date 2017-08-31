package com.omni.newtaipeifarm.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wiliiamwang on 06/07/2017.
 */

public class SearchFarmResult implements Serializable {

    @SerializedName("sid")
    private String sid;
    @SerializedName("store_name")
    private String name;
    @SerializedName("store_circle")
    private String circle;
    @SerializedName("s_category")
    private String catecory;
    @SerializedName("s_category_en")
    private String catecoryEN;
    @SerializedName("s_intro")
    private String intro;
    @SerializedName("c_id")
    private String cityId;
    @SerializedName("a_id")
    private String areaId;
    @SerializedName("s_address")
    private String address;
    @SerializedName("s_lat")
    private String lat;
    @SerializedName("s_lon")
    private String lng;
    @SerializedName("s_tel")
    private String tel;
    @SerializedName("s_open_time")
    private String openTime;
    @SerializedName("s_keywords")
    private String keywords;
    @SerializedName("website")
    private String webURL;
    @SerializedName("3d_url")
    private String threeDURL;
    @SerializedName("film_url")
    private String filmURL;
    @SerializedName("icon")
    private String icon;
    @SerializedName("logo")
    private String logo;
    @SerializedName("banner")
    private String banner;
    @SerializedName("distance")
    private double distance;
    @SerializedName("category_id")
    private String categoryId;
    @SerializedName("event")
    private String event;
    @SerializedName("email")
    private String email;
    @SerializedName("owner_name")
    private String ownerName;
    @SerializedName("popular")
    private String popular;
    @SerializedName("last_update")
    private String lastUpdate;
    @SerializedName("favorite")
    private String favorite;
    @SerializedName("ec_url")
    private String ecURL;

    private LatLng position;
    private Farm farm;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String storeName) {
        this.name = storeName;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String storeCircle) {
        this.circle = storeCircle;
    }

    public String getCatecory() {
        return catecory;
    }

    public void setCatecory(String catecory) {
        this.catecory = catecory;
    }

    public String getCatecoryEN() {
        return catecoryEN;
    }

    public void setCatecoryEN(String catecoryEN) {
        this.catecoryEN = catecoryEN;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getThreeDURL() {
        return threeDURL;
    }

    public void setThreeDURL(String threeDURL) {
        this.threeDURL = threeDURL;
    }

    public String getFilmURL() {
        return filmURL;
    }

    public void setFilmURL(String filmURL) {
        this.filmURL = filmURL;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }

    public String getEcURL() {
        return ecURL;
    }

    public void setEcURL(String ecURL) {
        this.ecURL = ecURL;
    }

    public Farm toFarm() {
        if (farm == null) {
            farm = new Farm.Builder().setSid(sid)
                    .setName(name)
                    .setCircle(circle)
                    .setCategory(catecory)
                    .setCategoryEN(catecoryEN)
                    .setIntro(intro)
                    .setCityId(cityId)
                    .setAreaId(areaId)
                    .setAddress(address)
                    .setLat(lat)
                    .setLng(lng)
                    .setTel(tel)
                    .setOpenTime(openTime)
                    .setKeywords(keywords)
                    .setWebURL(webURL)
                    .set3DURL(threeDURL)
                    .setFilmURL(filmURL)
                    .setIcon(icon)
                    .setLogo(logo)
                    .setBanner(banner)
                    .setCategoryId(categoryId)
                    .setEvent(event)
                    .setEmail(email)
                    .setPopular(popular)
                    .setFavorite(favorite)
                    .setLastUpdate(lastUpdate)
                    .setOwnerName(ownerName)
                    .setEcURL(ecURL)
                    .build();
        }
        return farm;
    }
}

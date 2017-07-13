package com.omni.newtaipeifarm.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by wiliiamwang on 05/07/2017.
 */

public class Farm implements Serializable {

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

    public static class Builder {

        private Farm mFarm;

        public Builder() {
            if (mFarm == null) {
                mFarm = new Farm();
            }
        }

        public Builder setSid(String id) {
            mFarm.setSid(id);
            return this;
        }

        public Builder setName(String name) {
            mFarm.setName(name);
            return this;
        }

        public Builder setCircle(String circle) {
            mFarm.setCircle(circle);
            return this;
        }

        public Builder setCategory(String category) {
            mFarm.setCatecory(category);
            return this;
        }

        public Builder setCategoryEN(String categoryEN) {
            mFarm.setCatecoryEN(categoryEN);
            return this;
        }

        public Builder setIntro(String intro) {
            mFarm.setIntro(intro);
            return this;
        }

        public Builder setAddress(String address) {
            mFarm.setAddress(address);
            return this;
        }

        public Builder setLat(String lat) {
            mFarm.setLat(lat);
            return this;
        }

        public Builder setLng(String lng) {
            mFarm.setLng(lng);
            return this;
        }

        public Builder setTel(String tel) {
            mFarm.setTel(tel);
            return this;
        }

        public Builder setOpenTime(String openTime) {
            mFarm.setOpenTime(openTime);
            return this;
        }

        public Builder setKeywords(String keywords) {
            mFarm.setKeywords(keywords);
            return this;
        }

        public Builder setWebURL(String webURL) {
            mFarm.setWebURL(webURL);
            return this;
        }

        public Builder set3DURL(String threeDURL) {
            mFarm.setThreeDURL(threeDURL);
            return this;
        }

        public Builder setFilmURL(String filmURL) {
            mFarm.setFilmURL(filmURL);
            return this;
        }

        public Builder setIcon(String icon) {
            mFarm.setIcon(icon);
            return this;
        }

        public Builder setLogo(String logo) {
            mFarm.setLogo(logo);
            return this;
        }

        public Builder setBanner(String banner) {
            mFarm.setBanner(banner);
            return this;
        }

        public Farm build() {
            return mFarm;
        }
    }

}

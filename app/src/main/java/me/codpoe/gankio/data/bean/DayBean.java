package me.codpoe.gankio.data.bean;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Codpoe on 2016/11/13.
 */

public class DayBean {

    /**
     * category : ["Android","瞎推荐","休息视频","福利","iOS","拓展资源","前端", "App"]
     * error : false
     * */

    @SerializedName("error")
    private boolean error;
    @SerializedName("results")
    private ResultsBean results;
    @SerializedName("category")
    private List<String> category;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public ResultsBean getResults() {
        return results;
    }

    public void setResults(ResultsBean results) {
        this.results = results;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public static class ResultsBean {

        @Nullable
        @SerializedName("Android")
        private List<GankBean> android;

        @Nullable
        @SerializedName("iOS")
        private List<GankBean> iOS;

        @Nullable
        @SerializedName("休息视频")
        private List<GankBean> video;

        @Nullable
        @SerializedName("前端")
        private List<GankBean> web;

        @Nullable
        @SerializedName("拓展资源")
        private List<GankBean> exResource;

        @Nullable
        @SerializedName("瞎推荐")
        private List<GankBean> recommendation;

        @Nullable
        @SerializedName("App")
        private List<GankBean> app;

        @Nullable
        @SerializedName("福利")
        private List<GankBean> meizhi;

        public List<GankBean> getAndroid() {
            return android;
        }

        public void setAndroid(List<GankBean> android) {
            this.android = android;
        }

        public List<GankBean> getIOS() {
            return iOS;
        }

        public void setIOS(List<GankBean> iOS) {
            this.iOS = iOS;
        }

        public List<GankBean> getVideo() {
            return video;
        }

        public void setVideo(List<GankBean> video) {
            this.video = video;
        }

        public List<GankBean> getWeb() {
            return web;
        }

        public void setWeb(List<GankBean> web) {
            this.web = web;
        }

        public List<GankBean> getExResource() {
            return exResource;
        }

        public void setExResource(List<GankBean> exResource) {
            this.exResource = exResource;
        }

        public List<GankBean> getRecommendation() {
            return recommendation;
        }

        public void setRecommendation(List<GankBean> recommendation) {
            this.recommendation = recommendation;
        }

        public List<GankBean> getApp() {
            return app;
        }

        public void setApp(List<GankBean> app) {
            this.app = app;
        }

        public List<GankBean> getMeizhi() {
            return meizhi;
        }

        public void setMeizhi(List<GankBean> meizhi) {
            this.meizhi = meizhi;
        }

        public static class GankBean {
            @SerializedName("_id")
            private String id;
            @SerializedName("createdAt")
            private String createdAt;
            @SerializedName("desc")
            private String desc;
            @SerializedName("publishedAt")
            private String publishedAt;
            @SerializedName("source")
            private String source;
            @SerializedName("type")
            private String type;
            @SerializedName("url")
            private String url;
            @SerializedName("used")
            private boolean used;
            @SerializedName("who")
            private String who;

            @Nullable
            @SerializedName("images")
            private List<String> images;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getPublishedAt() {
                return publishedAt;
            }

            public void setPublishedAt(String publishedAt) {
                this.publishedAt = publishedAt;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public boolean isUsed() {
                return used;
            }

            public void setUsed(boolean used) {
                this.used = used;
            }

            public String getWho() {
                return who;
            }

            public void setWho(String who) {
                this.who = who;
            }

            public List<String> getImages() {
                return images;
            }

            public void setImages(List<String> images) {
                this.images = images;
            }
        }
    }
}

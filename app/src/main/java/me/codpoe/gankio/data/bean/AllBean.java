package me.codpoe.gankio.data.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Codpoe on 2016/10/9.
 */

public class AllBean {


    /**
     * error : false
     */

    @SerializedName("error")
    private boolean error;
    /**
     * _id : 57f5c4b8421aa95de3b8ab3d
     * createdAt : 2016-10-06T11:27:52.883Z
     * desc : 超漂亮的 Animation Scale 动画设置效果。
     * images : ["http://img.gank.io/f618fbe0-af11-4d97-85aa-d89b65628d09"]
     * publishedAt : 2016-10-08T11:42:07.440Z
     * source : chrome
     * type : Android
     * url : https://github.com/nickbutcher/AnimatorDurationTile
     * used : true
     * who : Masayuki Suda
     */

    @SerializedName("results")
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        @SerializedName("_id")
        private String id;
        @SerializedName("desc")
        private String desc;
        @SerializedName("publishedAt")
        private String publishedAt;
        @SerializedName("type")
        private String type;
        @SerializedName("url")
        private String url;
        @SerializedName("who")
        private String who;
        @SerializedName("images")
        private List<String> images;

        private int height;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}

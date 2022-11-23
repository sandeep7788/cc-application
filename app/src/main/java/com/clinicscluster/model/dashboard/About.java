
package com.clinicscluster.model.dashboard;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class About {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image_alt")
    @Expose
    private String imageAlt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("meta_keywords")
    @Expose
    private String metaKeywords;
    @SerializedName("meta_title")
    @Expose
    private String metaTitle;
    @SerializedName("meta_description")
    @Expose
    private String metaDescription;
    @SerializedName("short_content")
    @Expose
    private Object shortContent;
    @SerializedName("video")
    @Expose
    private Object video;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("trash")
    @Expose
    private Integer trash;
    @SerializedName("created_by")
    @Expose
    private Object createdBy;
    @SerializedName("updated_by")
    @Expose
    private Integer updatedBy;
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public void setImageAlt(String imageAlt) {
        this.imageAlt = imageAlt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(String metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public String getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    public String getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(String metaDescription) {
        this.metaDescription = metaDescription;
    }

    public Object getShortContent() {
        return shortContent;
    }

    public void setShortContent(Object shortContent) {
        this.shortContent = shortContent;
    }

    public Object getVideo() {
        return video;
    }

    public void setVideo(Object video) {
        this.video = video;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getTrash() {
        return trash;
    }

    public void setTrash(Integer trash) {
        this.trash = trash;
    }

    public Object getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Object createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Object getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}


package com.clinicscluster.model.dashboard;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Slider {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("bg_image")
    @Expose
    private Object bgImage;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("subheading")
    @Expose
    private String subheading;
    @SerializedName("button_text")
    @Expose
    private String buttonText;
    @SerializedName("button_url")
    @Expose
    private Object buttonUrl;
    @SerializedName("alt_text")
    @Expose
    private Object altText;
    @SerializedName("meta_keywords")
    @Expose
    private Object metaKeywords;
    @SerializedName("meta_title")
    @Expose
    private Object metaTitle;
    @SerializedName("meta_description")
    @Expose
    private Object metaDescription;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("trash")
    @Expose
    private Integer trash;
    @SerializedName("created_by")
    @Expose
    private Integer createdBy;
    @SerializedName("updated_by")
    @Expose
    private Integer updatedBy;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getBgImage() {
        return bgImage;
    }

    public void setBgImage(Object bgImage) {
        this.bgImage = bgImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSubheading() {
        return subheading;
    }

    public void setSubheading(String subheading) {
        this.subheading = subheading;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public Object getButtonUrl() {
        return buttonUrl;
    }

    public void setButtonUrl(Object buttonUrl) {
        this.buttonUrl = buttonUrl;
    }

    public Object getAltText() {
        return altText;
    }

    public void setAltText(Object altText) {
        this.altText = altText;
    }

    public Object getMetaKeywords() {
        return metaKeywords;
    }

    public void setMetaKeywords(Object metaKeywords) {
        this.metaKeywords = metaKeywords;
    }

    public Object getMetaTitle() {
        return metaTitle;
    }

    public void setMetaTitle(Object metaTitle) {
        this.metaTitle = metaTitle;
    }

    public Object getMetaDescription() {
        return metaDescription;
    }

    public void setMetaDescription(Object metaDescription) {
        this.metaDescription = metaDescription;
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

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}

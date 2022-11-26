
package com.clinicscluster.model.dashboard;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class DashboardModel {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("sliders")
    @Expose
    private List<Slider> sliders = null;
    @SerializedName("services")
    @Expose
    private List<Service> services = null;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;
    @SerializedName("siteSettings")
    @Expose
    private List<AboutUsListHomeModel> siteSettings = null;

    public List<AboutUsListHomeModel> getSiteSettings() {
        return siteSettings;
    }

    public void setSiteSettings(List<AboutUsListHomeModel> siteSettings) {
        this.siteSettings = siteSettings;
    }

    @SerializedName("doctors")
    @Expose
    private List<Doctor> doctors = null;
    @SerializedName("about")
    @Expose
    private About about;
    @SerializedName("whyus")
    @Expose
    private Whyus whyus;
    @SerializedName("banner")
    @Expose
    private Banner banner;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Slider> getSliders() {
        return sliders;
    }

    public void setSliders(List<Slider> sliders) {
        this.sliders = sliders;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public About getAbout() {
        return about;
    }

    public void setAbout(About about) {
        this.about = about;
    }

    public Whyus getWhyus() {
        return whyus;
    }

    public void setWhyus(Whyus whyus) {
        this.whyus = whyus;
    }

    public Banner getBanner() {
        return banner;
    }

    public void setBanner(Banner banner) {
        this.banner = banner;
    }

}

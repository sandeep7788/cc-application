
package com.clinicscluster.model.dashboard;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Banner {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("homepage")
    @Expose
    private String homepage;
    @SerializedName("homepageupper")
    @Expose
    private Object homepageupper;
    @SerializedName("aboutpage")
    @Expose
    private String aboutpage;
    @SerializedName("aboutpageupper")
    @Expose
    private String aboutpageupper;
    @SerializedName("servicepage")
    @Expose
    private String servicepage;
    @SerializedName("servicepageupper")
    @Expose
    private String servicepageupper;
    @SerializedName("servicedetail")
    @Expose
    private String servicedetail;
    @SerializedName("servicedetailupper")
    @Expose
    private String servicedetailupper;
    @SerializedName("doctorpage")
    @Expose
    private String doctorpage;
    @SerializedName("doctorpageupper")
    @Expose
    private String doctorpageupper;
    @SerializedName("appointmentpage")
    @Expose
    private String appointmentpage;
    @SerializedName("appointmentpageupper")
    @Expose
    private Object appointmentpageupper;
    @SerializedName("contactpage")
    @Expose
    private String contactpage;
    @SerializedName("contactpageupper")
    @Expose
    private String contactpageupper;
    @SerializedName("contactpagemain")
    @Expose
    private String contactpagemain;
    @SerializedName("loginpage")
    @Expose
    private String loginpage;
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

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public Object getHomepageupper() {
        return homepageupper;
    }

    public void setHomepageupper(Object homepageupper) {
        this.homepageupper = homepageupper;
    }

    public String getAboutpage() {
        return aboutpage;
    }

    public void setAboutpage(String aboutpage) {
        this.aboutpage = aboutpage;
    }

    public String getAboutpageupper() {
        return aboutpageupper;
    }

    public void setAboutpageupper(String aboutpageupper) {
        this.aboutpageupper = aboutpageupper;
    }

    public String getServicepage() {
        return servicepage;
    }

    public void setServicepage(String servicepage) {
        this.servicepage = servicepage;
    }

    public String getServicepageupper() {
        return servicepageupper;
    }

    public void setServicepageupper(String servicepageupper) {
        this.servicepageupper = servicepageupper;
    }

    public String getServicedetail() {
        return servicedetail;
    }

    public void setServicedetail(String servicedetail) {
        this.servicedetail = servicedetail;
    }

    public String getServicedetailupper() {
        return servicedetailupper;
    }

    public void setServicedetailupper(String servicedetailupper) {
        this.servicedetailupper = servicedetailupper;
    }

    public String getDoctorpage() {
        return doctorpage;
    }

    public void setDoctorpage(String doctorpage) {
        this.doctorpage = doctorpage;
    }

    public String getDoctorpageupper() {
        return doctorpageupper;
    }

    public void setDoctorpageupper(String doctorpageupper) {
        this.doctorpageupper = doctorpageupper;
    }

    public String getAppointmentpage() {
        return appointmentpage;
    }

    public void setAppointmentpage(String appointmentpage) {
        this.appointmentpage = appointmentpage;
    }

    public Object getAppointmentpageupper() {
        return appointmentpageupper;
    }

    public void setAppointmentpageupper(Object appointmentpageupper) {
        this.appointmentpageupper = appointmentpageupper;
    }

    public String getContactpage() {
        return contactpage;
    }

    public void setContactpage(String contactpage) {
        this.contactpage = contactpage;
    }

    public String getContactpageupper() {
        return contactpageupper;
    }

    public void setContactpageupper(String contactpageupper) {
        this.contactpageupper = contactpageupper;
    }

    public String getContactpagemain() {
        return contactpagemain;
    }

    public void setContactpagemain(String contactpagemain) {
        this.contactpagemain = contactpagemain;
    }

    public String getLoginpage() {
        return loginpage;
    }

    public void setLoginpage(String loginpage) {
        this.loginpage = loginpage;
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

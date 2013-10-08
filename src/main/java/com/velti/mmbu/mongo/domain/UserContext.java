package com.velti.mmbu.mongo.domain;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.PrePersist;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.Map;

/**
 * User: flioutsis
 * Date: 9/26/13
 * Time: 5:36 PM
 */
@Entity(value = "users", noClassnameStored = true)
public class UserContext {
    @Id
    private ObjectId id;

    private String msisdn;

    private String countryCode;

    private String npa;

    private String nxx;

    @Indexed
    private Map<String, String> userProperties;

    @Indexed
    private Map<String, UserOptState> lists;

    private Date createdDate;

    private Date updatedDate;

    @PrePersist
    public void prePersist() {
        createdDate = (createdDate == null) ? new Date() : createdDate;
        updatedDate = (updatedDate == null) ? new Date() : updatedDate;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getNpa() {
        return npa;
    }

    public void setNpa(String npa) {
        this.npa = npa;
    }

    public String getNxx() {
        return nxx;
    }

    public void setNxx(String nxx) {
        this.nxx = nxx;
    }

    public Map<String, String> getUserProperties() {
        return userProperties;
    }

    public void setUserProperties(Map<String, String> userProperties) {
        this.userProperties = userProperties;
    }

    public Map<String, UserOptState> getLists() {
        return lists;
    }

    public void setLists(Map<String, UserOptState> lists) {
        this.lists = lists;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
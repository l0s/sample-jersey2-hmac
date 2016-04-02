package com.macasaet.rest.domain;

import static org.apache.commons.lang.StringUtils.isBlank;

import java.util.UUID;

public class Entry {

    private String id;
    private String title;
    private String description;
    private String lastUpdatedBy;
    
    public String getId() {
        if (isBlank(id)) {
            setId(UUID.randomUUID().toString());
        }
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastUpdatedBy() {
        if (isBlank(lastUpdatedBy)) {
            setLastUpdatedBy("system");
        }
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

}
package org.kenakata.Model.JsonModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.kenakata.Utils.DateFormat;

import javax.persistence.*;
import java.util.Date;

public class Category {

    public int id;

    public String name;

    public String status;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = DateFormat.myDateFormat)
    public String created_at;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = DateFormat.myDateFormat)
    public String updated_at;

    public Category() {
    }

    public Category(int id, String name, String status, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}

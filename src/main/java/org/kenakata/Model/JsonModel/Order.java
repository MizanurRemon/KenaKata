package org.kenakata.Model.JsonModel;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

public class Order {
    public int id;
    public String status;
    public User user;
    public Product product;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd MMM, yyyy")
    public String created_at;
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "dd MMM, yyyy")
    public String updated_at;



}

package org.kenakata.Model.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.util.Date;

@Entity(name = "tbl_category")
@Table(name = "tbl_category")
public class EntityCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "status")
    private String status;

//    @Column(name = "created_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    @JsonFormat(pattern = "dd MMM, yyyy")
//    private Date created_at;
//
//    @Column(name = "updated_at")
//    @Temporal(TemporalType.TIMESTAMP)
//    @JsonFormat(pattern = "dd MMM, yyyy")
//    private Date updated_at;

    public EntityCategory() {
    }

    public EntityCategory(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
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

}

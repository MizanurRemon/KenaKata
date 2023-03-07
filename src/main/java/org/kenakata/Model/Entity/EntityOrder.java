package org.kenakata.Model.Entity;


import org.kenakata.Utils.Constants;

import javax.persistence.*;

@Entity(name = "tbl_order")
@Table(name = "tbl_order")
public class EntityOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "user_id")
    private int user_id;
    @Column(name = "product_id")
    private int product_id;

    public EntityOrder() {
    }

    public EntityOrder(int id, int user_id, int product_id) {
        this.id = id;
        this.user_id = user_id;
        this.product_id = product_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

}

package org.kenakata.Model.Entity;


import javax.persistence.*;

@Entity(name = "tbl_product")
@Table(name = "tbl_product")
public class EntityProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private String price;

    @Column(name = "category_id")
    private int category_id;

    @Column(name = "unit")
    private String unit;

    @Column(name = "stock")
    private String stock;

    @Column(name = "status")
    private String status;

    public EntityProduct() {
    }

    public EntityProduct(int id, String name, String price, int category_id, String unit, String stock, String status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category_id = category_id;
        this.unit = unit;
        this.stock = stock;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

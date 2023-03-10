package com.github.parasmani300.rmsorders.model;

import javax.persistence.*;

@Entity
@Table(name = "OrderGenerator")
public class OrderGenerator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer orderNo;

    public OrderGenerator() {
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}

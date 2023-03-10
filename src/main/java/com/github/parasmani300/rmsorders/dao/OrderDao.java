package com.github.parasmani300.rmsorders.dao;

import com.github.parasmani300.rmsorders.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order,Integer> {
}

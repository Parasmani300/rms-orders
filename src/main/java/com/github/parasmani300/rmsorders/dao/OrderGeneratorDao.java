package com.github.parasmani300.rmsorders.dao;

import com.github.parasmani300.rmsorders.model.OrderGenerator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderGeneratorDao extends JpaRepository<OrderGenerator,Integer> {
}

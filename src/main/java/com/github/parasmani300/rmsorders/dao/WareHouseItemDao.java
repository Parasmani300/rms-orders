package com.github.parasmani300.rmsorders.dao;

import com.github.parasmani300.rmsorders.model.WarehouseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WareHouseItemDao extends JpaRepository<WarehouseItem,Integer> {
    public List<WarehouseItem> findByWhIDAndItemID(Integer whID, Integer itemID);
}

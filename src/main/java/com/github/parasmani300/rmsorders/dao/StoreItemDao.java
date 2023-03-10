package com.github.parasmani300.rmsorders.dao;

import com.github.parasmani300.rmsorders.model.StoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreItemDao extends JpaRepository<StoreItem,Integer> {
    List<StoreItem> findByStoreIdAndItemID(Integer storeId, Integer itemID);
}

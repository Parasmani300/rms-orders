package com.github.parasmani300.rmsorders.service.impl;

import com.github.parasmani300.rmsorders.dao.OrderDao;
import com.github.parasmani300.rmsorders.dao.OrderGeneratorDao;
import com.github.parasmani300.rmsorders.dao.StoreItemDao;
import com.github.parasmani300.rmsorders.dao.WareHouseItemDao;
import com.github.parasmani300.rmsorders.model.Order;
import com.github.parasmani300.rmsorders.model.OrderGenerator;
import com.github.parasmani300.rmsorders.model.StoreItem;
import com.github.parasmani300.rmsorders.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderDao orderDao;

    @Autowired
    StoreItemDao storeItemDao;

    @Autowired
    WareHouseItemDao wareHouseItemDao;

    @Autowired
    OrderGeneratorDao orderGeneratorDao;

    @Override
    public Order makeOrder(Order order) {
        List<StoreItem> storeItem = storeItemDao.findByStoreIdAndItemID(order.getStoreId(),order.getItemId());
        if(storeItem.size() == 0){
            return null;
        }
        StoreItem myStore = storeItem.get(0);
        myStore.setStockOnHand(myStore.getStockOnHand() - order.getQty());
        myStore.setNonSellableQty(myStore.getNonSellableQty() + order.getQty());
        storeItemDao.save(myStore);
        Order order1 = orderDao.save(order);
        order1.setOrderNo(order1.getTxnNo());
        orderDao.save(order);
        return order1;
    }

    @Override
    public List<Order> makeOrder(Order[] orders) {
        List<Order> orderList = new ArrayList<>();
//        OrderGenerator orderGenerator = orderGeneratorDao.save(new OrderGenerator());
        Integer orderNo = null;
        boolean orderAvailable = false;
        while(!orderAvailable)
        {
            OrderGenerator orderGenerator = orderGeneratorDao.save(new OrderGenerator());
            orderNo = orderGenerator.getOrderNo();
            Optional<Order> myOrder = orderDao.findById(orderNo);
            if(!myOrder.isPresent()){
                orderAvailable = true;
            }
        }
        for(int i = 0;i<orders.length;i++)
        {
            Order order = orders[i];
            List<StoreItem> storeItem = storeItemDao.findByStoreIdAndItemID(order.getStoreId(),order.getItemId());
            if(storeItem.size() == 0){
                continue;
            }
            StoreItem myStore = storeItem.get(0);
            myStore.setStockOnHand(myStore.getStockOnHand() - order.getQty());
            myStore.setNonSellableQty(myStore.getNonSellableQty() + order.getQty());
            storeItemDao.save(myStore);
            Order order1 = orderDao.save(order);
            order1.setOrderNo(orderNo);
            order1 = orderDao.save(order);
            orderList.add(order1);
        }
        return orderList;
    }

    @Override
    public Order fetchOrderDetails(Integer orderId) {
        Optional<Order> orderDetails = orderDao.findById(orderId);
        if(!orderDetails.isPresent()){
            System.out.println("Order is not present");
            return null;
        }

        return orderDetails.get();
    }

    @Override
    public Order updateOrderDetails(Integer orderId, String columnName, String columnValue) throws ParseException {
        Optional<Order> optionalOrder = orderDao.findById(orderId);
        if(!optionalOrder.isPresent()){
            return null;
        }
        Order order = optionalOrder.get();
        if(columnName.equals("itemId"))
        {
            order.setItemId(Integer.parseInt(columnValue));
        }else if(columnName.equals("shippingStatus"))
        {
            order.setShippingStatus(columnValue);
        }else if(columnName.equals("orderDate"))
        {
            Date date = new SimpleDateFormat("dd/mm/yyyy").parse(columnValue);
            order.setOrderDate(date);
        }else if(columnName.equals("shippedDate"))
        {
            Date date = new SimpleDateFormat("dd/mm/yyyy").parse(columnValue);
            order.setShippedDate(date);
        }else if(columnName.equals("deliveredDate"))
        {
            Date date = new SimpleDateFormat("dd/mm/yyyy").parse(columnValue);
            order.setDeliveredDate(date);
        }else if(columnName.equals("qty")){
            List<StoreItem> storeItem = storeItemDao.findByStoreIdAndItemID(order.getStoreId(),order.getItemId());
            if(storeItem.size() == 0){
                return null;
            }
            StoreItem myStore = storeItem.get(0);
            myStore.setStockOnHand(myStore.getStockOnHand() - (Integer.parseInt(columnValue) - order.getQty()));
            myStore.setNonSellableQty(myStore.getNonSellableQty() + (Integer.parseInt(columnValue) - order.getQty()));
            storeItemDao.save(myStore);
            order.setQty(Integer.parseInt(columnValue));
        }else if(columnName.equals("processedInd")){
            order.setProcessedInd(columnValue);
        }else if(columnName.equals("storeId")){
            order.setStoreId(Integer.parseInt(columnValue));
        }

        return order;
    }

    @Override
    public Order deleteOrderDetails(Integer orderId) {
        Optional<Order> optionalOrder = orderDao.findById(orderId);
        if(!optionalOrder.isPresent()){
            return null;
        }
        orderDao.deleteById(orderId);
        return optionalOrder.get();
    }

    @Override
    public List<Order> fetchAllOrders(Integer pageNo, Integer pageSize) {
        Pageable p = PageRequest.of(pageNo,pageSize);
        Page<Order> orderPage = orderDao.findAll(p);
        List<Order> orderList = orderPage.getContent();
        return orderList;
    }
}

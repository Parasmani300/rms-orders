package com.github.parasmani300.rmsorders.service;

import com.github.parasmani300.rmsorders.model.Order;

import java.text.ParseException;
import java.util.List;

public interface OrderService {
    public Order makeOrder(Order order);
    public List<Order> makeOrder(Order[] order);
    public Order fetchOrderDetails(Integer orderId);
    public Order updateOrderDetails(Integer orderId,String columnName,String columnValue) throws ParseException;
    public Order deleteOrderDetails(Integer orderId);
    public List<Order> fetchAllOrders(Integer pageNo,Integer pageSize);
}

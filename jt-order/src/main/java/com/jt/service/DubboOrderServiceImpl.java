package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

@Service
public class DubboOrderServiceImpl implements DubboOrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	/**
	 * 
	 */
	@Override
	public String saveOrder(Order order) {
		String orderId=""+order.getUserId()+System.currentTimeMillis() ;
		order.setOrderId(orderId).setCreated(new Date()).setUpdated(order.getCreated());
		order.setStatus(1);
		orderMapper.insert(order);
		System.out.println("订单入库成功");
		
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setItemId(orderId).setCreated(new Date()).setUpdated(orderItem.getCreated());
			orderItemMapper.insert(orderItem);
		}
		System.out.println("订单物流入库成功");
		
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId).setCreated(new Date()).setUpdated(orderShipping.getCreated());
		orderShippingMapper.insert(orderShipping);
		System.out.println("订单商品入库成功");
		return orderId;
	}

	@Override
	public Order findOrderById(String id) {
//		Order order =orderMapper.selectById(id); 
//		OrderShipping orderShipping = orderShippingMapper.selectById(id);
//		QueryWrapper<OrderItem> queryWrapper=new QueryWrapper<>();
//		queryWrapper.eq("order_id", id);
//		List<OrderItem> orderItems = orderItemMapper.selectList(queryWrapper);
//		order.setOrderShipping(orderShipping)
//			 .setOrderItems(orderItems);
		return orderMapper.findOrderById(id);
	}
	
}

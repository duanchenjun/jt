package com.jt.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

@RestController
@RequestMapping("/web/item")
public class WebItenController {
	
	@Autowired
	private ItemService itemService;
	/**
	 * 编辑jt-manage.完成数据获取
	 *  /findItemById?id=10086
	 * @param id
	 * @return
	 */
	@RequestMapping("/findItemById")
	public Item findItemById(Long id) {
		
		return itemService.findItemById(id);
	}
	@RequestMapping("/findItemDescById")
	public ItemDesc findItemDescById(Long id) {
		
		return itemService.findItemDescById(id);
	}
}

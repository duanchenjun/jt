package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;
import com.jt.vo.EasyUITable;
import com.jt.vo.SysResult;

@RestController //返回数据都是json
@RequestMapping("/item")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@RequestMapping("/query")
	public EasyUITable findItemByPage(Integer page,Integer rows) {
		return itemService.findItemByPage(page,rows);
	}
	
	@RequestMapping("/save")
	public SysResult saveItem(Item item,ItemDesc itemDesc) {
			itemService.saveItem(item,itemDesc);
			return SysResult.success();
		}
	@RequestMapping("/update")
	public SysResult updateItem(Item item,ItemDesc itemDesc) {
		itemService.updateItem(item,itemDesc);
		return SysResult.success();
	}
	
	@RequestMapping("/delete")
	public SysResult deleteItems(Long[] ids) {
		itemService.deleteItems(ids);
		return SysResult.success();
	}
	@RequestMapping("/instock")
	public SysResult instock(Long[] ids) {
		int status=2; //下架
		itemService.updateItemState(status,ids);
		return SysResult.success();
	}
	@RequestMapping("/reshelf")
	public SysResult reshelf(Long[] ids) {
		int status=1; //上架
		itemService.updateItemState(status,ids);
		return SysResult.success();
	}
	/**
	 * 实现商品详情查询
	 */
	@RequestMapping("/query/item/desc/{itemId}")
	public  SysResult findItemDescById(@PathVariable Long itemId) {
		ItemDesc itemDesc=itemService.findItemDescById(itemId);
		//将数据回传给页面
		return SysResult.success(itemDesc);
	}
	
	
}

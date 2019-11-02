package com.jt.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.anno.Cache_Find;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;

import redis.clients.jedis.Jedis;


@Service
public class ItemCatServiceImpl implements ItemCatService{
	@Autowired
	private ItemCatMapper itemCatMapper;
	//@Autowired
	private Jedis jedis;
	
	@Override
	public ItemCat findItemCatById(Long itemCatId) {
		return itemCatMapper.selectById(itemCatId);
	}
	
	/**
	 * VO对象~~~转换~~~POJO
	 * 思路:
	 * 	1.根据parentId查询一级商品分类信息List<ItemCat>
	 * 	2.遍历List
	 */
	@Override
	@Cache_Find
	public List<EasyUITree> findEasyUIByParentId(Long parentId) {
		List<ItemCat> itemCatList = findItemCatByParentId(parentId);
		List<EasyUITree> treeLsit=new ArrayList<>();
		for (ItemCat itemCat : itemCatList) {
			Long id=itemCat.getId();
			String text=itemCat.getName();
			String state=itemCat.getIsParent()?"closed":"open";
			EasyUITree easyUITree=new EasyUITree(id, text, state);
			treeLsit.add(easyUITree);
		}
		System.out.println("=============数据库");
		return treeLsit;
	}
	public List<ItemCat> findItemCatByParentId(Long parentId){
		QueryWrapper<ItemCat> queryWrapper=new QueryWrapper<ItemCat>();
		queryWrapper.eq("parent_id", parentId);
		List<ItemCat> itemCatList = itemCatMapper.selectList(queryWrapper);
		return itemCatList;
	}
	/**
	 * 缓存操作流程:
	 * 1.先查询缓存
	 * 2.结果为null,说明用户第一次查询,应该先查询数据库,将数据库数据保存到缓存
	 * 3.结果不为null,json串讲json数据转化为对象
	 */
	
	@Override
	public List<EasyUITree> findEasyUITreeCache(Long parentId) {
//		String value = jedis.get(String.valueOf(parentId));
//		if(value!=null) {
//			System.out.println("=====缓存");
//		}
//		
//		if(value==null) {
//			List<EasyUITree> list=findEasyUIByParentId(parentId);
//			 value = ObjectMapperUtil.toJSON(list);
//			jedis.set(String.valueOf(parentId), value);
//			
//			return list;
//		}
//		List<EasyUITree> list=ObjectMapperUtil.toObject(value, new ArrayList().getClass());
		
		String key= "ITEM_CAT_"+parentId;
		String result=jedis.get(key); //获取json
		System.out.println("================"+result);
		List<EasyUITree> treeList=new ArrayList<>();
		if(StringUtils.isEmpty(result)) {
			//用户第一次查询
			treeList=findEasyUIByParentId(parentId);
			System.out.println("用户第一次查询数据库");
			//将数据保存到缓存中
			String value= ObjectMapperUtil.toJSON(treeList);
			jedis.set(key, value);
		}else {
			//说明 缓存中有数据,直接返回数据
			treeList=ObjectMapperUtil.toObject(result, treeList.getClass());
			System.out.println("redis缓存查询!!!");
		}
		return treeList;
	}
	
	
	
	
	
	
	
}

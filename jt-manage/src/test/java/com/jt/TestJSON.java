package com.jt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.ItemDesc;
@TestComponent
@RunWith(SpringRunner.class)
public class TestJSON {
	private static final ObjectMapper MAPPER=new ObjectMapper();
	@Test
	public void toJSON() throws IOException {
		ItemDesc itemDesc=new ItemDesc();
		itemDesc.setItemId(100L).setItemDesc("商品详情").setCreated(new Date())
		.setUpdated(itemDesc.getCreated());
		String json = MAPPER.writeValueAsString(itemDesc);
		System.out.println(json);
		
		//将json转化为对象
		ItemDesc desc = MAPPER.readValue(json, ItemDesc.class);
		System.out.println(desc);
		
	}
	@Test
	public void testList() throws IOException {
		ItemDesc itemDesc1=new ItemDesc();
		itemDesc1.setItemId(100L).setItemDesc("商品详情").setCreated(new Date())
		.setUpdated(itemDesc1.getCreated());
		ItemDesc itemDesc2=new ItemDesc();
		itemDesc2.setItemId(100L).setItemDesc("商品详情").setCreated(new Date())
		.setUpdated(itemDesc2.getCreated());
		List<ItemDesc> list=new ArrayList<>();
		list.add(itemDesc1);
		list.add(itemDesc2);
		String json = MAPPER.writeValueAsString(list);
		System.out.println(json);
		
		List<ItemDesc> list2 = MAPPER.readValue(json, list.getClass());
		System.out.println(list2);
	}
	
	
	
	
	
	
}

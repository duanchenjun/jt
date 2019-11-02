package com.jt;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jt.mapper.ItemMapper;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemTest {
	@Autowired
	private ItemMapper itemMapper;
	@Test
	public void test1() {
		Long[] ids= {1474391965L,1474391966L};
		int rows = itemMapper.deleteByIds(ids);
		System.out.println(rows);
	}
}

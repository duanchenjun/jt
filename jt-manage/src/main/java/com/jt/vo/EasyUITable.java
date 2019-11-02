package com.jt.vo;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EasyUITable {
	/**
	 * 数据转化为JSON串时调用属性的get方法
	 * getTotal() ~~~get去掉~~~首字母小写 生成key
	 * value:利用get方法获取的值.
	 * 
	 * json转化为对象
	 * 调用对象的set方法
	 */
	private Integer total;
	private List<?> rows;
	
	
	public EasyUITable() {
		super();
	}
	
	public EasyUITable(Integer total, List<?> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}


	
	
}

package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor		//无参构造
@AllArgsConstructor		//全部参数结构
public class EasyUITree {
	private Long id;		//节点Id
	private String text;	//节点名称
	private String state;	//节点状态 open/closed
	
}

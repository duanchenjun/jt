package com.jt.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ImageVO {
	private Integer error=0;	//0	没错,1表示有错
	private String url;			//图片的虚拟路径
	private Integer width;		//宽度
	private Integer height;		//高度
	
	//指定失败的方法
	public static ImageVO fail() {
		return new ImageVO(1, null, null, null);
	}
}

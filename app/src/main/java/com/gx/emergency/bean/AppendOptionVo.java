package com.gx.emergency.bean;

import org.litepal.crud.DataSupport;

/**
 * 选项
 */
public class AppendOptionVo extends DataSupport {
	private Integer id;//选项ID
	private String name;//选项名称

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public AppendOptionVo(Integer id,String name) {
		super();
		this.id=id;
		this.name = name;
	}

}

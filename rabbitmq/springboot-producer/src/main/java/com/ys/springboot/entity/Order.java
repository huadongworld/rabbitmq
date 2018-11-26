package com.ys.springboot.entity;

import java.io.Serializable;

/**
 * @author HuaDong
 * @date 2018/11/26 21:25
 */
public class Order implements Serializable {

	private String id;

	private String name;
	
	public Order() {
	}
	public Order(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

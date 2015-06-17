package com.chzu.app.bean;

import java.io.Serializable;

public class BaseBean implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1773168421771841768L;

	public final String TAG = BaseBean.class.getSimpleName();

	public int id;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}

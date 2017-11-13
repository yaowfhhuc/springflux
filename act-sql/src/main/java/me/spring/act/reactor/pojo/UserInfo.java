package me.spring.act.reactor.pojo;

import java.io.Serializable;


public class UserInfo implements Serializable{

	private static final long serialVersionUID = 5709643397841607433L;
	private String id;
	private String name;
	private int age;
	
	
	public UserInfo() {
	}
	public UserInfo(String id) {
		this.id=id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	
}

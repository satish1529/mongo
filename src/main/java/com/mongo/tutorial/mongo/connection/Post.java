package com.mongo.tutorial.mongo.connection;

import java.util.List;

public class Post  {
	String name;
	int id;
	List<Comments> comments;
	
	List<Integer> likes;
	
	Post(String name ,int id)
	{
		this.name = name;
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
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}


	/**
	 * @return the comments
	 */
	public List<Comments> getComments() {
		return comments;
	}


	/**
	 * @param comments the comments to set
	 */
	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}


	/**
	 * @return the likes
	 */
	public List<Integer> getLikes() {
		return likes;
	}


	/**
	 * @param likes the likes to set
	 */
	public void setLikes(List<Integer> likes) {
		this.likes = likes;
	}
	
	
	
}

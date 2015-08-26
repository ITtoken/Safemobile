package com.example.entity;

import java.io.Serializable;

public class News implements Serializable{
	
	private int sourceId;
	private String newsTitle;
	
	
	
	public News(int sourceId, String newsTitle) {
		super();
		this.sourceId = sourceId;
		this.newsTitle = newsTitle;
	}
	public News() {
	}
	public int getSourceId() {
		return sourceId;
	}
	public void setSourceId(int sourceId) {
		this.sourceId = sourceId;
	}
	public String getNewsTitle() {
		return newsTitle;
	}
	public void setNewsTitle(String newsTitle) {
		this.newsTitle = newsTitle;
	}



	
}

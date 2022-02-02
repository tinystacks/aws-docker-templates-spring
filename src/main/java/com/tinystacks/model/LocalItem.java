package com.tinystacks.model;

public class LocalItem {

	private Integer id;
	private String title;
	private String content;

	public LocalItem() {
	}

	public LocalItem(Integer id, String title, String content) {
		this.id= id;
		this.title = title;
		this.content = content;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		return "LocalItem [id=" + id + ", title=" + title + ", content=" + content + "]";
	}
}
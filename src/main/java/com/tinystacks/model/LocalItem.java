package com.tinystacks.model;

import java.util.UUID;

public class LocalItem {

	private String id;
	private String title;
	private String content;

	public LocalItem() {
	}

	public LocalItem(String id, String title, String content) {
		UUID uuid = UUID.randomUUID();
		this.id= uuid.toString();
		this.title = title;
		this.content = content;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
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
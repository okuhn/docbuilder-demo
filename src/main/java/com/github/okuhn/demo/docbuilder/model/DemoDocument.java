package com.github.okuhn.demo.docbuilder.model;

import lombok.Data;

@Data
public class DemoDocument {
	private byte[] content;
	private String name;
	private String mimeType;
}

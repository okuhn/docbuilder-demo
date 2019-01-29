package com.github.okuhn.demo.docbuilder.service;

import org.springframework.stereotype.Service;

import com.github.okuhn.demo.docbuilder.model.DemoDocument;

@Service
public class PlainTextService implements DocumentCreationService {

	@Override
	public DemoDocument createDocument(final String name) {
		final DemoDocument document = new DemoDocument();
		document.setContent(name.getBytes());
		document.setName(name + ".txt");
		document.setMimeType("text/plain");
		return document;		
	}
}

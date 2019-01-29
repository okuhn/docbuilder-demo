package com.github.okuhn.demo.docbuilder.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.github.okuhn.demo.docbuilder.model.DemoDocument;

@Service
public class PowerPointService implements DocumentCreationService {

	private static final String PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
	
	@Override
	public DemoDocument createDocument(String name) {
		final DemoDocument document = new DemoDocument();
		document.setContent(getPowerPointBytes(name));
		document.setName(name + ".pptx");
		document.setMimeType(PPTX);
		return document;		
	}

	private byte[] getPowerPointBytes(String name) {
		
		try(final XMLSlideShow pptx = new XMLSlideShow(new ClassPathResource("template.pptx").getInputStream());) {
			final XSLFSlide slide0 = pptx.getSlides().get(0);
			slide0.getPlaceholder(0).setText(name);
			slide0.getPlaceholder(1).setText(name);
    		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
			pptx.write(stream);
    		return stream.toByteArray();
    	} catch (IOException e) {
    		throw new RuntimeException("Error creating powerpoint document: " + e.getMessage());
    	}
	}
}

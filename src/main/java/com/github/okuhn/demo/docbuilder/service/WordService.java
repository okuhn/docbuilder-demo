package com.github.okuhn.demo.docbuilder.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import com.github.okuhn.demo.docbuilder.model.DemoDocument;

@Service
public class WordService implements DocumentCreationService {

	private static final String DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
	
	@Override
	public DemoDocument createDocument(String name) {
		final DemoDocument document = new DemoDocument();
		document.setContent(getWordbytes(name));
		document.setName(name + ".docx");
		document.setMimeType(DOCX);
		return document;		
	}

	private byte[] getWordbytes(String name) {
		try(final XWPFDocument document = new XWPFDocument();) {
			final XWPFParagraph paragraph = document.createParagraph();
		    final XWPFRun run = paragraph.createRun();
		    run.setText(name);
    		final ByteArrayOutputStream stream = new ByteArrayOutputStream();
    		document.write(stream);
    		return stream.toByteArray();
    	} catch (IOException e) {
    		throw new RuntimeException("Error creating word document: " + e.getMessage());
    	}
	}

}

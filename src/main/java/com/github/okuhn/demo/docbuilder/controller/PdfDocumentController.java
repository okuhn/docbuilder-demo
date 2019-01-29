package com.github.okuhn.demo.docbuilder.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.okuhn.demo.docbuilder.model.DemoDocument;
import com.github.okuhn.demo.docbuilder.service.CommonDocumentCreationService;

@RestController
public class PdfDocumentController {

	private final CommonDocumentCreationService commonDocumentCreationService;
	
	public PdfDocumentController(CommonDocumentCreationService commonDocumentCreationService) {
		this.commonDocumentCreationService = commonDocumentCreationService;
	}
	
	@RequestMapping(value = "/document/{name}", method = RequestMethod.GET)
    public ResponseEntity<InputStreamResource> document(@PathVariable("name") final String name, final HttpServletResponse response) 
    		throws IOException {
        return createDoc(name, response);
    }

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> create(@RequestParam("name") final String name, final HttpServletResponse response) throws IOException {
		return createDoc(name, response);
	}
	
	private ResponseEntity<InputStreamResource> createDoc(final String name, final HttpServletResponse response) {
        final DemoDocument doc = commonDocumentCreationService.createDocument(name);
		final ByteArrayInputStream bis = new ByteArrayInputStream(doc.getContent());
        response.setHeader("Content-Disposition", "inline; filename=\"" + doc.getName() +  "\"");
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(doc.getMimeType()))
                .body(new InputStreamResource(bis));
	}
}

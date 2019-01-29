package com.github.okuhn.demo.docbuilder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.okuhn.demo.docbuilder.model.DemoDocument;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Service
public class CommonDocumentCreationService implements DocumentCreationService {

	@Autowired
	private PdfDocumentService pdfDocumentService;
	
	@Autowired
	private ExcelService excelService;
	
	@Autowired
	private PlainTextService plainTextService;
	
	@Autowired
	private WordService wordService;

	@Autowired
	private PowerPointService powerPointService;
	
	private final Counter plaintextCounter;
	private final Counter pdfCounter;
	private final Counter wordCounter;
	private final Counter excelCounter;
	private final Counter powerPointCounter;
		
	public CommonDocumentCreationService(final MeterRegistry registry) {
		plaintextCounter = buildCounter(registry, "plaintext");
		pdfCounter = buildCounter(registry, "pdf");
		wordCounter = buildCounter(registry, "word");
		excelCounter = buildCounter(registry, "excel");
		powerPointCounter = buildCounter(registry, "powerPoint");
	}

	private Counter buildCounter(final MeterRegistry registry, final String tag) {
		return Counter
			.builder("demo_document_generation_count")
			.tags("doc_type", tag)
			.register(registry);
	}

	@Override
	public DemoDocument createDocument(String name) {
		
		final int startIndex = Character.isLetter(name.charAt(0)) ? 0 : 1;
		switch(name.toLowerCase().substring(startIndex, startIndex+1)) {
		case "a":
			plaintextCounter.increment();
			return plainTextService.createDocument(name);
		case "x":
			excelCounter.increment();
			return excelService.createDocument(name);
		case "p":
			powerPointCounter.increment();
			return powerPointService.createDocument(name);
		case "d":
		case "w":
			wordCounter.increment();
			return wordService.createDocument(name);
		default:
			pdfCounter.increment();
			return pdfDocumentService.createDocument(name);
		}
	}
}

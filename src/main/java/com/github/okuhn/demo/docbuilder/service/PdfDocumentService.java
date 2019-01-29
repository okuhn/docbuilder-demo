package com.github.okuhn.demo.docbuilder.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.github.okuhn.demo.docbuilder.model.DemoDocument;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfGState;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PdfDocumentService implements DocumentCreationService {

	
	@Override
	public DemoDocument createDocument(final String name) {
		final DemoDocument document = new DemoDocument();
		document.setContent(createPdfBytes(name));
		document.setName(name + ".pdf");
		document.setMimeType(MediaType.APPLICATION_PDF_VALUE);
		return document;		
	}

	private byte[] createPdfBytes(final String name) {
		final ByteArrayOutputStream unstampedOutputStream = new ByteArrayOutputStream();
		try {
			createPdf(unstampedOutputStream, name);
			final byte[] unstampedBytes = unstampedOutputStream.toByteArray();
			return stamp(unstampedBytes);
		} catch (IOException | DocumentException e) {
			return e.getMessage().getBytes();
		}
	}
	
	private byte[] stamp(byte[] unstampedBytes) throws DocumentException, IOException {
		final PdfReader reader = new PdfReader(unstampedBytes);
		final ByteArrayOutputStream stampedOutputStream = new ByteArrayOutputStream();
		final PdfStamper stamper = new PdfStamper(reader, stampedOutputStream);

        final int numberOfPages = reader.getNumberOfPages();
		for (int i = 1; i <= numberOfPages; i++) {
			final PdfContentByte canvas = stamper.getOverContent(i);
			final PdfGState gs1 = new PdfGState();
        	gs1.setFillOpacity(0.2f);
        	canvas.setGState(gs1);
        	final Font font = new Font();
        	font.setSize(192);
        	final Phrase entwurfPhrase = new Phrase("Page " + i, font);
        	final float diagonalRotation = (float)Math.toDegrees(Math.atan(Math.sqrt(2)));
        	ColumnText.showTextAligned(canvas,
        			Element.ALIGN_LEFT, entwurfPhrase, 180, 100, diagonalRotation);
        }
        
        stamper.close();
        reader.close();
        return stampedOutputStream.toByteArray();
	}

	private void createPdf(final OutputStream outputStream, final String name) throws IOException, DocumentException {
		final Font font = new Font(null, 14, Font.BOLD);

		final float left = 50;
		final float right = 20;
		final float top = 40;
		final float bottom = 36;
		Document document = new Document(PageSize.A4, left, right, top, bottom);

		PdfWriter.getInstance(document, outputStream);

		document.open();
	
		final Chunk c1 = new Chunk("Test document " + name, font);
		final Paragraph p0 = new Paragraph("Page 1");
		p0.setSpacingBefore(300);
		document.add(p0);
		
		final Paragraph p1 = new Paragraph(c1);
        p1.setSpacingBefore(300);
		document.add(p1);
		
		final Paragraph p2 = new Paragraph("Page 2");
		p2.setSpacingBefore(300);
		document.add(p2);
		
		final Paragraph p3 = new Paragraph(c1);
        p3.setSpacingBefore(300);
		document.add(p3);

		document.close();
	}
}

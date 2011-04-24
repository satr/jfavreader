package com.google.code.p.jfavreader.pdf.validation;

import java.util.Map;

import com.google.code.p.jfavreader.pdf.objects.AbstractPdfDocumentObject;

public class PdfDictionarySubdictionaryValidator extends
		AbstractPdfDictionaryValidator {
	private Map<Integer, AbstractPdfDocumentObject> ContentObjects;

	public PdfDictionarySubdictionaryValidator(
			Map<String, Object> subdictionary,
			Map<Integer, AbstractPdfDocumentObject> contentObjects) {
		super(subdictionary, "");
		this.ContentObjects = contentObjects;
	}

	public void Validate() {
		PdfDictionaryValidator.Validate(dictionary, ContentObjects);
	}
}

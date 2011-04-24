package com.google.code.p.jfavreader.pdf.validation;

import java.util.Map;

public class PdfDictionaryIntegerValidator extends AbstractPdfDictionaryValidator {
	protected PdfDictionaryIntegerValidator(Map<String, Object> dictionary,
			String key, String value) {
		super(dictionary, key);
		Value = value;
	}

	private String Value;

	public void Validate() {
		dictionary.put(Key, Integer.parseInt(Value));
	}
}

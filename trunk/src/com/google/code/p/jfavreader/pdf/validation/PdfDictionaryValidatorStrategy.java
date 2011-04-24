package com.google.code.p.jfavreader.pdf.validation;

import java.util.Map;
import java.util.regex.*;

import com.google.code.p.jfavreader.pdf.engine.PdfConstants;
import com.google.code.p.jfavreader.pdf.objects.AbstractPdfDocumentObject;

public class PdfDictionaryValidatorStrategy {
	public static AbstractPdfDictionaryValidator GetStrategyFor(String key,
			Map<String, Object> dictionary,
			Map<Integer, AbstractPdfDocumentObject> contentObjects) {
		Object value = dictionary.get(key);
		if (value == null)
			return new PdfDictionaryNullValidator();
		if (value instanceof Map<?, ?>)
			return new PdfDictionarySubdictionaryValidator(
					(Map<String, Object>) value, contentObjects);
		if (!(value instanceof String))
			return new PdfDictionaryNullValidator();
		String stringValue = ((String) value).trim();
		if (Pattern.matches(PdfConstants.Integer.PATTERN, stringValue))
			return new PdfDictionaryIntegerValidator(dictionary, key,
					stringValue);
		if (Pattern.matches(PdfConstants.Object.REF_PATTERN, stringValue))
			return new PdfDictionaryObjectReferenceValidator(dictionary, key,
					stringValue, contentObjects);
		if (Pattern.matches(PdfConstants.Array.PATTERN, stringValue)) {
			final boolean matches = Pattern.matches(PdfConstants.Array.OBJECTS_PATTERN, stringValue);
			if (matches)
				return new PdfDictionaryArrayOfObjectsValidator(dictionary,
						key, stringValue, contentObjects);
		}
		return new PdfDictionaryNullValidator();
	}
}

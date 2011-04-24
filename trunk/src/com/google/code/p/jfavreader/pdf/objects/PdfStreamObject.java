package com.google.code.p.jfavreader.pdf.objects;

import java.io.InputStream;
import java.util.Map;
import com.google.code.p.jfavreader.pdf.engine.*;

public class PdfStreamObject extends PdfDictionaryObject {
	public PdfStreamObject(int id,
			Map<String, Object> dictionary, long streamPosition) {
		super(id, dictionary);
		StreamPosition = streamPosition;
	}

	public PdfScalarObject Length;

	public long StreamPosition;

	public InputStream Stream;

	public void Validate(Map<Integer, AbstractPdfDocumentObject> pdfObjects)
			throws PdfException {
		super.Validate(pdfObjects);
		if (!dictionary.containsKey(PdfConstants.Names.Length))
			throw new PdfException("Stream Object doesn't contain Length entry");
		Object value = dictionary.get(PdfConstants.Names.Length);
		if (value instanceof PdfScalarObject)
			Length = (PdfScalarObject) value;
		else if (value instanceof Integer)
			Length = new PdfScalarObject(0, (Integer) value);
		else if (value instanceof Long)
			Length = new PdfScalarObject(0, (Long) value);
		else
			throw new PdfException("Length entry type was not recognized");
	}

	public String toString() {
		long length = Length == null ? 0 : Length.Value;
		return String.format("%s|%d|Length:%d|Stream position:%d", "Stream", Id, length, StreamPosition);
	}
}

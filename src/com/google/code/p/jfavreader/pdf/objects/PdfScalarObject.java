package com.google.code.p.jfavreader.pdf.objects;

public class PdfScalarObject extends AbstractPdfDocumentObject {
	public PdfScalarObject(int id, long number) {
		super(id);
		Value = number;
	}

	public long Value;

	public String toString() {
		return String.format("%s|%d|Value:%d", "Scalar", Id, Value);
	}
}

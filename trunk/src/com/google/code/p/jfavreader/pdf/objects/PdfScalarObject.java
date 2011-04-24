package com.google.code.p.jfavreader.pdf.objects;

public class PdfScalarObject extends AbstractPdfDocumentObject {
	public PdfScalarObject(int id, long position, long number) {
		super(id, position);
		Value = number;
	}

	public long Value;

	public String toString() {
		return String.format("%s|%s|%d|Value:%d", "Scalar", Id, Position, Value);
	}
}

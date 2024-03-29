package com.google.code.p.jfavreader.pdf.objects;

import java.util.*;

import com.google.code.p.jfavreader.pdf.engine.PdfException;
import com.google.code.p.jfavreader.pdf.engine.PdfConstants;

public class PdfPagesObject extends PdfDictionaryObject {
	public PdfPagesObject(int id, Map<String, Object> dictionary) {
		super(id, dictionary);
		Kids = new ArrayList<PdfDictionaryObject>();
	}

	public List<PdfDictionaryObject> Kids;

	public void Validate(Map<Integer, AbstractPdfDocumentObject> pdfObjects) throws PdfException{
            super.Validate(pdfObjects);
            if(!dictionary.containsKey(PdfConstants.Names.Kids))
                throw new PdfException("Pages Object doesn't contain Kids collection");
            Kids = new ArrayList<PdfDictionaryObject>();
            final Object object = dictionary.get(PdfConstants.Names.Kids);
			for (AbstractPdfDocumentObject pdfObject : (List<AbstractPdfDocumentObject>)object)
				Kids.add((PdfDictionaryObject)pdfObject);
        }

	public String toString() {
		return String.format("%s|%d|Kids:%d", "Pages", Id, Kids.size());
	}
}

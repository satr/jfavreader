package com.google.code.p.jfavreader.pdf.objects;

import java.util.Map;
import com.google.code.p.jfavreader.pdf.engine.PdfException;
import com.google.code.p.jfavreader.pdf.validation.PdfDictionaryValidator;

    public class PdfDictionaryObject extends AbstractPdfDocumentObject{
        public PdfDictionaryObject(int id, long position, Map<String, Object> dictionary){ 
        	super(id, position);
            this.dictionary = dictionary;
        }

        public Map<String, Object> dictionary;

        public void Validate(Map<Integer, AbstractPdfDocumentObject> pdfObjects) throws PdfException{
            PdfDictionaryValidator.Validate(dictionary, pdfObjects);
        }
    }

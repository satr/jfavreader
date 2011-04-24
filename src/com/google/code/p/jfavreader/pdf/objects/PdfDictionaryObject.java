package com.google.code.p.jfavreader.pdf.objects;

import java.util.Map;
import com.google.code.p.jfavreader.pdf.engine.PdfException;
import com.google.code.p.jfavreader.pdf.validation.PdfDictionaryValidator;

    public class PdfDictionaryObject extends AbstractPdfDocumentObject{
        public PdfDictionaryObject(int id, Map<String, Object> dictionary){ 
        	super(id);
            this.dictionary = dictionary;
        }

        public Map<String, Object> dictionary;

        public void Validate(Map<Integer, AbstractPdfDocumentObject> pdfObjects) throws PdfException{
            PdfDictionaryValidator.Validate(dictionary, pdfObjects);
        }
    }

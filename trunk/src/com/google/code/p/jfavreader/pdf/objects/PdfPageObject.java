package com.google.code.p.jfavreader.pdf.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.code.p.jfavreader.pdf.engine.PdfConstants;
import com.google.code.p.jfavreader.pdf.engine.PdfException;


    public class PdfPageObject extends PdfDictionaryObject{
        public PdfPageObject(int id, long position, Map<String, Object> dictionary) { 
        	super(id, position, dictionary);
        }

        public List<PdfDictionaryObject> Content;

        public void Validate(Map<Integer, AbstractPdfDocumentObject> pdfObjects) throws PdfException {
            super.Validate(pdfObjects);
            if (!dictionary.containsKey(PdfConstants.Names.Contents))
                throw new PdfException("Page Object doesn't contain Content Object reference");
            Content = new ArrayList<PdfDictionaryObject>();
            Object content = dictionary.get(PdfConstants.Names.Contents);
            if (content instanceof PdfDictionaryObject)
                Content.add((PdfDictionaryObject)content);
            else if(content instanceof List<?>){
            	for (AbstractPdfDocumentObject pdfObject : (List<AbstractPdfDocumentObject>)content)
            		Content.add((PdfDictionaryObject) pdfObject);
            }
        }

        public String toString() {
            return String.format("%s|%s|%d|Content:({3})", "Page", Id, Position, Content);
        }
    }

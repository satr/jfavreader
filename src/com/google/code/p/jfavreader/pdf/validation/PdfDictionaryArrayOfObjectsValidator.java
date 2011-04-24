package com.google.code.p.jfavreader.pdf.validation;

import java.util.Map;

import com.google.code.p.jfavreader.pdf.engine.PdfEntityParser;
import com.google.code.p.jfavreader.pdf.objects.AbstractPdfDocumentObject;


    public class PdfDictionaryArrayOfObjectsValidator extends AbstractPdfDictionaryValidator{
        private String Value;
        private Map<Integer, AbstractPdfDocumentObject> ContentObjects;

        public PdfDictionaryArrayOfObjectsValidator(Map<String, Object> dictionary, String key, String value, Map<Integer, AbstractPdfDocumentObject> contentObjects){
        	super(dictionary, key);
            Value = value;
            this.ContentObjects = contentObjects;
        }

        public void Validate(){
            dictionary.put(Key, PdfEntityParser.GetArrayOfObject(Value, ContentObjects));
        }
    }

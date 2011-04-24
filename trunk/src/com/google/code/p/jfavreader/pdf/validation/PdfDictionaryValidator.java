package com.google.code.p.jfavreader.pdf.validation;

import java.util.Map;

import com.google.code.p.jfavreader.pdf.objects.AbstractPdfDocumentObject;

    public final class PdfDictionaryValidator{
        public static void Validate(Map<String, Object> dictionary, Map<Integer, AbstractPdfDocumentObject> pdfObjects) {
        	for (String key : dictionary.keySet()) 
                PdfDictionaryValidatorStrategy.GetStrategyFor(key, dictionary, pdfObjects).Validate();
        }
    }

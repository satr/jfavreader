package com.google.code.p.jfavreader.pdf.objects;

import java.util.Map;

    public class PdfFontObject extends PdfDictionaryObject{
        public PdfFontObject(int id, Map<String, Object> dictionary) { 
        	super(id, dictionary);
        }
        
        public String toString() {
            return String.format("%s|%d", "Font", Id);
        }
    }

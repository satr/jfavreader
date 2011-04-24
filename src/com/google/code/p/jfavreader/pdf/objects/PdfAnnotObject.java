package com.google.code.p.jfavreader.pdf.objects;

import java.util.Map;


    public class PdfAnnotObject extends PdfDictionaryObject{
        public PdfAnnotObject(int id, long position, Map<String, Object> dictionary) { 
        	super(id, position, dictionary);
        }
        
        public String toString() {
            return String.format("%s|%s|%d", "Annot", Id, Position);
        }
    }

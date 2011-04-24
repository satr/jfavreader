package com.google.code.p.jfavreader.pdf.objects;

import java.util.Map;

import com.google.code.p.jfavreader.pdf.engine.PdfConstants;
import com.google.code.p.jfavreader.pdf.engine.PdfException;


    public class PdfCatalogObject extends PdfDictionaryObject {
        public PdfCatalogObject(int id, long position, Map<String, Object> dictionary){ 
            super(id, position, dictionary);
        }

        public PdfPagesObject Pages;

        public void Validate(Map<Integer, AbstractPdfDocumentObject> pdfObjects) throws PdfException{
            super.Validate(pdfObjects);

            //            if(!dictionary.containsKey(PdfConstants.Names.OpenAction))
            //                throw new PdfException("Catalog Object doesn't contain OpenAction entry");
            //            OpenActionObject = //TODO - optional?
            if(!dictionary.containsKey(PdfConstants.Names.Pages))
                throw new PdfException("Catalog Object doesn't contain Pages entry");
            if(!(dictionary.get(PdfConstants.Names.Pages) instanceof PdfPagesObject))
                throw new PdfException("Catalog Object contain an invalid type of a Pages reference Object");
            Pages = (PdfPagesObject)dictionary.get(PdfConstants.Names.Pages);
        }

        public String toString() {
            String pages = Pages == null ? "" : Pages.toString();
            return String.format("%s|%s|%d|Pages:({3})", "Catalog", Id, Position, pages);
        }
    }

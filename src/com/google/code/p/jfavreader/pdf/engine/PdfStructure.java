package com.google.code.p.jfavreader.pdf.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.code.p.jfavreader.pdf.objects.AbstractPdfDocumentObject;
import com.google.code.p.jfavreader.pdf.objects.PdfCatalogObject;
import com.google.code.p.jfavreader.pdf.objects.PdfDictionaryObject;
import com.google.code.p.jfavreader.pdf.objects.PdfPagesObject;
import com.google.code.p.jfavreader.pdf.validation.PdfDictionaryValidator;

    public class PdfStructure{
        private final Object _sync = new Object();

        public PdfStructure(){
            Trailers = new ArrayList<Map<String, Object>>();
            PdfObjects = new HashMap<Integer, AbstractPdfDocumentObject>();
        }

        public List<Map<String, Object>> Trailers;

        public Map<Integer, AbstractPdfDocumentObject> PdfObjects;

        public boolean getTrailerHasPrevValue(){
           return getTrailerPrevValue() != null;
        }

        public String getTrailerPrevValue(){
        	for (Iterator iterator = Trailers.iterator(); iterator.hasNext();) {
        		Map<String, Object> trailer = (Map<String, Object>) iterator.next();
                if (trailer.containsKey(PdfConstants.Names.Prev))
                    return trailer.get(PdfConstants.Names.Prev).toString();
            }
            return null;
        }

        public void ValidatePdfObjects() throws PdfException{
        	for (AbstractPdfDocumentObject pdfObject : PdfObjects.values()) {
				if(pdfObject instanceof PdfDictionaryObject)
					((PdfDictionaryObject)pdfObject).Validate(PdfObjects);
			}
        	for (Map<String, Object> trailer : Trailers) 
        		PdfDictionaryValidator.Validate(trailer, PdfObjects);
            Root = GetTrailerObject(PdfConstants.Names.Root, true);
//            PopulatePages(Root.Pages);
        }

        private void PopulatePages(List<PdfPagesObject> pdfDocumentPagesObjects){
//            parentContentObject.Pages = parentContentObject.GetObject<List<PdfScalarObject>>(PdfConstants.Names.Kids);
//            foreach (var contentObject extends parentContentObject.Pages)
//                PopulatePages(contentObject);
        }

        private <T> T GetTrailerObject(String key, boolean isMandatory) throws PdfException{
        	for (Map<String, Object> dictionary : Trailers) {
                if (dictionary.containsKey(key))
                    return (T)dictionary.get(key);
            }
            if (isMandatory)
                throw new PdfException("Entity not found extends trailer by key \"%s\"", key);
            return null;
        }

        private PdfCatalogObject Root;

    }

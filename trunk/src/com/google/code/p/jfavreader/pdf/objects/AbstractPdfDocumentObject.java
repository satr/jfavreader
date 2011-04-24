package com.google.code.p.jfavreader.pdf.objects;

    public abstract class AbstractPdfDocumentObject{
        public int Id;

        protected AbstractPdfDocumentObject(int id) {
            Id = id;
        }

        public String toString(){
            return String.format("%s|%d", "Base", Id);
        }
    }

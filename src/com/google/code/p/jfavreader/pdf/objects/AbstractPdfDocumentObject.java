package com.google.code.p.jfavreader.pdf.objects;

    public abstract class AbstractPdfDocumentObject{
        public int Id;
        public long Position;

        protected AbstractPdfDocumentObject(int id, long position) {
            Id = id;
            Position = position;
        }

        public String toString(){
            return String.format("%s|%s|%d", "Base", Id, Position);
        }
    }

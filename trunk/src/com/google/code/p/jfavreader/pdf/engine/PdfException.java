package com.google.code.p.jfavreader.pdf.engine;

    public class PdfException extends Exception {
		private static final long serialVersionUID = -3644194646384411111L;

		public PdfException(String format, Object[] args) { 
        	super(String.format(format, args));
        }

		public PdfException(String message) { 
        	this(message, new String[0]);
        }

		public PdfException(String message, Object arg0) { 
        	this(message, new Object[]{arg0});
        }

		public PdfException(String message, Object arg0, Object arg1) { 
        	this(message, new Object[]{arg0, arg1});
        }
}

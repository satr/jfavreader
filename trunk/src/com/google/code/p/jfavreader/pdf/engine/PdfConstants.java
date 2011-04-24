package com.google.code.p.jfavreader.pdf.engine;

public class PdfConstants {
	public static class Patterns {
		public static String EndOfLine = "[\r\n]";
	}

	public static class XRef {
		public static String Row = "^\\d{10} \\d{5} [fn][ ]*$";
		public static String Counter = "^\\d+ \\d+$";
	}

	public static class Markers {
		public static String Percent = "%";
		public static String Obj = " obj";
		public static String EndObj = "endobj";
		public static String Stream = "stream";
		public static String EndStream = "endstream";
		public static String StartDictionary = "<<";
		public static String EndDictionary = ">>";
		public static String STARTXREF = "startxref";
		public static String XREF = "xref";
		public static String TRAILER = "trailer";
	}

	public static class Names {
		public static String Type = "/Type";
		public static String Prev = "/Prev";
		public static String Root = "/Root";
		public static String Catalog = "/Catalog";
		public static String Pages = "/Pages";
		public static String Page = "/Page";
		public static String Count = "/Count";
		public static String Kids = "/Kids";
		public static String Length = "/Length";
		public static String OpenAction = "/OpenAction";
		public static String Contents = "/Contents";
		public static String Font = "/Font";
		public static String FontDescriptor = "/FontDescriptor";
		public static String Annot = "/Annot";
	}

	public static class dictionary {
		public static int KEY_GROUP = 2;
		public static int VALUE_GROUP = 3;
		public static String PATTERN = "((/\\w+)(([ ]*\\[[^\\]]+\\])|([ ]*[^\\[^\\w][^/^\\]]+))[\n\r]{0,1})";
	}

	public static class Integer {
		public static String PATTERN = "\\A[ ]*\\d+[ \r\n]*$";
	}

	public static class Array {
		public static String PATTERN = "\\A\\[[^\\]]*\\]$";
		public static String OBJECTS_PATTERN = "[ ]*\\[([ ]*\\d+ \\d+ R[ \r\n]*)+\\][ \r\n]*";
	}

	public static class Object {
		public static int GROUP_ID = 1;
		public static int GROUP_VAL = 2;
		public static int GROUP_TYPE = 3;
		public static String PATTERN = "\\d+ \\d+ obj";
		public static String REF_PATTERN = "(\\d+) (\\d+) (R)";
		public static String REF_TYPE = "R";
	}
}

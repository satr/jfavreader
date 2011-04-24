package com.google.code.p.jfavreader.pdf.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.code.p.jfavreader.pdf.objects.AbstractPdfDocumentObject;

public class PdfEntityParser {
	public static AbstractPdfDocumentObject GetObjectByRef(String value, Map<Integer, AbstractPdfDocumentObject> contentObjects) {
		Matcher matcher = Pattern.compile(PdfConstants.Object.REF_PATTERN).matcher(value);
		if(!matcher.find() || matcher.groupCount() != 3)
			return null;
		return GetObjectByRef(matcher.group(PdfConstants.Object.GROUP_ID), 
							 matcher.group(PdfConstants.Object.GROUP_VAL),
							 matcher.group(PdfConstants.Object.GROUP_TYPE), 
							 contentObjects);
	}

	private static AbstractPdfDocumentObject GetObjectByRef(String idValue,
			String valValue, String typeValue,
			Map<Integer, AbstractPdfDocumentObject> contentObjects) {
		int ObjectId = Integer.parseInt(idValue);
		return typeValue.equalsIgnoreCase(PdfConstants.Object.REF_TYPE) 
			   && contentObjects.containsKey(ObjectId) 
				? contentObjects.get(ObjectId) 
				: null;
	}

	public static List<AbstractPdfDocumentObject> GetArrayOfObject(
			String value, Map<Integer, AbstractPdfDocumentObject> contentObjects) {
		List<AbstractPdfDocumentObject> refContentObjects = new ArrayList<AbstractPdfDocumentObject>();
		Matcher matcher = Pattern.compile(PdfConstants.Object.REF_PATTERN).matcher(value);
		while (matcher.find()) {
			if (matcher.groupCount() == 3) {
				AbstractPdfDocumentObject contentObject = GetObjectByRef(matcher.group(PdfConstants.Object.GROUP_ID), 
																		 matcher.group(PdfConstants.Object.GROUP_VAL),
																		 matcher.group(PdfConstants.Object.GROUP_TYPE), 
																		 contentObjects);
				if (contentObject != null)
					refContentObjects.add(contentObject);
			}
		}
		return refContentObjects;
	}

	public static long GetXRefOffset(String value) {
		long numVal = Long.parseLong(value.substring(0, value.indexOf(" ")));
		return numVal;
	}

	public static boolean IsXRefOffsetUsed(String value) {
		return value.trim().toLowerCase().endsWith("n");
	}

	public static int GetXRefStartObjectNum(String value) {
		int numVal = Integer.parseInt(value.substring(0, value.indexOf(" ")));
		return numVal;
	}

	public static int GetXRefObjectCount(String value) {
		int startIndex = value.indexOf(" ") + 1;
		int numVal = Integer.parseInt(value.substring(startIndex,value.length()));
		return numVal;
	}
}

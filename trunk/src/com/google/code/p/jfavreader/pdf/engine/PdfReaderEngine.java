package com.google.code.p.jfavreader.pdf.engine;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.System;
import java.io.*;
import java.io.ObjectInputStream.GetField;

import com.google.code.p.jfavreader.pdf.objects.*;




    public class PdfReaderEngine {
        final Pattern _xrefObjectCounterPattern = Pattern.compile(PdfConstants.XRef.Counter);
        final Pattern _xrefOffsetPattern = Pattern.compile(PdfConstants.XRef.Row);
        final Pattern _xrefIntegerPattern = Pattern.compile(PdfConstants.Integer.PATTERN);
        final Pattern _endOfLinePattern = Pattern.compile(PdfConstants.Patterns.EndOfLine);
        final Pattern _startOfObjectPattern = Pattern.compile(PdfConstants.Object.PATTERN);
        final Pattern dictionaryPattern = Pattern.compile(PdfConstants.dictionary.PATTERN);
        private String fileName;

        public  RandomAccessFile getReader() throws IOException{
            if(!getFileExists())
            	return null;
            return new RandomAccessFile(getFileName(), "r");
        }

        public boolean getFileExists(){
            return new File(getFileName()).exists();
        }

        public PdfStructure LoadPdfStructure() throws PdfException, IOException{
        	PdfStructure pdfStructure = new PdfStructure();
            if(!getFileExists())
                return pdfStructure;
            ReadPdfDocument(pdfStructure, getReader());
            pdfStructure.ValidatePdfObjects();
            return pdfStructure;
        }

        private void ReadPdfDocument(PdfStructure pdfStructure, RandomAccessFile reader) throws IOException, PdfException{
        	Map<Integer, Long> ObjectOffsets = GetObjectOffsets(pdfStructure, reader);
            pdfStructure.PdfObjects = GetPdfObjects(ObjectOffsets, reader);
        }

        private Map<Integer, AbstractPdfDocumentObject> GetPdfObjects(Map<Integer, Long> ObjectOffsets, RandomAccessFile reader) throws IOException, PdfException{
            Map<Integer, AbstractPdfDocumentObject> Objects = new HashMap<Integer, AbstractPdfDocumentObject>();
            for (Entry<Integer, Long> keyValuePair : ObjectOffsets.entrySet()) {
                int ObjectId = keyValuePair.getKey();
                long ObjectPosition = keyValuePair.getValue();
                Objects.put(ObjectId, ReadPdfDocumentObject(ObjectId, ObjectPosition, reader));
            }
            return Objects;
        }

        private AbstractPdfDocumentObject ReadPdfDocumentObject(int ObjectId, long ObjectPosition, RandomAccessFile reader) throws IOException, PdfException{
            reader.seek(ObjectPosition);
            String line = reader.readLine();
            if(line == null || !_startOfObjectPattern.matcher(line).matches())
                throw new PdfException("Invalid format for Object #%d", ObjectId);
            line = line.endsWith(PdfConstants.Markers.Obj) 
                    ? reader.readLine() 
                    : _startOfObjectPattern.matcher(line).replaceAll("");

            Map<String, Object> dictionary = new HashMap<String, Object>();
            do{
                if (_xrefIntegerPattern.matcher(line).matches())
                    return new PdfScalarObject(ObjectId, Long.parseLong(line));
                if (line.equals(PdfConstants.Markers.EndObj)){
                    if (ValidateType(PdfConstants.Names.Pages, dictionary))
                        return new PdfPagesObject(ObjectId, dictionary);
                    if (ValidateType(PdfConstants.Names.Page, dictionary))
                        return new PdfPageObject(ObjectId, dictionary);
                    if (ValidateType(PdfConstants.Names.Catalog, dictionary))
                        return new PdfCatalogObject(ObjectId, dictionary);
                    if (ValidateType(PdfConstants.Names.Font, dictionary))
                        return new PdfFontObject(ObjectId, dictionary);
                    if (ValidateType(PdfConstants.Names.FontDescriptor, dictionary))
                        return new PdfFontDescriptorObject(ObjectId, dictionary);
                    if (ValidateType(PdfConstants.Names.Annot, dictionary))
                        return new PdfFontDescriptorObject(ObjectId, dictionary);
                    return new PdfDictionaryObject(ObjectId, dictionary);
                    //                    throw new PdfException("Unexpected end of a PDF-Object");
                }
                if (line.startsWith(PdfConstants.Markers.StartDictionary))
                    line = ReadPdfDictionary(reader, line, dictionary);
                if (line.endsWith(PdfConstants.Markers.Stream))
                    return new PdfStreamObject(ObjectId, dictionary, reader.getFilePointer());
            } while ((line = reader.readLine()) != null);
            throw new PdfException("End of PDF-Object was not found");
        }

        private String ReadPdfDictionary(RandomAccessFile reader, String startLine, Map<String, Object> dictionary) throws IOException{
            String line = startLine;
            if (line == null)
                return line;
            StringBuilder StringBuilder = new StringBuilder();
            boolean startLineContainsEnddictionaryAndStartStream = line.contains(PdfConstants.Markers.EndDictionary + PdfConstants.Markers.Stream);
            StringBuilder.append(startLineContainsEnddictionaryAndStartStream
                                  ? line.substring(0, line.indexOf(PdfConstants.Markers.Stream))
                                  : line);
            StringBuilder.append("\n");
            if (!line.endsWith(PdfConstants.Markers.EndDictionary)
                && !startLineContainsEnddictionaryAndStartStream) {
                for(;;){
                    if (line.contains(PdfConstants.Markers.EndDictionary + PdfConstants.Markers.Stream)){
                        int streamIndex = line.indexOf(PdfConstants.Markers.Stream);
                        StringBuilder.append(String.format("%s\n", line.substring(0, streamIndex)));
                        line = line.substring(streamIndex);
                        break;
                    }
                    if ((line = reader.readLine()) == null)
                        break;
                    StringBuilder.append(String.format("%s\n", line));
                    if (line.endsWith(PdfConstants.Markers.EndDictionary))
                        break;
                }
            }
            String dictionaryText = StringBuilder.toString();
            Matcher matcher = dictionaryPattern.matcher(dictionaryText);
            AddDictionaryEntries(dictionary, matcher);
            return line;
        }

        private void AddDictionaryEntries(Map<String, Object> dictionary, Matcher matcher){
            while(matcher.find()) {
            	String key = matcher.group(PdfConstants.dictionary.KEY_GROUP);
                String value = matcher.group(PdfConstants.dictionary.VALUE_GROUP);
                value = _endOfLinePattern.matcher(value).replaceAll("");
                if (!value.equals(PdfConstants.Markers.StartDictionary)) {
                    if (value.endsWith(PdfConstants.Markers.EndDictionary)){
                        dictionary.put(key, value.substring(0, value.length() - PdfConstants.Markers.EndDictionary.length()));
                        return;
                    }
                    dictionary.put(key, value);
                    continue;
                }
                Map<String, Object> innerDictionary = new HashMap<String, Object>();
                dictionary.put(key, innerDictionary);
                AddDictionaryEntries(innerDictionary, matcher);
            }
        }

//        private static void ReadPdfContentObjectStream(PdfScalarObject pdfContentObject, RandomAccessFile reader){
//            long startStreamPosition = reader.BaseStream.Position;
//            long endStreamPosition = startStreamPosition;
//            pdfContentObject.Stream = new MemoryStream();
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.Equals(PdfConstants.Markers.EndStream)){
//                    reader.BaseStream.Seek(startStreamPosition - endStreamPosition, SeekOrigin.Current);
//                    static int BUFFER_SIZE = 1024;
//                    var buffer = new byte[BUFFER_SIZE];
//                    while (startStreamPosition < endStreamPosition) {
//                        long bytesLeft = endStreamPosition - startStreamPosition;
//                        int bytesToRead = bytesLeft > BUFFER_SIZE ? BUFFER_SIZE extends (int)bytesLeft;
//                        var readBytes = reader.BaseStream.Read(buffer, 0, bytesToRead);
//                        pdfContentObject.Stream.Write(buffer, 0, readBytes);
//                    }
//                    break;
//                }
//                endStreamPosition = reader.BaseStream.Position;
//            }
//        }

        public Map<Integer, Long> GetObjectOffsets(PdfStructure pdfStructure, RandomAccessFile reader) throws PdfException, IOException{
        	Map<Integer, Long> ObjectOffsets = new HashMap<Integer, Long>();
            long xrefOffset = GetStartXRefOffset(reader);
            PopulateObjectOffsetsAndTrailer(pdfStructure, reader, xrefOffset, ObjectOffsets);
            if (!pdfStructure.getTrailerHasPrevValue())
                return ObjectOffsets;
            try {
				xrefOffset = Long.parseLong(pdfStructure.getTrailerPrevValue());
			} catch (NumberFormatException e) {
                throw new PdfException("Invalid \\Prev value \"%s\"", pdfStructure.getTrailerPrevValue());
			}
            PopulateObjectOffsetsAndTrailer(pdfStructure, reader, xrefOffset, ObjectOffsets);
            return ObjectOffsets;
        }

        public void PopulateObjectOffsetsAndTrailer(PdfStructure pdfStructure, RandomAccessFile reader, long xrefOffset, 
                                                    Map<Integer, Long> ObjectOffsets) throws IOException, PdfException{
            reader.seek(xrefOffset);
            String line = reader.readLine();
            if (!line.equals(PdfConstants.Markers.XREF))
                throw new PdfException("%s not found", PdfConstants.Markers.XREF);
            line = reader.readLine();
            if (line == null || !_xrefObjectCounterPattern.matcher(line).matches())
                throw new PdfException("Invalid Object ref counter");
            int ObjectOverallNum = PdfEntityParser.GetXRefStartObjectNum(line);
            int ObjectCount = PdfEntityParser.GetXRefObjectCount(line);
            int ObjectNum = 0;
            while ((line = reader.readLine()) != null && ObjectNum < ObjectCount) {
                if (_xrefObjectCounterPattern.matcher(line).matches()) {
                    ObjectOverallNum = PdfEntityParser.GetXRefStartObjectNum(line);
                    ObjectCount = PdfEntityParser.GetXRefObjectCount(line);
                    ObjectNum = 0;
                    continue;
                }
                if (_xrefOffsetPattern.matcher(line).matches()) {
                    if(PdfEntityParser.IsXRefOffsetUsed(line))
                        ObjectOffsets.put(ObjectOverallNum, PdfEntityParser.GetXRefOffset(line));
                    ObjectOverallNum++;
                    ObjectNum++;
                    continue;
                }
                if (!line.startsWith(PdfConstants.Markers.TRAILER))
                    throw new PdfException("%s was expected", PdfConstants.Markers.TRAILER);
                break;
            }
            line = reader.readLine();
            Map<String, Object> dictionary = new HashMap<String, Object>();
            ReadPdfDictionary(reader, line, dictionary);
            pdfStructure.Trailers.add(dictionary);
        }

        private long GetStartXRefOffset(RandomAccessFile reader) throws IOException, PdfException{
        	final long fileLength = getFile().length();
			long position = fileLength - 100;
			if(position <= 0)
				throw new PdfException("Invalid PDF file size");
            reader.seek(position);
            String line;
            while ((line = reader.readLine()) != null
                    && !line.equals(PdfConstants.Markers.STARTXREF))
                    continue;
            if (!line.equals(PdfConstants.Markers.STARTXREF))
                throw new PdfException(String.format("{0} value not found", PdfConstants.Markers.STARTXREF));
            return ParseLong(reader.readLine(), PdfConstants.Markers.STARTXREF);
        }
        
        private static long ParseLong(String value, String valueTitle) throws PdfException{
			try {
				return Long.parseLong(value);
			} catch (NumberFormatException e) {
                throw new PdfException(String.format("Invalid number format of value \"%s\"", valueTitle));
			}
        }

        private boolean ValidateType(String type, Map<String, Object> dictionary) throws PdfException {
            final Object object = GetObject(PdfConstants.Names.Type, dictionary);
			return object != null && object.toString().equalsIgnoreCase(type);
        }

        public <T> T GetObject(String key, Map<String, Object> dictionary) throws PdfException {
            return GetObject(key, dictionary, false);
        }

        public <T> T GetMandaryObject(String key, Map<String, Object> dictionary) throws PdfException {
            return GetObject(key, dictionary, true);
        }

        private static <T> T GetObject(String key, Map<String, Object> dictionary, boolean isMandatory) throws PdfException {
            if (dictionary.containsKey(key))
                return (T)dictionary.get(key);
            if (isMandatory)
                throw new PdfException("Entity not found by key \"%s\"", key);
            return null;
        }

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return fileName;
		}

		public File getFile() {
			return new File(fileName);
		}
    }

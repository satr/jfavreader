package com.google.code.p.jfavreader.pdf.validation;

import java.util.Map;

    public abstract class AbstractPdfDictionaryValidator {
        protected Map<String, Object> dictionary;
        protected String Key;

        protected AbstractPdfDictionaryValidator(Map<String, Object> dictionary, String key) {
            this.dictionary = dictionary;
            Key = key;
        }

        public abstract void Validate();
    }

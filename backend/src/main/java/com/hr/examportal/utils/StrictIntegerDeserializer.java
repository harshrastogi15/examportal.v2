package com.hr.examportal.utils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import java.io.IOException;

public class StrictIntegerDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.getCurrentToken();

        // Accept only integer numbers, reject others
        if (token.isNumeric()) {
            if (token == JsonToken.VALUE_NUMBER_INT) {
                return p.getIntValue();
            } else {
                // It's a number but not integer (e.g. float/double)
                throw InvalidFormatException.from(p, "Floating point number not allowed for Integer field", p.getText(), Integer.class);
            }
        }

        // Reject strings, booleans, nulls, etc.
        throw InvalidFormatException.from(p, "Expected integer number for Integer field", p.getText(), Integer.class);
    }
}
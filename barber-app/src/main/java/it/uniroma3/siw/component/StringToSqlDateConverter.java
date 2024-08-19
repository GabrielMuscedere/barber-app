package it.uniroma3.siw.component;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class StringToSqlDateConverter implements Converter<String, java.sql.Date> {
    @Override
    public java.sql.Date convert(String source) {
        try {
            return java.sql.Date.valueOf(source);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

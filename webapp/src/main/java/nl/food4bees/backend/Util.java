package nl.food4bees.backend;

import java.awt.Color;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import java.util.regex.Pattern;

import javax.servlet.http.Part;

import org.apache.commons.io.IOUtils;

public class Util
{
    private static final Pattern INTEGER_PATTERN = Pattern.compile("-?\\p{Digit}+");
    private static final Pattern DOUBLE_PATTERN = Pattern.compile("[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                                                                  "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                                                                  "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                                                                  "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");

    static public boolean isInteger(String s)
    {
        if (s == null) {
            return false;
        }
        return INTEGER_PATTERN.matcher(s).matches();
    }

    static public boolean isFloat(String s)
    {
        if (s == null) {
            return false;
        }
        return DOUBLE_PATTERN.matcher(s).matches();
    }


    static public String trim(String value)
    {
        if (value == null) {
            return null;
        }
        if (value.trim().length() == 0) {
            return null;
        }
        return value;
    }

    static public Integer parseInteger(String value)
    {
        return parseInteger(value, 10);
    }

    static public Integer parseInteger(String value, int base)
    {
        value = Util.trim(value);
        if (!Util.isInteger(value)) {
            return null;
        }
        return Integer.parseInt(value, base);
    }

    static public Double parseDouble(String value)
    {
        value = Util.trim(value);
        if (value == null) {
            return null;
        }

        return Double.parseDouble(value);
    }

    static public Date parseDate(String value) throws ParseException
    {
        value = Util.trim(value);
        if (value == null) {
            return null;
        }

        DateFormat dateFormat = new SimpleDateFormat("dd-MM");
        return dateFormat.parse(value);
    }

    static public String streamToString(InputStream inputStream) throws IOException
    {
        StringWriter writer = new StringWriter();
        IOUtils.copy(inputStream, writer);
        return writer.toString();
    }
}

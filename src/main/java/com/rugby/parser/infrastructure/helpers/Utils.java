package com.rugby.parser.infrastructure.helpers;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Locale;

public class Utils {

    /**
     * Given a text, it returns a substring of it form the supplied index and the number of words expected.
     * @param originalArray
     * @param index where to start parsing words
     * @param wordsNumber number of words wanted
     * @return the text found or an empty string if nothing found
     */
    public static String getWordsAtIndex(String[] originalArray, int index, int wordsNumber) {
        if (originalArray == null || originalArray.length == 0 || originalArray.length < index)
            return "";

        String[] words = Arrays.copyOfRange(originalArray, 0, index);

        StringBuilder stringBuilder = new StringBuilder();
        if (words.length >= wordsNumber) {
            for (int i = wordsNumber; i >= 1; i--) {
                stringBuilder.append(words[words.length-i]).append(" ");
            }
        }

        return stringBuilder.toString().trim();
    }

    public static LocalDate formatFrenchDate(String date) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("dd MMMM"))
                .toFormatter(Locale.FRENCH);

        if (date.substring(0, 2).trim().length() == 1)
            date = "0"+date;

        return MonthDay.parse(date, formatter).atYear(LocalDate.now().getYear());
    }

    public static LocalDate formatUkDate(String date) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(DateTimeFormatter.ofPattern("dd MMMM"))
                .toFormatter(Locale.UK);

        date = date.replace("st ", " ").replace("rd ", " ").replace("nd ", " ").replace("th ", " ");
        if (date.substring(0, 2).trim().length() == 1)
            date = "0"+date;

        return MonthDay.parse(date, formatter).atYear(LocalDate.now().getYear());
    }
}

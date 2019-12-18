/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.time.format.DateTimeFormatter;

/**
 *
 * @author Duc
 */
public class Datetime {
    public static DateTimeFormatter formatter;

    public static DateTimeFormatter getFormatter() {
        return formatter;
    }

    public static void setFormatter(DateTimeFormatter formatter) {
        Datetime.formatter = formatter;
    }
}

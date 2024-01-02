package huce.fit.mvvmpattern.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BanNT
 */

public class ValidatorUtil {
    public static boolean emptyValue(String str) {
        if (null == str || str.equals("")) {
            return true;
        }
        return false;
    }
}

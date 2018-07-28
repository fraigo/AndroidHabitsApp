package me.franciscoigor.habits.base;

public class StringHelper {

    public static String upperCaseFirst(String text){
        if (text == null ) {
            return null;
        }
        if (text.length()==1) {
            return text.toUpperCase();
        }
        return Character.toString(text.charAt(0)).toUpperCase()+text.substring(1);
    }
}

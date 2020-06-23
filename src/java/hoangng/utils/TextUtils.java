/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hoangng.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author popem
 */
public class TextUtils {
    
    public static String refineHtml(String src) {
        src = getBody(src);
        src = removeMiscTags(src);
        
        XMLWellformChecker checker = new XMLWellformChecker();
        src = checker.check(src);
        
        src = getBody(src);
        return src;
    }
    
    private static String getBody(String src) {
        String result = src;
        
        String expression = "<body.*?</body>";
        Pattern pattern = Pattern.compile(expression);
        
        Matcher matcher = pattern.matcher(result);
        
        if (matcher.find()) {
            result = matcher.group(0);
        }
        
        return result;
    }
    
    public static String removeMiscTags(String src) {
        String result = src;
        
        String expression = "<script.*?</script>";
        result = result.replaceAll(expression, "");
        
        expression = "<!--.*?-->";
        result = result.replaceAll(expression, "");
        
        expression = "&nbsp;?";
        result = result.replaceAll(expression, "");
        
        return result;
    }
    
    
}

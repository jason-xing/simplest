package org.projectsample.simplest.s1.web.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Jason Xing
 */
public class Resource {
    
    private static Map<Locale, ResourceBundle> rbMap = new HashMap<Locale, ResourceBundle>();
    
    public static String get(Locale locale, String key) {
        ResourceBundle rb = null;
        if (rbMap.containsKey(locale)) {
            rb = rbMap.get(locale);
        } else {
            rb = ResourceBundle.getBundle("i18n/resource", locale);
            if (rb != null) {
                rbMap.put(locale, rb);
            }
        }
        if (rb == null) {
            return null;
        }
        return rb.getString(key);
    }
    
}

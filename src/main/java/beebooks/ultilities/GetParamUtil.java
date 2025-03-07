package beebooks.ultilities;

import javax.servlet.http.HttpServletRequest;

public class GetParamUtil {
    public static String getStringParam(HttpServletRequest request, String field, String label, int min, int max, String defaultValue) {
        String value = request.getParameter(field);
        if (value == null || value.trim().isEmpty()) {
            if(defaultValue == null){
                request.setAttribute(field + " Error", label +" is required");
                return null;
            }
            return defaultValue;
        }
        if (value.trim().length() > max) {
            request.setAttribute(field + " Error", label + " is less than or equal " + max + " characters");
            return null;
        }
        if (value.trim().length() < min) {
            request.setAttribute(field + " Error", label + " is greater than or equal " + min + " characters");
            return null;
        }
        return value.trim();
    }
}

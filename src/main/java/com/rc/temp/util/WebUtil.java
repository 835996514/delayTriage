package com.rc.temp.util;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WebUtil {

    public static String renderString(HttpServletResponse response,String string){
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        try {
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

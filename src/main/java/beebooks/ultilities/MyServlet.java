package beebooks.ultilities;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Tạo danh sách năm
        ArrayList<String> years = new ArrayList<String>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= 1900; i--) {
            years.add(Integer.toString(i));
        }

        // Đưa danh sách năm vào request attribute
        request.setAttribute("years", years);

        // Forward tới trang HTML
        RequestDispatcher dispatcher = request.getRequestDispatcher("your_page.html");
        dispatcher.forward(request, response);
    }

    public static void main(String[] args) {
        String input = "duchieu1370@gmail.com";
        String regex = "^[A-Za-z0-9]{6,32}@([a-zA-Z0-9]{2,12})(.[a-zA-Z]{2,12})+$";

        if (validateEmail(input, regex)) {
            System.out.println("Email is valid");
        } else {
            System.out.println("Email is invalid");
        }
    }

    public static boolean validateEmail(String email, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}

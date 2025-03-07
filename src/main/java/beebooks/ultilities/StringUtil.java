package beebooks.ultilities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StringUtil {
    public static String generateOrderCode() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String dateStr = dateFormat.format(new Date());

        Random random = new Random();
        int randomNumber = 10000 + random.nextInt(90000);

        return String.valueOf(randomNumber) + dateStr;
    }
}

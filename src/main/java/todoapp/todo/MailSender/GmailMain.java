package todoapp.todo.MailSender;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GmailMain {
    private static String to;
    private static String generatedCode;
    private static long codeTimestamp;

    private static Set<String> usedCodes = new HashSet<>();
    private static long validationPeriod = 5 * 60 * 1000;
    // set validation period as you need it

    public static boolean initialize() throws MessagingException {
        if (sendEmail()) {
            CodeStore.storeCodeInDatabase(to, generatedCode);
            return true;
        }
        return false;
    }


    public static void setEmail(String extractedEmail) {
        to = extractedEmail;
    }

    public static void setValidationPeriod(long period) {
        validationPeriod = period;
    }

    public static void generateCode() {
        Random random = new Random();

        // Generate a new code until a unique one is found
        do {
            generatedCode = String.format("%04d", random.nextInt(9000) + 1000);
        } while (usedCodes.contains(generatedCode));

        // Record the timestamp when the code is generated
        codeTimestamp = System.currentTimeMillis();

        // Add the generated code to the set of used codes
        usedCodes.add(generatedCode);
    }


    public static String getGeneratedCode() {
        return generatedCode;
    }

    public static boolean sendEmail() throws MessagingException {
        GmailSender gmailSender = new GmailSender();
        generateCode();
        String from = "tododesktopapp@gmail.com";
        String subject = "Forget Password Request";
        String text = "Your verification code: " + generatedCode;

        try {
            // Check for network connectivity
            if (!isNetworkAvailable()) {
                System.out.println("No network connectivity.");
                return false;
            }

            // Attempt to send the email
            if (to != null && isValidEmail(to) && isNetworkAvailable()) {
                gmailSender.sendMail(from, to, subject, text);
                return true; // Email sent successfully
            } else {
                System.out.println("Invalid email address: " + to);
            }
        } catch (MessagingException e) {
            // Handle messaging exception
            e.printStackTrace();
            System.out.println("Error sending email: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            throw new MessagingException("Simulated MessagingException");
        }
         return false;
    }

    private static boolean isNetworkAvailable() {
        try {
            InetAddress.getByName("www.google.com");
            return true; // Network is available
        } catch (UnknownHostException e) {
            return false; // Network is not available
        }
    }




    private static boolean isValidEmail(String email) {
        try {
            InternetAddress.parse(email, true);
            return true;
        } catch (AddressException e) {
            return false;
        }
    }

    public static boolean isCodeValid() {
        long currentTime = System.currentTimeMillis();
        return currentTime - codeTimestamp <= validationPeriod;
    }

    public static String getTo() {
        return to;
    }
}

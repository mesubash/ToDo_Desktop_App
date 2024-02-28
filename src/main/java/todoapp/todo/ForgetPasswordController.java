package todoapp.todo;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import todoapp.todo.MailSender.CodeStore;
import todoapp.todo.MailSender.GmailMain;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;
import javafx.concurrent.Task;



public class ForgetPasswordController {
    static MainApp mapp;
    String htmlMailBody;

    public static void setMainApp(MainApp mainApp) {
        mapp = mainApp;
    }

    private String email;
    private static String storedCode; // Store the code retrieved from the database
    public static void setStoredCode(String code){
        storedCode=code;
    }


    public void setEmail(String text) {
        this.email = text;
    }

    @FXML
    private TextField NextEmail;
    @FXML
    private HBox afterCodeValid;

    @FXML
    private TextField NextCodeField;

    @FXML
    private Label suggestionLabel;
    @FXML
    private Button updateButton;
    @FXML
    private void gobackFromNext(){
        mapp.showForgetPasswordScene();

    }
    @FXML
    private void gobackHandle(){

        mapp.showLoginScene();
    }
    int code = 0;

    @FXML
    private Label emailErr;
     @FXML
     private Hyperlink resendCodeLink;
    @FXML
    private TextField emailField;

    @FXML
    private TextField NextPasswordField;

    @FXML
    private TextField NextCPasswordField;

    @FXML
    private Label codeValidLabel;
    @FXML
    private AnchorPane disabledAnchor;
    @FXML
    private AnchorPane visibleAnchor;
    @FXML
    private Label codeSendFail;
    @FXML
    private Label emailvalidateErr;
    public void initialize() {


        // Add a listener for the main application window's deiconified event
        mapp.getPrimaryStage().iconifiedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                // The window is deiconified, check network availability
                try {
                    checkNetwork();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
    private void checkNetwork() throws IOException {
        if (!isNetworkAvailable()) {
            DialogueController controller=new DialogueController();
            controller.showErrorDialogue(mapp.getPrimaryStage(),"Network Error",415,41,2.5);
        }
        else {
//            System.out.println("available");
        }
    }
    //to check network availability
    private static boolean isNetworkAvailable() {
        try {
            InetAddress.getByName("www.google.com");
            return true; // Network is available
        } catch (UnknownHostException e) {
            return false; // Network is not available
        }
    }
    @FXML
    private void onProceed() throws Exception {
        // Check network availability
        if (isNetworkAvailable()){
            String e=emailField.getText();
            if(!e.isEmpty()){
                boolean found = validateEmail(e);

                if (found) {
                    // Disable UI controls and show "Please wait" message
                    setUIControlsDisabled(true);
                    setPleaseWaitMessage(true);

                    Task<Boolean> sendMailTask = new Task<>() {
                        @Override
                        protected Boolean call() throws Exception {
                            setEmail(emailField.getText());
                            GmailMain.generateCode();
                            String generatedCode = GmailMain.getGeneratedCode();
                            CodeStore.storeCodeInDatabase(e, generatedCode);

                            htmlMailBody="<html><head>\n" +
                                    "    <title>Password Reset Verification</title>\n" +
                                    "</head>\n" +
                                    "\n" +
                                    "<body>\n" +
                                    "    <h2>Password Reset Verification</h2>\n" +
                                    "    <p>Dear "+getName(email)+",</p>\n" +
                                    "    <p>We have received a request to reset the password associated with your account. To proceed with the password reset, please use the following verification code:</p>\n" +
                                    "    <p><strong>Verification Code:</strong>"+generatedCode+"</p>\n" +
                                    "    <p>Please enter this code on the password reset page to verify your identity and complete the password reset process. Note that this code is valid for 5 minutes for security reasons. If the code expires, you can request a new one on the password reset page.</p>\n" +
                                    "    <p>If you did not initiate this password reset request, please disregard this email. Your account security is important to us.</p>\n" +
                                    "    <p>Thank you, <br>Subash Singh Dhami</p>\n" +
                                    "</body></html>";
                            return sendMail(htmlMailBody);
                        }
                    };

                    sendMailTask.setOnSucceeded(event -> {
                        // Enable UI controls and hide "Please wait" message
                        setUIControlsDisabled(false);
                        setPleaseWaitMessage(false);

                        // Check the result of sendMail
                        if (sendMailTask.getValue()) {
                            // Proceed to the next scene only if mail is sent successfully
                            mapp.showNextScene(email);
                            DialogueController controller = new DialogueController();
                            try {
                                controller.showSuccessDialogue(mapp.getPrimaryStage(),"Code sent.",415,36,1.5);
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }
                        } else {
                            // Handle the case where mail sending failed
                            codeSendFail.setVisible(true);

                        }
                    });

                    // Handle exceptions if any
                    sendMailTask.setOnFailed(event -> {
                        Throwable exception = sendMailTask.getException();
                        // Enable UI controls and hide "Please wait" message
                        setUIControlsDisabled(false);
                        setPleaseWaitMessage(false);
                        codeSendFail.setVisible(true);
                    });

                    // Start the task in a separate thread
                    new Thread(sendMailTask).start();
                } else {
                    emailErr.setVisible(true);
                }
            }else{
                emailvalidateErr.setText("* this field cannot be empty");
                emailvalidateErr.setVisible(true);

            }
        }
        else {
            DialogueController controller=new DialogueController();
            controller.showErrorDialogue(mapp.getPrimaryStage(),"Network Error",415,41,2);

        }


    }
    @FXML
    private void onEmailTyped(){
        emailvalidateErr.setVisible(false);
    }


    // Helper methods to control UI state
    private void setUIControlsDisabled(boolean disabled) {
        if (disabled) {
            showDisabledState();
        } else {
            showVisibleState();
        }
    }

    private void showDisabledState() {
        disabledAnchor.setVisible(true);
        visibleAnchor.setVisible(false);
    }

    private void showVisibleState() {
        disabledAnchor.setVisible(false);
        visibleAnchor.setVisible(true);
    }
    private void setPleaseWaitMessage(boolean visible) {
        Platform.runLater(() -> {
            visibleAnchor.setVisible(!visible);
            disabledAnchor.setVisible(visible);
        });
    }


    private boolean validateEmail(String email) throws Exception {
        boolean found = false;
        Connection cn = DatabaseConnection.getConnection();
        String statement = "SELECT u.email, v.validation_code FROM users u LEFT JOIN validation_keys v ON u.email = v.email WHERE u.email=?";

        if (cn != null) {
            PreparedStatement ps = cn.prepareStatement(statement);
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                found = true;
                // Retrieve the code from the database
                storedCode = resultSet.getString("validation_code");
            }
        }

        return found;
    }

    @FXML
    public void initializeNext() throws IOException {
        if (NextEmail != null) {
            NextEmail.setText(email);
            NextEmail.setEditable(false);
        } else {
            DialogueController controller = new DialogueController();
            controller.showErrorDialogue(mapp.getPrimaryStage(),"Email sent fail",415,41,4);
        }
    }
    @FXML
    private void onRegisterNewClicked(){
        mapp.showRegisterScene();
    }

    String emptyMessage="*this field cannot be empty";
    @FXML
    private Label NextPasswordErr;
    @FXML
    private Label NextCPasswordErr;
    String cpassword="";
    String password="";
    @FXML
    private void onUpdateClicked() throws IOException {


          cpassword=getTrimmedTextOrSetError(NextCPasswordField, NextCPasswordErr,emptyMessage);
          password=getTrimmedTextOrSetError(NextPasswordField, NextPasswordErr, emptyMessage);

            boolean p=validatePassword(password,cpassword,"^(?=.*[A-Z]).{6,}$",NextPasswordErr,NextCPasswordErr,"*password must have at least 6 letters with a Uppercase");

          if (password.isEmpty() || cpassword.isEmpty() ||
                  p==false || p==false ) {

              return;
          }
          boolean updated= UpdatePassword.updatePassword(password,email);
          if(updated){

              DialogueController controller=new DialogueController();
              controller.setPassword(password);
              controller.showCopyDialogue(mapp.getPrimaryStage(),320,59,6);
              mapp.showLoginScene();

          }
          else {
              DialogueController controller=new DialogueController();
              controller.showErrorDialogue(mapp.getPrimaryStage(),"Update Failed !",410,41,3.2);
              mapp.showLoginScene();
          }


    }
    private boolean validatePassword(String fieldValueP, String fieldValueC, String regexPatternP,
                                     Label errorLabelP, Label errorLabelC, String errorMessage) {
        boolean result1 = false;
        boolean result2 = false;
        boolean result = false;
        Pattern pattern = Pattern.compile(regexPatternP);

        if (fieldValueP != null && !fieldValueP.isEmpty()) {
            if (!pattern.matcher(fieldValueP).matches()) {
                errorLabelP.setText(errorMessage);
                errorLabelP.setVisible(true);
                result1 = false;
            } else {
                errorLabelP.setVisible(false);
                result1 = true;
            }
        } else {
            errorLabelP.setText(emptyMessage);
            result1 = false;
        }

        if (fieldValueC != null && !fieldValueC.isEmpty()) {
            if (!fieldValueC.equals(fieldValueP)) {
                errorLabelC.setText("*must be same as password");
                errorLabelC.setTextFill(Color.RED); // Set text color to red
                errorLabelC.setVisible(true);
                result2 = false;
            } else {
                errorLabelC.setText("matches");
                errorLabelC.setTextFill(Color.GREEN); // Set text color to green
                errorLabelC.setVisible(true);
                result2 = true;
            }
        } else {
            errorLabelC.setText("*re-enter password to confirm");
            errorLabelC.setVisible(true);
            result2 = false;
        }

        if (result1 && result2) {
            System.out.println("same");
            result = true;
        } else {
            result = false;
            System.out.println("not same");
        }

        return result;
    }

    private String getTrimmedTextOrSetError(TextField textField, Label errorLabel, String errorMessage) {
        if (textField != null) {
            String text = textField.getText().trim();
            if (!text.isEmpty()) {
                return text;
            } else {
                errorLabel.setText(errorMessage);
                errorLabel.setVisible(true);
                return "";
            }
        } else {
            errorLabel.setText(errorMessage);
            errorLabel.setVisible(true);
            return "";
        }
    }


    @FXML
    private void onCheckClicked() throws AddressException, IOException {
        if (validateCode()) {
            System.out.println("Entered code: " + code);
            if (ifCodeValid(code)) {
                if(GmailMain.isCodeValid()){
                    DialogueController controller = new DialogueController();
                    controller.showSuccessDialogue(mapp.getPrimaryStage(),"Valid Code",415,35,.7);

                afterCodeValid.setDisable(false);
                updateButton.setDisable(false);
                resendCodeLink.setDisable(true);
                }
                else{
                    DialogueController controller = new DialogueController();
                    controller.showErrorDialogue(mapp.getPrimaryStage(),"Code expired.Request a new",415,35,2);

                }
            } else {
                DialogueController controller = new DialogueController();
                controller.showErrorDialogue(mapp.getPrimaryStage(),"Invalid Code",415,35,1.7);
            }
        }
    }
    private boolean validateCode() throws IOException {
        String codereg = "[0-9]{6}";
        boolean result = false;

        if (!NextCodeField.getText().trim().isEmpty() && !NextCodeField.getText().isEmpty()) {
            if (NextCodeField.getText().matches(codereg)) {
                try {
                    code = Integer.parseInt(NextCodeField.getText());
                } catch (Exception e) {
                    DialogueController controller = new DialogueController();
                    controller.showErrorDialogue(mapp.getPrimaryStage(),"Invalid Type of Code",415,35,2);
                    return false;
                }
                result = true;
            } else {
                System.out.println("Code does not match the pattern");
            }
        } else {
            System.out.println("Code is empty");
        }

        return result;
    }

    private boolean ifCodeValid(int enteredCode) {
        System.out.println(storedCode);
        // Implement code validity check logic here
        return storedCode != null && storedCode.equals(String.valueOf(enteredCode));
    }

    private boolean sendMail(String htmlContent) throws MessagingException {
        try {

            GmailMain.setEmail(email);
            return GmailMain.sendEmail(htmlContent,"Forget Password");
        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle messaging exception
            return false;
        }
    }

    private boolean canResend = true; // Flag to control resend availability
    private static final long RESEND_COOLDOWN = 30 * 1000; // 30 seconds cooldown

    @FXML
    private void onResendCodeClicked() {
        try {
            if(isNetworkAvailable()){
                if (canResend) {
                    // Set canResend to false when resend is initiated
                    canResend = false;
                    resendCodeLink.setDisable(true);
                    resendCodeLink.setTooltip(new Tooltip("You can resend only after 30 seconds of resent"));


                    // Fetch the existing email and user ID associated with the email
                    String userEmail = GmailMain.getTo();


                    // Generate a new code using GmailMain logic
                    GmailMain.generateCode();
                    String generatedCode = GmailMain.getGeneratedCode();

                    // Update the validation code in the database
                    CodeStore.storeCodeInDatabase(userEmail, generatedCode);

                    // Resend the email with the new validation code using GmailMain logic
                    htmlMailBody = "<html><head>\n" +
                            "    <title>Password Reset Verification</title>\n" +
                            "</head>\n" +
                            "\n" +
                            "<body>\n" +
                            "    <h2>Password Reset Verification</h2>\n" +
                            "    <p>Dear "+getName(userEmail)+",</p>\n" +
                            "    <p>We have received a request to reset the password associated with your account. To proceed with the password reset, please use the following verification code:</p>\n" +
                            "    <p><strong>Verification Code:</strong>"+generatedCode+"</p>\n" +
                            "    <p>Please enter this code on the password reset page to verify your identity and complete the password reset process. Note that this code is valid for 5 minutes for security reasons. If the code expires, you can request a new one on the password reset page.</p>\n" +
                            "    <p>If you did not initiate this password reset request, please disregard this email. Your account security is important to us.</p>\n" +
                            "    <p>Thank you, <br>Subash Singh Dhami</p>\n" +
                            "</body></html>";
                    String subject="Forget Password";
                    GmailMain.sendEmail(htmlMailBody,subject);

                    DialogueController controller = new DialogueController();
                    controller.showSuccessDialogue(mapp.getPrimaryStage(),"Code resent.",415,41,2);

                    // Set up a PauseTransition to reset canResend to true after the cooldown period
                    PauseTransition pause = new PauseTransition(Duration.millis(RESEND_COOLDOWN));
                    pause.setOnFinished(event -> {
                        resendCodeLink.setDisable(false);
                        canResend = true;
                        resendCodeLink.setTooltip(new Tooltip("You can resend only after 30 seconds of resent"));


                    });
                    pause.play();
                } else {
                    DialogueController controller = new DialogueController();
                    controller.showErrorDialogue(mapp.getPrimaryStage(),"Wait 30 second to resend.",415,41,1.5);

                }
            }
            else {
                DialogueController controller=new DialogueController();
                controller.showErrorDialogue(mapp.getPrimaryStage(),"Network Error",415,41,2.5);
            }

        } catch (MessagingException e) {
            e.printStackTrace();
            // Handle database errors or provide appropriate feedback
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    //getname
    public static String getName(String email) {
        try {
            Connection cn=DatabaseConnection.getConnection();
            if(cn!=null){
                PreparedStatement ps=cn.prepareStatement("SELECT name FROM users WHERE email = ?");
                ps.setString(1,email);
                ResultSet i=ps.executeQuery();
                if(i.next()){
                    String name=i.getString("name");
                    return name;
                }

            }
        }catch (Exception e){

        }
        return "user";
    }

}



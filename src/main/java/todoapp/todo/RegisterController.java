package todoapp.todo;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.mindrot.jbcrypt.BCrypt;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import todoapp.todo.MailSender.CodeStore;
import todoapp.todo.MailSender.GmailMain;
import todoapp.todo.MailSender.GmailSender;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.regex.Pattern;



public class RegisterController {
    static MainApp mapp;
    @FXML
    private AnchorPane registerFields;
    @FXML
    private HBox codeBox;
    @FXML
    private Button registerButton;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private TextField email;
    @FXML
    private TextField phone;
    @FXML
    private TextField password;
    @FXML
    private TextField cpassword;
    @FXML
    private TextField username;

    @FXML
    private Label suggestionTop;

    //errors fields fetch
    @FXML
    private Label phoneerr;
    @FXML
    private Label firstnameerr;
    @FXML
    private Label lastnameerr;
    @FXML
    private Label usernameerr;
    @FXML
    private Label emailerr;
    @FXML
    private TextField codeField;
    @FXML
    private Label passworderr;
    @FXML
    private Label cpassworderr;

    //for cusmtom dialogue box
    DialogueController dialogueController = new DialogueController();

    String emptyMessage="*this field cannot be empty";
    String noMatchMessage="*doesn't match the pattern";
    String inValidType="*invalid number";

    //regex


    public static void setMainApp(MainApp mainApp) {
        mapp = mainApp;

    }

    public void initialize(){
        firstname.setTooltip(new Tooltip("* at least of 3 characters"));
        firstname.getTooltip().setStyle("-fx-text-fill: #7D2AE8");

        lastname.setTooltip(new Tooltip("* at least of 3 characters"));
        lastname.getTooltip().setStyle("-fx-text-fill: #7D2AE8");

        email.setTooltip(new Tooltip("* valid mail with @gmail.com"));
        email.getTooltip().setStyle("-fx-text-fill: #7D2AE8");

        phone.setTooltip(new Tooltip("must be of 10 digits within the range of 9700000000 to 9899999999"));
        phone.getTooltip().setStyle("-fx-text-fill: #7D2AE8");

        username.setTooltip(new Tooltip("* at least of 3 characters"));
        username.getTooltip().setStyle("-fx-text-fill: #7D2AE8");

        password.setTooltip(new Tooltip("* a uppercase letter with 6 minimum letters"));
        password.getTooltip().setStyle("-fx-text-fill: #7D2AE8");

        cpassword.setTooltip(new Tooltip("* must be same as the password "));
        cpassword.getTooltip().setStyle("-fx-text-fill: #7D2AE8");

    }


    @FXML
    private void gobackHandle(){
        mapp.showLoginScene();
    }
    // customize patterns as needed
    String fnamereg="^[a-zA-Z]{2,10}$";
//    String lnamereg="";
    String usernamereg="^[a-zA-Z0-9_]{3,20}$";
    String emailreg ="^.{3,}@gmail\\.com$";

    String passwordreg="^(?=.*[A-Z]).{6,}$";
    //at least an uppercase letter with 6 minimum letters

    String phonereg="^[0-9]{10}$";
    //matches any 10-digit number
    long phoneNumber=0;
     boolean usernameTaken=false;
     boolean passwordMatch=false;


    @FXML
    private void onProceedClicked() throws Exception {
        if(isNetworkAvailable()) {

            String firstnameText = getTrimmedTextOrSetError(firstname, firstnameerr, emptyMessage);
            String lastnameText = getTrimmedTextOrSetError(lastname, lastnameerr, emptyMessage);
            String passwordText = getTrimmedTextOrSetError(password, passworderr, emptyMessage);
            String confirmPasswordText = getTrimmedTextOrSetError(cpassword, cpassworderr, emptyMessage);
            String emailAddress = getTrimmedTextOrSetError(email, emailerr, emptyMessage);
            String usernameText = getTrimmedTextOrSetError(username, usernameerr, emptyMessage);

            boolean p = validatePhField(phone, phonereg, phoneerr, noMatchMessage);

            boolean f = validateField(firstnameText, fnamereg, firstnameerr, noMatchMessage);
//            boolean l=validateField(lastnameText, "", lastnameerr, noMatchMessage);
            boolean u = validateField(usernameText, usernamereg, usernameerr, noMatchMessage);
            boolean e = validateField(emailAddress, emailreg, emailerr, noMatchMessage);
            boolean pw = validateField(passwordText, passwordreg, passworderr, noMatchMessage);
            boolean emailUsed = emailExistanceVerify(emailAddress);


            // Check if all validations passed before calling register
            if (firstnameText.isEmpty() || lastnameText.isEmpty() ||
                    passwordText.isEmpty() || confirmPasswordText.isEmpty() || emailAddress.isEmpty() ||
                    usernameText.isEmpty() || p == false || f == false || u == false || e == false ||
                    pw == false || usernameTaken == true || passwordMatch == false || emailUsed == true) {

                return;
            }
            registerFields.setDisable(true);
            sendEmailVerifCode(firstnameText, emailAddress);
            codeBox.setDisable(false);
        }else{
            dialogueController.showErrorDialogue(mapp.getPrimaryStage(),"Network Error",395,35,2);
        }

    }

    private void sendEmailVerifCode(String firstnameText, String emailAddress) throws MessagingException, IOException {
        GmailMain.setEmail(emailAddress);
        GmailMain.generateCode();

        String generatedCode = GmailMain.getGeneratedCode();
        String htmlContent = "<html>\n" +
                "<head>\n" +
                "    <title>Email Verification</title>\n" +
                "</head>\n" +
                "<body style=\"font-family: Arial, sans-serif; margin: 0; padding: 0;\">\n" +
                "\n" +
                "    <div>\n" +
                "        <h2>Welcome to ToDO Desktop App!</h2>\n" +
                "    </div>\n" +
                "\n" +
                "    <div style=\"padding: 20px;\">\n" +
                "        <p>Dear "+firstnameText+",</p>\n" +
                "        <p>Thank you for joining our platform! To ensure the security of your account, we require you to verify your email address. Your email, "+emailAddress+" has been added to our system, and we're excited to have you on board.</p>\n" +
                "        <p>Please use the following verification code to complete the registration process:</p>\n" +
                "\n" +
                "        <h3>Verification Code: "+generatedCode+"</h3>\n" +
                "\n" +
                "        <p>To verify your email address, enter this code in the email code field in the register page</p>\n" +
                "\n" +
                "        <p>If you didn't create an account or have received this email in error, please ignore it. Your account will only be activated once you verify your email.</p>\n" +
                "\n" +
                "        <p>Welcome to our community! We're here to provide you with a seamless experience. If you encounter any issues or have questions, feel free to reach out to our us at <a href=\"mailto:tododesktopapp@gmail.com\">tododesktopapp@gmail.com</a>.</p>\n" +
                "\n" +
                "        <p>Best regards,<br>ToDo Desktop App <br>Subash Singh Dhami</p>\n" +
                "    </div>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        String subject="Register Verification";
        if(GmailMain.sendEmail(htmlContent,subject)){
            dialogueController.showSuccessDialogue(mapp.getPrimaryStage(),"code sent in mail",391,35,1);
            CodeStore.storeCodeInDatabase(emailAddress,generatedCode);
        }

    }
    int code;

    @FXML
    private void onGoClicked() throws AddressException, IOException {
        if (validateCode()) {
            System.out.println("Entered code: " + code);
            if (ifCodeValid(code)) {
                if(GmailMain.isCodeValid()){
                    dialogueController.showSuccessDialogue(mapp.getPrimaryStage(),"Valid Code",415,35,.4);
                    dialogueController.showInfoDialogue(mapp.getPrimaryStage(),"Register finish","Redirecting......",390,35,4);
                    registerButton.setDisable(false);
                    onRegisterClicked();
                }
                else{
                    dialogueController.showErrorDialogue(mapp.getPrimaryStage(),"Code expired.Request a new",415,35,2);

                }
            } else {
                dialogueController.showErrorDialogue(mapp.getPrimaryStage(),"Invalid Code",415,35,1.7);
            }
        }
    }
    private boolean validateCode() throws IOException {
        String codereg = "[0-9]{6}";
        boolean result = false;

        if (!codeField.getText().trim().isEmpty() && !codeField.getText().isEmpty()) {
            if (codeField.getText().matches(codereg)) {
                try {
                    code = Integer.parseInt(codeField.getText());
                } catch (Exception e) {
                    dialogueController.showErrorDialogue(mapp.getPrimaryStage(),"Invalid Type of Code",415,35,2);
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


    private String getStoredCode(String email){
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement("SELECT validation_code FROM validation_keys WHERE email=?");
            ps.setString(1,email);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                return resultSet.getString("validation_code");
            }
            else{
                System.out.println("No code stored");
                return null;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private boolean ifCodeValid(int enteredCode) {
        String storedCode=getStoredCode(email.getText());
        System.out.println(storedCode);
        // Implement code validity check logic here
        return storedCode != null && storedCode.equals(String.valueOf(enteredCode));
    }


    private boolean validateField(String fieldValue,String regexPattern, Label errorLabel, String errorMessage) {
        boolean result = true;
        Pattern pattern = Pattern.compile(regexPattern);

        if (fieldValue != null && !fieldValue.isEmpty() && !pattern.matcher(fieldValue).matches()) {
            errorLabel.setText(errorMessage);
            suggestionTop.setDisable(false);
            result = false;
        }

        return result;
    }
    private boolean validatePhField(TextField phone, String phonereg, Label phoneerr, String noMatchMessage) {
        boolean result = true;
        Pattern pattern=Pattern.compile(phonereg);

        boolean matcher=pattern.matcher(phone.getText()).matches();
       if(!phone.getText().trim().isEmpty() && !phone.getText().isEmpty()){
           if(matcher){
               try{
                   phoneNumber=Long.parseLong(phone.getText());
                   if(phoneNumber<9700000000L || phoneNumber >9899999999L){
                       phoneerr.setText("*invalid number range");
                       phoneerr.setTextFill(Color.RED);
                       phone.setTooltip(new Tooltip("*Phone number must be between 9700000000 and 9899999999"));
                       phone.getTooltip().setStyle("-fx-text-fill: red;");
                       suggestionTop.setDisable(false);

                   }
               }
               catch (Exception e){
                   phoneerr.setText(inValidType);
                   phoneerr.setTextFill(Color.RED);
                   phone.setTooltip(new Tooltip("*it must contain numbers only"));
                   phone.getTooltip().setStyle("-fx-text-fill: red;");
                   suggestionTop.setDisable(false);


                   result=false;
                   return result;
               }

               return result=true;
           } else{
               phoneerr.setText(noMatchMessage);
               phoneerr.setTextFill(Color.RED);
               phone.setTooltip(new Tooltip("*phone number must have 10 digits"));
               phone.getTooltip().setStyle("-fx-text-fill: red;");
               suggestionTop.setDisable(false);


               try {
                   phoneNumber =Long.parseLong(phone.getText());
               }
               catch (Exception e){
                   phoneerr.setText(inValidType);
                   phoneerr.setTextFill(Color.RED);
                   phone.setTooltip(new Tooltip("*it must contain numbers only"));
                   phone.getTooltip().setStyle("-fx-text-fill: red;");
                   suggestionTop.setDisable(false);

                   result=false;
                   return result;

               }
               return result=false;

           }
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
                suggestionTop.setDisable(false);
                return "";
            }
        } else {
            errorLabel.setText(errorMessage);
            suggestionTop.setDisable(false);
            return "";
        }
    }


    @FXML
    private void usernameVerify() throws Exception{
        String usernameText=getTrimmedTextOrSetError(username, usernameerr, emptyMessage);
        Connection cn= DatabaseConnection.getConnection();
        String statement="Select username from users where username=?";
        if(!usernameText.isEmpty() && !usernameText.isBlank() && cn!=null){
            PreparedStatement ps=cn.prepareStatement(statement);
            ps.setString(1,usernameText);
            ResultSet s=ps.executeQuery();
            if(s.next()){
                usernameerr.setText("*already taken");
                usernameTaken=true;
            }
            else{
                usernameerr.setText(" available");
                usernameerr.setTextFill(Color.GREEN);
                usernameTaken=false;
            }
        }
    }
    private boolean emailExistanceVerify(String email) throws IOException {
        try{
            Connection cn = DatabaseConnection.getConnection();
            String query = "SELECT COUNT(u_id) FROM users WHERE email = ?";
            PreparedStatement ps =cn.prepareStatement(query);
            ps.setString(1,email);
            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()){
                if(resultSet.getInt(1)>0){
                    dialogueController.showErrorDialogue(mapp.getPrimaryStage(),"email already registered",410,31,1);
                    return true;
                }
                return false;
            }

        }catch(Exception e){
            dialogueController.showErrorDialogue(mapp.getPrimaryStage(),"Dbase connection failed",410,31,1.7);

        }
        return false;
    }
    @FXML
    private void resetUsernameField(){
        usernameerr.setText("*");
        usernameerr.setTextFill(Color.RED);

    }
    @FXML
    private void passwordVerify(){
        if(!cpassword.getText().equals(password.getText())){
            cpassworderr.setText("*must be same as the password");
            cpassworderr.setTextFill(Color.RED);
            passwordMatch=false;
            if(passwordMatch==false){
                cpassworderr.setText("*must be same with the password");
                cpassworderr.setTextFill(Color.RED);
                cpassworderr.setTooltip(new Tooltip("must be same with the password"));
                suggestionTop.setDisable(false);
            }
            return;

        }
        passwordMatch=true;

    }

    //reset fields

    @FXML
    private void resetCPassword(){
        cpassworderr.setTextFill(Color.GREEN);
    }
    @FXML
    private void resetPassword(){

        passworderr.setText("*");
    }
    @FXML
    private void resetFName(){
        firstnameerr.setText("*");
    }
    @FXML
    private void resetLName(){
        lastnameerr.setText("*");
    }
    @FXML
    private void resetPhone(){
        phoneerr.setText("(if any)");
    }
    @FXML
    private void resetEmail(){
        emailerr.setText("*");

    }



    private void register(String name, String emailAddress, long phoneNumber, String usernameText, String passwordText) throws IOException {
        try (Connection cnx = DatabaseConnection.getConnection()) {
            if (cnx != null) {
                String statement;
                if (phoneNumber != 0) {
                    statement = "INSERT INTO users (name, username, password, email, phone) VALUES (?, ?, ?, ?, ?)";
                } else {
                    statement = "INSERT INTO users (name, username, password, email) VALUES (?, ?, ?, ?)";
                }

                try (PreparedStatement ps = cnx.prepareStatement(statement)) {
                    ps.setString(1, name);
                    ps.setString(2, usernameText);
                    ps.setString(3, hashPassword(passwordText));
                    ps.setString(4, emailAddress);
                    if (phoneNumber != 0) {
                        ps.setLong(5, phoneNumber);
                    }

                    int i = ps.executeUpdate();
                    if (i > 0) {
                        mapp.showLoginScene();
                        dialogueController.showSuccessDialogue(mapp.getPrimaryStage(),"User Registered!",230,31,2.1);
                        String subject="Welcome "+name;
                        String htmlContent= "<html lang=\"en\">\n" +
                                "<head>\n" +
                                "    <title>Welcome to Our Platform!</title>\n" +
                                "</head>\n" +
                                "<body>\n" +
                                "    <div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px;\">\n" +
                                "        \n" +
                                "        <p>Dear "+firstname.getText()+",</p>\n" +
                                "\n" +
                                "        <p>We are thrilled to welcome you to our platform! Your email address, "+emailAddress+", has been successfully added, and you are now officially part of our community. With your account, you gain access to a range of exciting features and personalized services.</p>\n" +
                                "\n" +
                                "        <p>As a new user, we encourage you to explore our platform and make the most of your experience. Whether you're here for productivity, collaboration, or simply to stay organized, we've got you covered. Feel free to customize your profile, set preferences, and dive into the diverse range of tools and resources we offer.</p>\n" +
                                "\n" +
                                "        <p>If you ever need assistance or have questions, our support team is here to help. Reach out to tododesktopapp@gmail.com, and we'll be more than happy to assist you.</p>\n" +
                                "\n" +
                                "        <p>Thank you for choosing us. We look forward to being part of your journey on our platform.</p>\n" +
                                "\n" +
                                "        <p>Best regards,<br>Subash Singh Dhami</p>\n" +
                                "    </div>\n" +
                                "</body>\n" +
                                "</html>\n";
                        GmailMain.sendEmail(htmlContent,subject);
                    } else {

                        dialogueController.showErrorDialogue(mapp.getPrimaryStage(),"Register Failed! Try Again",230,31,2.1);
                        mapp.showLoginScene();
                    }
                }
            }
        } catch (Exception e) {

            dialogueController.showErrorDialogue(mapp.getPrimaryStage(),e.getMessage(),410,31,1.7);
        }
    }
    private void onRegisterClicked() throws IOException {
        try {
            register((firstname.getText() + " " + lastname.getText()), email.getText(), phoneNumber, username.getText(), password.getText());

        }
        catch (Exception e){
            dialogueController.showErrorDialogue(mapp.getPrimaryStage(),"Register Failed! Try Again",230,31,2.1);
            mapp.showRegisterScene();


        }

    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(4));

    }
    private static boolean isNetworkAvailable() {
        try {
            InetAddress.getByName("www.google.com");
            return true; // Network is available
        } catch (UnknownHostException e) {
            return false; // Network is not available
        }
    }

}

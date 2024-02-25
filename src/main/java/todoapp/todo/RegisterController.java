package todoapp.todo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.regex.Pattern;

import static javafx.scene.paint.Color.TRANSPARENT;


public class RegisterController {


    static MainApp mapp;
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
    private Label passworderr;
    @FXML
    private Label cpassworderr;
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
    //at least a uppercase letter with 6 minimum letters

    String phonereg="^[0-9]{10}$";
    //matches any 10 digit number
    long phoneNumber=0;
     boolean usernameTaken=false;
     boolean passwordMatch=false;


    @FXML
    private void onRegisterClicked() throws Exception {

        String firstnameText = getTrimmedTextOrSetError(firstname, firstnameerr, emptyMessage);
        String lastnameText = getTrimmedTextOrSetError(lastname, lastnameerr, emptyMessage);
        String passwordText = getTrimmedTextOrSetError(password, passworderr, emptyMessage);
        String confirmPasswordText = getTrimmedTextOrSetError(cpassword, cpassworderr, emptyMessage);
        String emailAddress = getTrimmedTextOrSetError(email, emailerr, emptyMessage);
        String usernameText = getTrimmedTextOrSetError(username, usernameerr, emptyMessage);

          boolean p= validatePhField(phone,phonereg,phoneerr,noMatchMessage);

        boolean f = validateField(firstnameText, fnamereg, firstnameerr, noMatchMessage);
//            boolean l=validateField(lastnameText, "", lastnameerr, noMatchMessage);
        boolean u = validateField(usernameText, usernamereg, usernameerr, noMatchMessage);
        boolean e = validateField(emailAddress, emailreg, emailerr, noMatchMessage);
        boolean pw = validateField(passwordText, passwordreg, passworderr, noMatchMessage);


        // Check if all validations passed before calling register
        if (firstnameText.isEmpty() || lastnameText.isEmpty() ||
                passwordText.isEmpty() || confirmPasswordText.isEmpty() || emailAddress.isEmpty() ||
                usernameText.isEmpty() || p == false || f == false || u == false || e == false ||
                pw ==false ||usernameTaken==true || passwordMatch==false) {

            return;
        }

        // If all validations passed, call the register method

       register((firstnameText + " " + lastnameText), emailAddress, phoneNumber, usernameText, passwordText);

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



    private void register(String name, String emailAddress, long phoneNumber, String usernameText, String passwordText) {
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
                        showSuccess();

                    } else {
                        mapp.showLoginScene();
                        showError();
                    }
                }
            }
        } catch (Exception e) {
            // Handle the exception appropriately, log it, and consider not printing to console in production
            e.printStackTrace();
        }
    }



    private void showSuccess() {
        Stage primaryStage = mapp.getPrimaryStage();

        // Assuming you are transitioning to the login scene
        mapp.showLoginScene();

        // Show success dialog after the scene transition
        showCustomInfoAlert("User Registered Successfully", primaryStage,Color.GREEN);
    }
    private void showError() {
        Stage primaryStage = mapp.getPrimaryStage();
        String message="Cannot register the user!";
        showCustomInfoAlert(message,primaryStage,Color.RED);


    }

    private void showCustomInfoAlert(String content, Stage primaryStage,Color bkcolor) {
        Stage dialog = new Stage();
        dialog.initOwner(primaryStage);

        // Customize the appearance
        dialog.initStyle(StageStyle.UNDECORATED);
        dialog.setResizable(false);

        Label contentLabel = new Label(content);
        contentLabel.setBackground(Background.fill(bkcolor));
        contentLabel.setStyle("-fx-padding: 3px,3px,3px,3px;-fx-text-fill: white;");
        contentLabel.setFont(new Font("times new roman", 18));

        dialog.setX(primaryStage.getX() + 230);
        dialog.setY(primaryStage.getY() + 31);

        dialog.setScene(new Scene(new VBox(contentLabel), TRANSPARENT));

        // Use Timeline for a delayed closing effect
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> dialog.close())
        );
        timeline.play();

        dialog.showAndWait();
    }




    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(4));

    }


}

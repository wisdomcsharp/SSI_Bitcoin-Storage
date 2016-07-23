package ExceptionList;

/**
 * Created by Wisdom on 22/07/2016.
 */
public class PasswordTooShort extends RuntimeException{
    public String Message = "Password must be at least 8 characters";
    public PasswordTooShort(){

    }
}

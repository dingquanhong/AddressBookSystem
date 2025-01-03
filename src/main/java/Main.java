import Utils.UserToken.AutomaticLogin;
import Utils.View.Theme.Theme;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        Theme.load();
        AutomaticLogin.autologin();
    }
}

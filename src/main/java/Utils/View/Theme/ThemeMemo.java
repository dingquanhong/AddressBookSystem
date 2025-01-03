package Utils.View.Theme;

import Model.User;

import java.io.Serializable;
import java.util.ArrayList;

public class ThemeMemo implements Serializable {
    ArrayList<User> userList;
    String themeName;
    String themePath;

    public String getThemePath() {
        return themePath;
    }

    public void setThemePath(String themePath) {
        this.themePath = themePath;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }
}

package Utils.View.Theme;

public class ThemeToken {
    String themeName;
    String themePath;

    public ThemeToken(String themeName, String themePath) {
        this.themeName = themeName;
        this.themePath = themePath;
    }

    public ThemeMemo createMemo()
    {
        ThemeMemo themeMemo = new ThemeMemo();
        themeMemo.setThemeName(themeName);
        themeMemo.setThemePath(themePath);
        return themeMemo;
    }
    public void restoreFromMemo(ThemeMemo themeMemo)
    {
        themeName = themeMemo.getThemeName();
        themePath = themeMemo.getThemePath();
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getThemePath() {
        return themePath;
    }

    public void setThemePath(String themePath) {
        this.themePath = themePath;
    }
}

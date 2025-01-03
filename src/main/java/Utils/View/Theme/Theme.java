package Utils.View.Theme;

import Utils.Database.Result;
import View.Home;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.IntelliJTheme;

import javax.swing.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Theme {
    public static void load(){
        ThemeManager themeManager = new ThemeManager();
        Result result = themeManager.load();
        if (result.getStatus()){
            ThemeMemo themeToken = (ThemeMemo) result.getData();
                String themePath = themeToken.getThemePath();
                if (!Files.notExists(Paths.get(themePath))){
                    FlatIntelliJLaf.setup();
                    new Result(false,"主题加载错误！");
                    return;
                }
                IntelliJTheme.setup(Home.class.getResourceAsStream(
                        themeToken.getThemePath()
                ));

        }else {
            FlatIntelliJLaf.setup();
        }
    }
}

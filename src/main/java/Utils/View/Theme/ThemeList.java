package Utils.View.Theme;

import java.io.File;
import java.util.ArrayList;

public class ThemeList {
    public static final ArrayList<ThemeToken> themes = new ArrayList<>();
    static {
        // 指定主题文件所在的目录
        String themesDirectoryPath = "src/main/resources/themes";
        File themesDirectory = new File(themesDirectoryPath);

        // 检查目录是否存在并且是一个目录
        if (themesDirectory.exists() && themesDirectory.isDirectory()) {
            // 获取目录下的所有文件
            File[] themeFiles = themesDirectory.listFiles((dir, name) -> name.endsWith(".theme.json"));

            if (themeFiles != null) {
                for (File themeFile : themeFiles) {
                    // 获取文件名
                    String fileName = themeFile.getName();
                    // 提取主题名（第一个 . 之前的字符）
                    String themeName = fileName.substring(0, fileName.indexOf('.'));
                    // 创建 ThemeToken 对象并添加到 themes 列表中
                    themes.add(new ThemeToken(themeName, "/themes/" + fileName));
                }
            }
        } else {
            System.err.println("Themes directory does not exist or is not a directory: " + themesDirectoryPath);
        }
    }
}

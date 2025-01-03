package Utils.View.Theme;

import Utils.Database.Result;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ThemeManager {
    //path改为当前文件夹下
    Path path = Paths.get("./theme.config");
    public Result save(ThemeMemo memo){
        try {
            Files.createDirectories(path.getParent());
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()));
            oos.writeObject(memo);
            oos.close();
            return new Result(true,"主题信息记录成功");
        } catch (IOException e) {
            return new Result(false,e.getMessage());
        }
    }
    public Result load(){
        try {
            Files.createDirectories(path.getParent());
            if (Files.notExists(path)) {
                return new Result(false,"未找到主题信息文件");
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()));
            ThemeMemo memo = (ThemeMemo) ois.readObject();
            return new Result(true,"读取主题信息成功!",memo);
        } catch (IOException e) {
            return new Result(false,e.getMessage());
        }catch (ClassNotFoundException e){
            return new Result(false,e.getMessage());
        }
    }
}

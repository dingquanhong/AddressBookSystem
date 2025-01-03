package Utils.UserToken;

import Utils.Database.Result;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserManager {
    Path path = Paths.get("./user.config");
    public Result save(UserMemo memo)  {
        try {
            Files.createDirectories(path.getParent());
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()));
            oos.writeObject(memo);
            oos.close();
            return new Result(true,"用户信息记录成功");
        } catch (IOException e) {
            return new Result(false,e.getMessage());
        }
    }
    public Result load() {

        UserMemo memo = null;
        try {
            Files.createDirectories(path.getParent());
            if (Files.notExists(path)) {
                return new Result(false,"未找到用户信息文件");
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()));
            memo = (UserMemo) ois.readObject();
            return new Result(true,"读取用户信息成功!",memo);
        } catch (IOException e) {
            return new Result(false,e.getMessage());
        } catch (ClassNotFoundException e) {
            return new Result(false,e.getMessage());
        }
    }

}

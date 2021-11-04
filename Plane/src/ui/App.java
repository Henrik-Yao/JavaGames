package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

//处理图片的工具类
//此处我定义了两种获取图片的方法，可以对照参考
public class App {
    //static可以公用所有对象都共用该方法，并且可以不依赖对象实现
    public static BufferedImage getImg(String path){
        //用try方法捕获异常
        try {
            //io流，输送数据的管道
            BufferedImage img = ImageIO.read(App.class.getResource(path));
            return img;
        }
        //异常处理，打印异常
        catch (IOException e) {
            e.printStackTrace();
        }
        //没找到则返回空
        return null;
    }

    //此处与贪吃蛇小游戏的调用方法是一样的
    public static ImageIcon getImg2(String path){
        InputStream is;
        //从主类文件所在的路径寻找相应路径的图片
        is = App.class.getClassLoader().getResourceAsStream(path);
        //用try方法捕获异常
        try {
            return new ImageIcon(ImageIO.read(is));
        }
        //异常处理，打印异常
        catch (IOException e) {
            e.printStackTrace();
        }
        //没找到则返回空
        return null;
    }
}

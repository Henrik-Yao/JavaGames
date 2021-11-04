package ui;

import java.awt.image.BufferedImage;

//飞行物具有共同特点，故抽离成父类
public class FlyObject {
    //均采用照片素材
    BufferedImage img;
    //位置的横坐标
    int x;
    //位置的纵坐标
    int y;
    //图片元素的宽度
    int w;
    //图片元素的高度
    int h;
}

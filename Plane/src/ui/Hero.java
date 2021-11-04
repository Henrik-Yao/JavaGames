package ui;

import java.awt.image.BufferedImage;

//游戏机
public class Hero extends FlyObject{
    //设置游戏机血量
    int hp;

    //构造函数
    public Hero(){
        //获取游戏机元素
        img = App.getImg("/img/hero.png");
        //确认初始位置
        x = 200;
        y = 500;
        //获取游戏机图片元素的宽和高
        w = img.getWidth();
        h = img.getHeight();
        //设置初始血量为3
        hp = 3;
    }

    //根据传入参数移动相应位置
    public void moveToMouse(int mx,int my){
        x = mx;
        y = my;
    }
}

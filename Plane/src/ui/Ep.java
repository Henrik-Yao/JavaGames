package ui;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//敌机类
public class Ep extends FlyObject{
    //设置敌机速度
    int sp;
    //设置敌机类型，不同的敌机类型有不同的属性
    int type;

    //构造函数
    public Ep(){
        //引入随机数随机调用敌机
        Random random = new Random();
        //调用[1,15]范围内的敌机
        int index = random.nextInt(15)+1;
        //保存敌机类型
        type = index;
        //如果序号小于10，则补充前导0，实质符合图片命名规律
        String path = "/img/ep" + (index<10?"0":"")+index+".png";
        //根据路径调用方法类函数获取图片io流
        img = App.getImg(path);
        //确定敌机位置
        //获取敌机照片元素宽度参数
        w = img.getWidth();
        //边界长减去照片宽度，防止照片越界
        x = random.nextInt(512-w);
        y = 0;
        //设置速度
        sp = 17-index;
    }

    //设置敌机移动方法
    public void move() {
        //如果敌机类型为5，则向左方倾斜移动
        if(type==5){
            x -= 5;
            y += sp;
        }
        //如果敌机类型为5，则向右方倾斜移动
        else if(type==6){
            x += 5;
            y += sp;
        }
        //如果是其他类型，则正常向下移动
        else {
            y+=sp;
        }
    }

    //判断敌机是否被子弹击中
    public boolean shootBy(Fire f) {
        //获取图片元素属性，确定相应的坐标算法，判断是否满足条件，满足则被击中
        Boolean hit = x <= f.x+f.w &&x>f.x-w&&y<=f.y+f.h&&y>f.y-h;
        return hit;
    }

    //判断敌机是否被玩家机击中
    public boolean shootBy(Hero f) {
        //获取图片元素属性，确定相应的坐标算法，判断是否满足条件，满足则被击中
        Boolean hit = x <= f.x+f.w &&x>f.x-w&&y<=f.y+f.h&&y>f.y-h;
        return hit;
    }
}

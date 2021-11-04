package ui;

import javax.swing.*;

//创建窗体
public class GameFrame extends JFrame {
    //构造方法，初始化窗体属性
    public GameFrame(){
        //设置标题，来源于JFrame
        setTitle("飞机大战");
        //设置大小
        setSize(512,768);
        //设置居中
        setLocationRelativeTo(null);
        //设置窗体可见
        //setVisible(true);
        //不允许玩家修改界面大小
        setResizable(false);
        //设置默认的关闭选项
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

package com.company;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        //创建窗体对象
        JFrame frame = new JFrame();
        //创建窗体参数（）
        frame.setBounds(10,10,900,720);
        //设置不允许更改大小
        //frame.setResizable(false);
        //设置关闭方式
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //添加主题逻辑框架
        frame.add(new Mpanel());
        //设置窗体可见
        frame.setVisible(true);
    }
}
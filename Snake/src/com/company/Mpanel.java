package com.company;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

//基本套路，继承JPanel，实现KeyListener, ActionListener接口
public class Mpanel extends JPanel implements KeyListener, ActionListener {
    //定义所需要的图片元素
    //标题
    ImageIcon title;
    //蛇的身体部位
    ImageIcon body;
    //蛇向上方向的头
    ImageIcon up;
    //蛇向下方向的头
    ImageIcon down;
    //蛇向左方向的头
    ImageIcon left;
    //蛇向右方向的头
    ImageIcon right;
    //食物元素
    ImageIcon food;

    //定义初始长度
    int len = 3;
    //定义初始分数
    int score = 0;
    //存放蛇位置坐标的数组
    int[] snakex = new int[750];
    int[] snakey = new int[750];
    //定义方向
    String fx = "R";
    //定义游戏是否开始
    Boolean isStart = false;
    //定义游戏是否失败
    Boolean isFailed = false;
    //定义游戏时钟，在此可以更改蛇移动速度，默认100ms
    Timer timer = new Timer(100,this);
    //定义食物的坐标
    int foodx;
    int foody;
    //引入随机数，用于生成食物的随机位置
    Random random = new Random();
    //定义背景音乐
    Clip bgm;

    //构造方法
    public Mpanel(){
        //加载图片元素
        loadImages();
        //初始化数据
        initSnake();
        //设置聚焦
        this.setFocusable(true);
        //设置键盘监听
        this.addKeyListener(this);
        //开始计时
        timer.start();
        //加载背景音乐
        loadBGM();
    }

    //设置绘图元素
    public void paintComponent(Graphics g){
        //调用父类方法
        super.paintComponent(g);
        //设置背景颜色为白色
        this.setBackground(Color.white);
        //设置标题元素位置
        title.paintIcon(this,g,25,11);
        //设置白色背景填充位置
        g.fillRect(25,75,850,600);
        //设置画笔颜色为白色
        g.setColor(Color.white);
        //设置长度显示
        g.drawString("Len: " + len,750,35);
        //设置分数显示
        g.drawString("Score: " + score,750,50);

        //根据方向调用相应的蛇头元素
        if(fx == "R"){
            right.paintIcon(this,g,snakex[0],snakey[0]);
        }
        else if(fx == "L"){
            left.paintIcon(this,g,snakex[0],snakey[0]);
        }
        else if(fx == "U"){
            up.paintIcon(this,g,snakex[0],snakey[0]);
        }
        else if(fx == "D"){
            down.paintIcon(this,g,snakex[0],snakey[0]);
        }
        for(int i=1;i<len;i++){
            body.paintIcon(this,g,snakex[i],snakey[i]);
        }

        //调用食物元素
        food.paintIcon(this,g,foodx,foody);

        //游戏开始的处理
        if(isStart == false){
            //设置画笔颜色
            g.setColor(Color.white);
            //设置字体
            g.setFont(new Font("arial",Font.BOLD,40));
            //设置具体文字
            g.drawString("Press Space To Start",250,300);
        }

        //游戏失败的处理
        if(isFailed){
            //设置画笔颜色
            g.setColor(Color.red);
            //设置字体
            g.setFont(new Font("arial",Font.BOLD,40));
            //设置具体文字
            g.drawString("Failed: Press Space To Start",250,300);
        }
    }

    //初始化数据
    public void initSnake(){
        len = 3;
        snakex[0] = 100;
        snakey[0] = 100;
        snakex[1] = 75;
        snakey[1] = 100;
        snakex[2] = 50;
        snakey[2] = 100;
        //设置食物的初始随机生成位置
        foodx = 25 + 25 * random.nextInt(34);
        foody = 75 + 25 * random.nextInt(24);
        //设置初始值
        fx = "R";
        score = 0;
    }

    //接口要求的实现方法
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //获取键盘监听
        int keyCode = e.getKeyCode();
        //空格
        if(keyCode == KeyEvent.VK_SPACE){
            //如果失败则重置信息
            if(isFailed){
                isFailed = !isFailed;
                //重新开始
                initSnake();
            }
            //设置开始
            else {
                isStart = !isStart;
            }

            //判断开始后循环播放背景音乐
            if(isStart){
                bgm.loop(Clip.LOOP_CONTINUOUSLY);
            }
            //未开始和暂停则不播放背景音乐
            else {
                bgm.stop();
            }
            //重新绘图
            repaint();
        }
        //右键处理并设置不能回头
        else if(keyCode == KeyEvent.VK_LEFT && fx != "R"){
            fx = "L";
        }
        //左键
        else if(keyCode == KeyEvent.VK_RIGHT && fx != "L"){
            fx = "R";
        }
        //上键
        else if(keyCode == KeyEvent.VK_UP && fx != "D"){
            fx = "U";
        }
        //下键
        else if(keyCode == KeyEvent.VK_DOWN && fx != "U"){
            fx = "D";
        }

    }

    //接口要求的实现方法
    @Override
    public void keyReleased(KeyEvent e) {

    }

    //事件监听
    @Override
    public void actionPerformed(ActionEvent e) {
        //开始且没有失败的情况
        if(isStart && !isFailed){
            //循环移动蛇身
            for(int i=len-1;i>0;i--){
                snakex[i]=snakex[i-1];
                snakey[i]=snakey[i-1];
            }
            //向右移动并判断是否撞墙，撞墙则失败
            if(fx == "R"){
                snakex[0]=snakex[0]+25;
                if(snakex[0]>825)isFailed=true;
            }
            //向左移动并判断是否撞墙，撞墙则失败
            else if(fx == "L"){
                snakex[0]=snakex[0]-25;
                if(snakex[0]<50)isFailed=true;
            }
            //向上移动并判断是否撞墙，撞墙则失败
            else if(fx == "U"){
                snakey[0]=snakey[0]-25;
                if(snakey[0]<100)isFailed=true;
            }
            //向下移动并判断是否撞墙，撞墙则失败
            else if(fx == "D"){
                snakey[0]=snakey[0]+25;
                if(snakey[0]>625)isFailed=true;
            }

            //吃到食物的判断
            if(snakex[0]==foodx&&snakey[0]==foody){
                //身体加长，分加多，并且随机生成新食物
                len++;
                score += 10;
                foodx = 25 + 25 * random.nextInt(34);
                foody = 75 + 25 * random.nextInt(24);
            }

            //撞到自身的处理
            for(int i=1;i<len;i++){
                if(snakex[i]==snakex[0] && snakey[i]==snakey[0]){
                    isFailed = true;
                }
            }

            //重新绘图
            repaint();
        }
        //事件刷新
        timer.start();
    }

    //加载背景音乐
    public void loadBGM(){
        try {
            //从类所在文件引入
            bgm = AudioSystem.getClip();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("com/company/sound/bgm.wav");
            AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            bgm.open(ais);
            //bgm.loop(Clip.LOOP_CONTINUOUSLY);
        }
        //相应的异常处理
        catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    //加载图片
    public void loadImages(){
        InputStream is;
        try {
            //依次添加响应元素
            is = getClass().getClassLoader().getResourceAsStream("com/company/img/title.jpg");
            title = new ImageIcon(ImageIO.read(is));
            is = getClass().getClassLoader().getResourceAsStream("com/company/img/body.png");
            body = new ImageIcon(ImageIO.read(is));
            is = getClass().getClassLoader().getResourceAsStream("com/company/img/up.png");
            up = new ImageIcon(ImageIO.read(is));
            is = getClass().getClassLoader().getResourceAsStream("com/company/img/down.png");
            down = new ImageIcon(ImageIO.read(is));
            is = getClass().getClassLoader().getResourceAsStream("com/company/img/left.png");
            left = new ImageIcon(ImageIO.read(is));
            is = getClass().getClassLoader().getResourceAsStream("com/company/img/right.png");
            right = new ImageIcon(ImageIO.read(is));
            is = getClass().getClassLoader().getResourceAsStream("com/company/img/food.png");
            food = new ImageIcon(ImageIO.read(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

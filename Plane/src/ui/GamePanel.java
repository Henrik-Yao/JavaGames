package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//自定义游戏面板(创建画布)
public class GamePanel extends JPanel {

    //定义背景图
    BufferedImage bg;

    //构造游戏机
    Hero hero = new Hero();
    //敌机集合
    List<Ep> eps = new ArrayList<Ep>();
    //弹药集合
    List<Fire> fs = new ArrayList<Fire>();
    //定义分数
    int score;
    //设置游戏开关
    Boolean gameover=false;
    //设置火力
    int power = 1;

    //动作函数，描绘场景内飞行物的运动状态
    public void action(){
        //创建线程（基本模板）
        new Thread(){
            public void run(){
                //无线循环创建
                while (true){
                    //判断如果游戏没有失败，则执行以下操作
                    if(!gameover){
                        //敌机进场
                        epEnter();
                        //调用敌机移动方法
                        epMove();
                        //发射子弹
                        shoot();
                        //子弹移动
                        fireMove();
                        //判断子弹是否击中敌机
                        shootEp();
                        //检测敌机是否撞到游戏机
                        hit();
                    }
                    //每执行一次，线程休眠一会儿
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //重绘界面
                    repaint();
                }
            }
        }.start();//让线程开始运行
    }

    //每执行20次，释放一个敌机，创建计数器计数
    int index = 0;
    protected void epEnter(){
        index++;
        //创建敌机
        if(index>=20){
            //创建敌机
            Ep e = new Ep();
            //加入集合
            eps.add(e);
            //重置计数器
            index = 0;
        }
    }

    //让敌机移动
    protected void epMove(){
        //遍历敌机集合，依次移动
        for (int i = 0; i < eps.size(); i++) {
            //获取集合中敌机
            Ep e = eps.get(i);
            //调用敌机类中的移动方法
            e.move();
        }
    }

    //每执行20次，创建一颗子弹，创建计数器计数
    int findex = 0;
    protected void shoot(){
        findex++;
        if(findex>=20){
            //根据火力判断子弹行数
            //一排子弹
            if(power==1){
                //创建子弹
                Fire fire1 = new Fire(hero.x+45,hero.y,1);
                //将子弹存入子弹集合中
                fs.add(fire1);
            }
            //两排子弹
            else if(power==2){
                //创建子弹
                Fire fire1 = new Fire(hero.x+15,hero.y,0);
                //将子弹存入子弹集合中
                fs.add(fire1);
                //创建子弹
                Fire fire2 = new Fire(hero.x+75,hero.y,2);
                //将子弹存入子弹集合中
                fs.add(fire2);
            }
            //三排子弹
            else{
                //创建子弹
                Fire fire1 = new Fire(hero.x+15,hero.y,0);
                //将子弹存入子弹集合中
                fs.add(fire1);
                //创建子弹
                Fire fire2 = new Fire(hero.x+75,hero.y,2);
                //将子弹存入子弹集合中
                fs.add(fire2);
                //创建子弹
                Fire fire3 = new Fire(hero.x+45,hero.y-10,1);
                //将子弹存入子弹集合中
                fs.add(fire3);
            }
            //使计数器归0
            findex = 0;
        }
    }

    //让子弹移动
    protected void fireMove(){
        //遍历子弹集合
        for (int i = 0; i < fs.size(); i++) {
            //获取每一颗子弹位置
            Fire f = fs.get(i);
            //依次移动每一颗子弹
            f.move();
        }
    }

    //判断子弹是否击中敌机
    protected void shootEp(){
        //遍历所有子弹
        for (int i = 0; i < fs.size(); i++) {
            //获取每一颗子弹
            Fire f = fs.get(i);
            //判断一颗子弹是否击中敌机
            bang(f);

        }
    }

    //判断一颗子弹是否击中敌机
    protected void bang(Fire f){
        for (int i = 0; i < eps.size(); i++) {
            //取出每一张敌机
            Ep e = eps.get(i);
            //判断这个子弹是否击中敌机
            if(e.shootBy(f)&&e.type!=15){
                //判断游戏机机是否击中道具机
                if(e.type==12){
                    //火力增加
                    power++;
                    //如果火力值大于三，增加血量
                    if(power>3){
                        //恢复血量
                        if(hero.hp<3){
                            hero.hp++;
                        }
                        //使游戏机血量不超过3
                        power = 3;
                    }
                }
                //如果敌机被子弹击中
                //敌机消失
                eps.remove(e);
                //删除子弹
                fs.remove(f);
                //增加分数
                score += 10;
            }
        }
    }

    //检测敌机是否撞到主机
    protected void hit() {
        for (int i = 0; i < eps.size(); i++) {
            //获取每一个敌机
            Ep e = eps.get(i);
            //调用敌机的方法判断
            if(e.shootBy(hero)){
                //删除敌机
                eps.remove(e);
                //主机血量减少
                hero.hp--;
                //火力恢复初始值
                power = 1;
                //分数增加
                score += 10;
                //当主机血量减少到0时游戏结束
                if(hero.hp==0){
                    gameover = true;
                }
            }

        }
    }

    //构造函数
    public GamePanel(GameFrame frame){
        //设置背景
        bg = App.getImg("/img/bg2.jpg");

        //创建鼠标监听和鼠标适配器
        MouseAdapter adapter = new MouseAdapter() {
            //点击鼠标时会执行的代码
            @Override
            public void mouseClicked(MouseEvent e) {
                //游戏结束时候，点击屏幕时重新开始游戏
                if(gameover){
                    //重新初始化主机
                    hero = new Hero();
                    //重置游戏开关
                    gameover = false;
                    //分数清0
                    score = 0;
                    //清空敌机集合
                    eps.clear();
                    //随机背景图
                    Random random = new Random();
                    int index = random.nextInt(5)+1;
                    bg = App.getImg("/img/bg"+index+".jpg");
                    //重新绘制
                    repaint();
                }
            }

            //确定需要监听的事件，此处监听鼠标移动事件
            @Override
            public void mouseMoved(MouseEvent e) {
                //让游戏机的横纵坐标等于鼠标的移动坐标
                //获取鼠标的横纵坐标
                int mx = e.getX();
                int my = e.getY();
                //传递坐标
                if(!gameover){
                    //使鼠标坐标正好位于图片中央
                    hero.moveToMouse(mx-114/2,my-93/2);
                }
                //重新绘制界面
                repaint();
            }
        };
        //将适配器加入到监听器中
        addMouseListener(adapter);
        addMouseMotionListener(adapter);

        //使用键盘监听，创建键盘适配器
        KeyListener kd = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            //当键盘被按下是触发
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                //上键
                if(keyCode == KeyEvent.VK_UP){
                    hero.y-=10;
                }
                //下键
                else if(keyCode == KeyEvent.VK_DOWN){
                    hero.y+=10;
                }
                //左键
                else if(keyCode == KeyEvent.VK_LEFT){
                    hero.x-=10;
                }
                //右键
                else if(keyCode == KeyEvent.VK_RIGHT){
                    hero.x+=10;
                }
                repaint();

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        };
        //将适配器加入窗体的监听器中
        frame.addKeyListener(kd);
    }

    //画图方法
    @Override
    public void paint(Graphics g) {
        //调用父类中的一些渲染方法
        super.paint(g);
        //画背景
        g.drawImage(bg,0,0,null);
        //画敌机
        for (int i = 0; i < eps.size(); i++) {
            Ep ep = eps.get(i);
            g.drawImage(ep.img,ep.x,ep.y,null);
        }
        //画子弹
        for (int i = 0; i < fs.size(); i++) {
            Fire fire = fs.get(i);
            g.drawImage(fire.img,fire.x,fire.y,fire.w,fire.h,null);

        }
        //画分数
        g.setColor(Color.white);
        //设置字体型号，设置加粗，设置字体大小
        g.setFont(new Font("\u6977\u4F53",Font.BOLD,30));
        //显示分数
        g.drawString("分数:"+score,10,30);
        //画游戏机
        g.drawImage(hero.img,hero.x,hero.y,null);
        //画游戏机血量
        for (int i = 0; i < hero.hp; i++) {
            g.drawImage(hero.img,380+i*35,5,30,30,null);
        }
        //画游戏结束
        if(gameover){
            //设置字体颜色为红色
            g.setColor(Color.red);
            //设置字体型号，设置加粗，设置字体大小
            g.setFont(new Font("楷体",Font.BOLD,35));
            //显示字体
            g.drawString("GAMEOVER",170,300);
            //设置字体颜色为绿色
            g.setColor(Color.green);
            //设置字体型号，设置加粗，设置字体大小
            g.setFont(new Font("楷体",Font.BOLD,29));
            //显示字体
            g.drawString("yh提醒你点击屏幕任意位置重新开始",10,350);
        }
        //重新绘制界面
        repaint();
    }
}

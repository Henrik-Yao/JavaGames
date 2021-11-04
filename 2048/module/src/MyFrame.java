import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

//定义自己的类（主类）去继承JFrame类并实现KeyListener接口和ActionListener接口
public class MyFrame extends JFrame implements KeyListener, ActionListener {

    //用于存放游戏各位置上的数据
    int[][] data = new int[4][4];

    //用于判断是否失败
    int loseFlag = 1;

    //用于累计分数
    int score = 0;

    //用于切换主题
    String theme = "A";

    //设置三个菜单项目
    JMenuItem item1 = new JMenuItem("经典");
    JMenuItem item2 = new JMenuItem("霓虹");
    JMenuItem item3 = new JMenuItem("糖果");

    //核心方法
    public MyFrame(){
        //初始化窗口
        initFrame();
        //初始化菜单
        initMenu();
        //初始化数据
        initData();
        //绘制界面
        paintView();
        //为窗体提供键盘监听，该类本身就是实现对象
        this.addKeyListener(this);
        //设置窗体可见
        setVisible(true);
    }

    //窗体初始化
    public void initFrame(){
        //设置尺寸
        setSize(514,538);
        //设置居中
        setLocationRelativeTo(null);
        //设置总在最上面
        setAlwaysOnTop(true);
        //设置关闭方式
        setDefaultCloseOperation(3);
        //设置标题
        setTitle("2048小游戏");
        //取消默认布局
        setLayout(null);
    }

    //初始化菜单
    public void initMenu() {
        //菜单栏目
        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("换肤");
        JMenu menu2 = new JMenu("关于我们");

        //添加上menuBar
        menuBar.add(menu1);
        menuBar.add(menu2);

        //添加上menu
        menu1.add(item1);
        menu1.add(item2);
        menu1.add(item3);

        //注册监听
        item1.addActionListener(this);
        item2.addActionListener(this);
        item3.addActionListener(this);

        //添加进窗体
        super.setJMenuBar(menuBar);
    }

    //初始化数据，在随机位置生成两个2
    public void initData(){
        generatorNum();
        generatorNum();
    }

    //重新绘制界面的方法
    public void paintView(){
        //调用父类中的方法清空界面
        getContentPane().removeAll();

        //判断是否失败
        if(loseFlag==2){
            //绘制失败界面
            JLabel loseLable = new JLabel(new ImageIcon("D:\\Download\\BaiDu\\image\\"+theme+"-lose.png"));
            //设置位置和高宽
            loseLable.setBounds(90,100,334,228);
            //将该元素添加到窗体中
            getContentPane().add(loseLable);
        }

        //根据现有数据绘制界面
        for(int i=0;i<4;i++) {
            //根据位置循环绘制
            for (int j = 0; j < 4; j++) {
                JLabel image = new JLabel(new ImageIcon("D:\\Download\\BaiDu\\image\\"+theme+"-"+data[i][j]+".png"));
                //提前计算好位置
                image.setBounds(50 + 100 * j, 50+100*i, 100, 100);
                //将该元素添加进窗体
                getContentPane().add(image);
            }
        }

        //绘制背景图片
        JLabel background = new JLabel(new ImageIcon("D:\\Download\\BaiDu\\image\\"+theme+"-Background.jpg"));
        //设置位置和高宽
        background.setBounds(40,40,420,420);
        //将该元素添加进窗体
        getContentPane().add(background);

        //得分模板设置
        JLabel scoreLable = new JLabel("得分:"+score);
        //设置位置和高宽
        scoreLable.setBounds(50,20,100,20);
        //将该元素添加进窗体
        getContentPane().add(scoreLable);

        //重新绘制界面
        getContentPane().repaint();
    }

    //用不到的但是必须重写的方法，无需关注
    @Override
    public void keyTyped(KeyEvent e) {}

    //键盘被按下所触发的方法，在此方法中加入区分上下左右的按键
    @Override
    public void keyPressed(KeyEvent e) {
        //keyCode接收按键信息
        int keyCode = e.getKeyCode();
        //左移动
        if(keyCode == 37){
            moveToLeft(1);
            generatorNum();
        }
        //上移动
        else if(keyCode==38){
            moveToTop(1);
            generatorNum();
        }
        //右移动
        else if(keyCode==39){
            moveToRight(1);
            generatorNum();
        }
        //下移动
        else if(keyCode==40){
            moveToBottom(1);
            generatorNum();
        }
        //忽视其他按键
        else {
            return;
        }
        //检查是否能够继续移动
        check();
        //重新根据数据绘制界面
        paintView();
    }

    //左移动的方法，通过flag判断，传入1是正常移动，传入2是测试移动
    public void moveToLeft(int flag) {
        for(int i=0;i<data.length;i++){
            //定义一维数组接收一行的数据
            int[] newArr = new int[4];
            //定义下标方便操作
            int index=0;
            for(int x=0;x<data[i].length;x++){
                //将有数据的位置前移
                if(data[i][x]!=0){
                    newArr[index]=data[i][x];
                    index++;
                }
            }
            //赋值到原数组
            data[i]=newArr;
            //判断相邻数据是否相邻，相同则相加，不相同则略过
            for(int x=0;x<3;x++){
                if(data[i][x]==data[i][x+1]){
                    data[i][x]*=2;
                    //如果是正常移动则加分
                    if(flag==1){
                        score+=data[i][x];
                    }
                    //将合并后的数据都前移，实现数据覆盖
                    for(int j=x+1;j<3;j++){
                        data[i][j]=data[i][j+1];
                    }
                    //末尾补0
                    data[i][3]=0;
                }
            }
        }
    }

    //右移动的方法，通过flag判断，传入1是正常移动，传入2是测试移动
    public void moveToRight(int flag) {
        //翻转二维数组
        reverse2Array();
        //对旋转后的数据左移动
        moveToLeft(flag);
        //再次翻转
        reverse2Array();
    }

    //上移动的方法，通过flag判断，传入1是正常移动，传入2是测试移动
    public void moveToTop(int flag) {
        //逆时针旋转数据
        anticlockwise();
        //对旋转后的数据左移动
        moveToLeft(flag);
        //顺时针还原数据
        clockwise();
    }

    //下移动的方法，通过flag判断，传入1是正常移动，传入2是测试移动
    public void moveToBottom(int flag) {
        //顺时针旋转数据
        clockwise();
        //对旋转后的数据左移动
        moveToLeft(flag);
        //逆时针旋转还原数据
        anticlockwise();
    }

    //检查能否左移动
    public boolean checkLeft(){
        //开辟新二维数组用于暂存数据和比较数据
        int[][] newArr = new int[4][4];
        //复制数组
        copyArr(data,newArr);
        //测试移动
        moveToLeft(2);
        boolean flag = false;
        //设置break跳出的for循环标记
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                //如果有数据不相同，则证明能够左移动，则返回true
                if(data[i][j]!=newArr[i][j]){
                    flag=true;
                    break lo;
                }
            }
        }
        //将原本的数据还原
        copyArr(newArr,data);
        return flag;
    }

    //检查能否右移动，与checkLeft()方法原理相似
    public boolean checkRight(){
        int[][] newArr = new int[4][4];
        copyArr(data,newArr);
        moveToRight(2);
        boolean flag = false;
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(data[i][j]!=newArr[i][j]){
                    flag=true;
                    break lo;
                }
            }
        }
        copyArr(newArr,data);
        return flag;
    }

    //检查能否上移动，与checkLeft()方法原理相似
    public boolean checkTop(){
        int[][] newArr = new int[4][4];
        copyArr(data,newArr);
        moveToTop(2);
        boolean flag = false;
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(data[i][j]!=newArr[i][j]){
                    flag=true;
                    break lo;
                }
            }
        }
        copyArr(newArr,data);
        return flag;
    }

    //检查能否下移动，与checkLeft()方法原理相似
    public boolean checkBottom(){
        int[][] newArr = new int[4][4];
        copyArr(data,newArr);
        moveToBottom(2);
        boolean flag = false;
        lo:
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(data[i][j]!=newArr[i][j]){
                    flag=true;
                    break lo;
                }
            }
        }
        copyArr(newArr,data);
        return flag;
    }

    //检查是否失败
    public void check(){
        //上下左右均不能移动 ，则游戏失败
        if(checkLeft()==false&&checkRight()==false&&checkTop()==false&&checkBottom()==false){
            loseFlag = 2;
        }
    }

    //复制二维数组的方法，传入原数组和新数组
    public void copyArr(int[][] src,int[][] dest){
        for (int i = 0; i < src.length; i++) {
            for (int j = 0; j < src[i].length; j++) {
                //遍历复制
                dest[i][j]=src[i][j];
            }
        }
    }

    //键盘被松开
    @Override
    public void keyReleased(KeyEvent e) {}

    //翻转一维数组
    public void reverseArray(int[] arr){
        for(int start=0,end=arr.length-1;start<end;start++,end--){
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
        }
    }

    //翻转二维数组
    public void reverse2Array(){
        for (int i = 0; i < data.length; i++) {
            reverseArray(data[i]);
        }
    }

    //顺时针旋转
    public void clockwise(){
        int[][] newArr = new int[4][4];
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                //找规律啦~
                newArr[j][3-i] = data[i][j];
            }
        }
        data = newArr;
    }

    //逆时针旋转
    public void anticlockwise(){
        int[][] newArr = new int[4][4];
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                //规律
                newArr[3-j][i] = data[i][j];
            }
        }
        data = newArr;
    }

    //空位置随机生成2
    public void generatorNum(){
        int[] arrarI = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        int[] arrarJ = {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1};
        int w=0;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if(data[i][j]==0){
                    //找到并存放空位置
                    arrarI[w]=i;
                    arrarJ[w]=j;
                    w++;
                }
            }
        }
        if(w!=0){
            //随机数找到随机位置
            Random r= new Random();
            int index = r.nextInt(w);
            int x = arrarI[index];
            int y = arrarJ[index];
            //空位置随机生成2
            data[x][y]=2;
        }
    }

    //换肤操作
    @Override
    public void actionPerformed(ActionEvent e) {
        //接收动作监听，
        if(e.getSource()==item1){
            theme = "A";
        }else if(e.getSource()==item2){
            theme = "B";
        }else if(e.getSource()==item3){
            theme = "C";
        }
        //换肤后重新绘制
        paintView();
    }
}


    //测试失败效果的数据
    /*int[][] data = {
            {2,4,8,4},
            {16,32,64,8},
            {128,2,256,2},
            {512,8,1024,2048}
    };*/
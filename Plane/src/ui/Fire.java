package ui;

public class Fire extends FlyObject{
    //子弹当前移动方向，0为左上角飞，1垂直飞，2右上角飞
    int dir;

    //构造方法，初始化子弹
    public Fire(int hx,int hy,int dir){
        //获取子弹的图片
        img = App.getImg("/img/fire.png");
        //确定图片的大小，此处把子弹大小缩小了4倍
        w = img.getWidth()/4;
        h = img.getHeight()/4;
        //根据构造函数传进来的参数设置子弹的位置以及子弹的方向
        x = hx;
        y = hy;
        this.dir=dir;
    }

    //子弹的移动方法
    public void move() {
        //左上角飞
        if(dir==0){
            x -= 1;
            y -= 10;
        }
        //垂直上飞
        else if(dir == 1){
            y -= 10;
        }
        //右上角飞
        else if(dir == 2){
            x += 1;
            y -= 10;
        }
    }
}

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


//==============================================================================
//==============================================================================
//定义主体框架
class MyPaint  implements ChangeListener{
	
	public static final int Width=600;     //定义静态变量，主框架的宽度
	public static final int Heigth=400;    //定义静态变量，主框架的度高
	public static final int JFx=400;       //定义静态变量，主框架的位置
	public static final int JFy=140;       //定义静态变量，主框架的位置
	public static final int pnlWidth=50;   //定义静态变量，小球半径面板的宽度
	public static final int pnlHeigth=300; //定义静态变量，小球半径面板的高度
	private static int delay=1;            //定义静态变量，小球的速度
	private static int R=50;               //定义静态变量，小球的半径
	private static Color color=Color.black;//定义静态变量，小球的颜色
		
	JFrame mainFrame=new JFrame();        //主框架
	JPanel rpanel = new JPanel();          //轻量级容器——小球半径面板
	JToolBar colortool=new JToolBar();     //设置颜色工具条
	 JPanel vpanel=new JPanel();           //设置速度面板
	 JPanel bpanel= new JPanel();          //设置小球面板
	 Container contenPane=mainFrame.getContentPane();//获得内容面板
	
	JButton blackbutton;                   //设置黑色按钮
	JButton yellowbutton;				   //设置黄色色按钮
	JButton redbutton;                     //设置红色按钮
	JButton greenbutton;                   //设置绿色按钮
	JButton bluebutton;                    //设置蓝色按钮
	JButton otherbutton;                   //设置其他颜色按钮
	JColorChooser otherchooser=new JColorChooser();//调色板对话框
	JLabel info;                           //显示信息“设置颜色：”标签
	
	ButtonGroup rg=new ButtonGroup();      //定义单选按钮组
	JRadioButton r1;                       //定义单选按钮
	JRadioButton r2;
	JRadioButton r3;
	JRadioButton r4;
	JRadioButton r5;
	
	private JSpinner spinner;              //微调按钮
	Circle1 c;                              //定义小球
	ballSpeed bs=new ballSpeed(delay,delay);//定义速度处理
	ballColor bc=new ballColor(color);      //定义颜色处理
    ballR br=new ballR(R);	                //定义半径处理
    
    //主框架
	public void showpaint(){	
		mainFrame.setTitle("弹弹球");        //设置标题
		mainFrame.setBounds(JFx,JFy,Width,Heigth);   //设置框架的位置及大小
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置用户关闭框架时的响应动作	
		rpanel();                          //引入小球半径面板
		colortool();                       //引入小球颜色设计工具条 、速度微调按钮  
		ball();		                       //引入小球
		jianting();                        //事件监听器的引入
		Thread timer = new Thread(c);      //设置线程
		timer.start();                     //开启线程		
		mainFrame.setVisible(true);        //显示主框架
	}
	
	//小球半径面板的定义
	public void rpanel() {
		rpanel.setSize(pnlWidth,pnlHeigth);         //设置小球半径面板的大小
		rpanel.setBorder(BorderFactory.createTitledBorder("设置半径"));//设置半径面板定义带提示文字
		rpanel.setLayout(new GridLayout(6,1));     //设置小球半径面板的布局
		contenPane.add(rpanel,BorderLayout.WEST);   //设置面板的位置		
		r1=new JRadioButton("30mm ");               //生成小球半径对象
		r2=new JRadioButton("35mm ");
		r3=new JRadioButton("40mm ");    
		r4=new JRadioButton("45mm ");
		r5=new JRadioButton("50mm ");		    
		rg.add(r1);                                  //在单选按钮中添加单选按钮
		rg.add(r2);
		rg.add(r3);	
		rg.add(r4);
		rg.add(r5);
		rpanel.add(r1);                              //在小球半径面板中添加单选按钮
		rpanel.add(r2);
		rpanel.add(r3);	
		rpanel.add(r4);
		rpanel.add(r5);
	}
	
	//调色版的定义
	public void colortool() {		
		colortool.setSize(Width,Heigth/5);    //设置小球颜色面板的大小
		colortool.setBackground(Color.WHITE); //设置小球颜色面板的背景颜色为白色
		contenPane.add(colortool,BorderLayout.SOUTH);//设置面板的位置		
		blackbutton=new JButton("black");     //设置黑色按钮
		yellowbutton=new JButton("yellow");   //设置黄色按钮
		redbutton=new JButton("red");         //设置红色按钮
		greenbutton=new JButton("green");     //设置绿色按钮
		bluebutton=new JButton("blue");       //设置蓝色按钮
		otherbutton=new JButton("自定义");      //设置其他颜色按钮
		info=new JLabel("设置颜色：");           //显示信息“设置颜色：”标签		
		colortool.add(info);                  //在调色板中添加标签
		colortool.add(blackbutton);           //在调色板中添加颜色按钮
		colortool.add(yellowbutton);
		colortool.add(redbutton);
		colortool.add(greenbutton);
		colortool.add(bluebutton);
		colortool.add(otherbutton);
		speed();                              //将速度微调器放入调色板的EAST处
		colortool.add(vpanel);	
	}
	
	//速度微调按钮设置
	public void speed(){
		contenPane.add(vpanel,BorderLayout.EAST);//设置面板的位置
        vpanel.add(new JLabel("speed"));         //给微调按钮命名
        spinner =new JSpinner();                 //new一个微调按钮
        spinner.setValue(1);                     //设置初始值为1
        vpanel.add(spinner);                     //将指定组件追加到此容器的尾部
        spinner.addChangeListener(this);         //监听里面的数值发生变化
	}
	
	//定义小球
	public void ball(){
		c = new Circle1();                       //生成小球运动对象
	    mainFrame.add(c,BorderLayout.CENTER);    //将小球运动的面板放在主框架的中间
		c.setdelay(bs.xSpeed);                   //传递小球的速度
		c.setColor(bc.color);                    //传递小球的颜色
		c.setR(br.R);		                     //传递小球的半径
		c.setBackground(Color.white);            //设置小球运动背景为白色
	}

	//监听微调器数值的变化
	public void stateChanged(ChangeEvent e) {
		delay=Integer.parseInt(""+spinner.getValue());//获取微调按钮文本里的值
		Circle1.setdelay(delay);                  //对speed设置数值		
	}
	
	//颜色、半径变化事件监听
	public void jianting() {	
		
		//单选按钮r1事件监听器，设置半径为30mm
		r1.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				if(e.getStateChange()==e.SELECTED)
					Circle1.setR(30);
			}
		});
		
		//单选按钮r2事件监听器，设置半径为35mm
		r2.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				if(e.getStateChange()==e.SELECTED)
					Circle1.setR(35);
			}
		});
		
		//单选按钮r3事件监听器，设置半径为40mm
		r3.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				if(e.getStateChange()==e.SELECTED)
					Circle1.setR(40);
			}
		});
		
		//单选按钮r3事件监听器，设置半径为45mm
		r4.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				
				if(e.getStateChange()==e.SELECTED)
					Circle1.setR(45);
			}
		});
		
		//单选按钮r3事件监听器，设置半径为50mm
		r5.addItemListener(new ItemListener() {
			
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange()==e.SELECTED)
					Circle1.setR(50);
			}
		});
		
		//黑色按钮事件监听器，设置小球为黑色
		blackbutton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				Circle1.setColor(Color.BLACK);
			}
		});
		
		//黄色按钮事件监听器，设置小球为黄色
		yellowbutton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				Circle1.setColor(Color.yellow);
			}
		});
		
		//红色按钮事件监听器，设置小球为红色
		redbutton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				Circle1.setColor(Color.red);
			}
		});
		
		//绿色按钮事件监听器，设置小球为黑绿色
		greenbutton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				Circle1.setColor(Color.green);
			}
		});
		
		//蓝色按钮事件监听器，设置小球为蓝色
		bluebutton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				// TODO 自动生成的方法存根
				Circle1.setColor(Color.blue);
			}
		});	
		
		//自定义按钮事件监听器，弹出调色版供用户选择颜色
		otherbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//显示调色版对话框，返回值所选的颜色
				color = otherchooser.showDialog(mainFrame,"自定义",Circle1.getColor());
				Circle1.setColor(color);
				if(color==null)
					Circle1.setColor(Color.BLACK);
			}
		});
	}
}



//==============================================================================================
//==============================================================================================
//生成运动的小球
 class Circle1 extends Panel implements Runnable{
	private static int R;       //小球的半径
    private static Color color; // 小球的颜色
	private static int xs=3;    //小球x方向的初始速度
	private static int ys=1;    //小球y方向的初始速度
	private static int x=0;     //小球在x方向的初始位置
	private static int y=0;     //小球在y方向的初始位置
	
	ballbianjie bj=new ballbianjie(600,400);//小球运动的边界
	
	//无参数构造方法
	 Circle1() {}

	//画球
	public void paint(Graphics g) {
		x+=xs;                 //小球下次的朝X轴运动的长度
		y+=ys;                 //小球下次的朝X轴运动的长度
		if ((x + R) >bj.RIGHT-20-2*R || x < 0) {//判定小球是否到达边界的最右边，若是则往回走
			xs = -xs;
		}
		if (y < 0 || (y + R) > bj.BOTTOM-2*R) {//判定小球是否到达边界的最右边，若是则往灰走
			ys = -ys;
		}
		g.setColor(color);
		g.fillOval(x, y, R, R);
	}  
	
	//重写Runnable内的run方法
	public void run() {
		while (true) {           //使线程一直在运作
			try {
				Thread.sleep(17);//重画延迟的时间
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			repaint();			//重画
		}
		
	}
   
    //设置速度
    public static void setdelay(int delay){  
    	xs=delay*2;
    	ys=delay*2;
    }
    
    //设置半径
	public static void setR(int r) {
		R=r;
	}
	
	//设置颜色
	public static void setColor(Color c) {
		color=c;
	}
    
	//获取半径
	public static int getR() {
		return R;
	}
	
	//获取颜色
	public static Color getColor() {
		return color;
	}
    
}
 
 
 
//====================================================================
//====================================================================

//主类
public class dh{
	public static void main(String[] args) {
		MyPaint testPaint=new MyPaint();
		testPaint.showpaint();
	}
}

//====================================================================
//====================================================================
//定义小球能够到达的边界
class ballbianjie{
	 int TOP;
	 int LEFT;
	 int BOTTOM;
	 int RIGHT; 	 
	 
	//带参数的构造方法
	ballbianjie(int top,int left,int bottom,int right){	        
		TOP=top;
		LEFT=left;
		BOTTOM=bottom;
		RIGHT=right;
	}

	public ballbianjie(int right, int bottom) {
		BOTTOM=bottom;
		RIGHT=right;
	}

	//设置上边界
	void setTOP(int top){                    
		TOP=top;
	}
	
	//获取上边界
	int getTOP() {                           
		return TOP;
	}
	
	//设置左边界
	void setLEFT(int left){                    
		LEFT=left;
	}
	
	//获取左边界
	int getLEFT() {                            
		return LEFT;
	}
	
	//设置下边界
	void setBOTTOM(int bottom){                    
		BOTTOM=bottom;
	}
	
	//获取下边界
	int getBOTTOM() {                            
		return BOTTOM;
	}
	
	 //设置右边界
	void setRIGHT(int right){                   
		RIGHT=right;
	}
	
	//获取右边界
	int getRIGHT() {                            
		return RIGHT;
	}	
}

//===========================================================
//===========================================================
//定义小球的颜色
class ballColor{
	 Color color; 
	
	//带参数的构造方法
	ballColor(Color co){
		color=co;
	}
	
	//设置小球的颜色
	void setColor(Color co){                    
		color=co;
	}
	
	 //获取小球的颜色
	Color getColor() {                           
		return color;
	}	
}

//==============================================================
//==============================================================
//定义小球的大小
class ballR{
	 int R ;
	 
	//带参数的构造方法
	ballR(int r){
		R=r;
	}
	
	//设置小球的大小
	void setR(int r){                    
		R=r;
	}
	
	//获取小球的大小
	int getR() {                            
		return R;
	}	
}

//===============================================================
//===============================================================
//定义小球的速度
class ballSpeed{
	 int xSpeed;   // 小球x轴上的速度
	 int ySpeed;

	// 小球y轴上的速度
	ballSpeed(int xS,int yS){
		xSpeed=xS;
		ySpeed=yS;
	}
	
	//设置小球x方向的速度
	void setxSpeed(int xS){                    
		xSpeed=xS;
	}
	
	//获取小球x方向的速度
	int getxSpeed() {                            
		return xSpeed;
	}
	
	 //设置小球y方向的速度
	void setySpeed(int yS){                   
		ySpeed=yS;
	}
	
	//获取小球y方向的速度
	int getySpeed() {                            
		return ySpeed;
	}	
}


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.awt.Graphics;
import javax.swing.Timer;
    public class flappyBird implements ActionListener, MouseListener, KeyListener {
        public final int height=800,width=800;
        public static flappyBird flappyBird;
        public Renderer renderer;
        public boolean gameover,started;
        public Rectangle bird;
        public int ticks,ymotion,score,maxscore=0;
        public Random rand;
        public String name;
        public ArrayList<Rectangle> column;
        public flappyBird(){
            Scanner sc=new Scanner(System.in);
            System.out.print("Enter the player name : ");
            name=sc.nextLine();
            System.out.println();
            System.out.println("##########################");
            System.out.println("###Thanks for Playing!!###");
            System.out.println("##########################");
//      ***JFrame is used to create a GUI Window...
            JFrame jFrame=new JFrame();
            Timer timer=new Timer(20,this);
            renderer=new Renderer();
            column=new ArrayList<Rectangle>();
            rand =new Random();
            jFrame.add(renderer);
            jFrame.setTitle("Flappy Bird");
//      ***Terminates the code on closing the GUI window...
            jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
//      ***Adjust the height and width of the window.
            jFrame.setSize(height,width);
            jFrame.addMouseListener(this);
            jFrame.addKeyListener(this);
            jFrame.setResizable(false);
//      ***Make sure that GUI is visible.
            jFrame.setVisible(true);   //true because the inbuilt func is asking for boolean...
            bird=new Rectangle(width/2-10,height/2-10,20,20);
            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);
//        timer.getInitialDelay();
            timer.start();

        }
        public void addColumn(Boolean start){
            int space=300;
            int WIDTH=100;
            int HEIGHT =50+rand.nextInt(300);
            if(start){
                column.add(new Rectangle(WIDTH+width+column.size()*300,height-HEIGHT-120,WIDTH,HEIGHT));
                column.add(new Rectangle(WIDTH+width+(column.size()-1)*300,0,WIDTH,height-HEIGHT-space));
            }
            else{
                column.add(new Rectangle(column.get(column.size()-1).x+600,height-HEIGHT-120,WIDTH,HEIGHT));
                column.add(new Rectangle(column.get(column.size()-1).x,0,WIDTH,height-HEIGHT-space));
            }
        }
        public void paintcolumn( Graphics g,Rectangle column){
            g.setColor(Color.green.darker().darker().darker());
            g.fillRect(column.x,column.y,column.width,column.height);
        }
        public void jump(){
            if(gameover){
                bird=new Rectangle(width/2-10,height/2-10,20,20);
                column.clear();
                score=0;
                ymotion=0;
                addColumn(true);
                addColumn(true);
                addColumn(true);
                addColumn(true);
                gameover=false;
            }
            if(!started){
                started=true;
            }
            else if(!gameover){
                if(ymotion>0){
                    ymotion=0;
                }
                ymotion-=10;
            }
        }
        public void actionPerformed(ActionEvent e){
//        bird motion
            int speed=10;
            ticks++;
            if(started){

                for(int i=0;i<column.size();i++){
                    Rectangle columns=column.get(i);
                    columns.x-=speed;
                }
                if(ticks%2==0&&ymotion<20){
                    ymotion+=2;
                }
                for(int i=0;i<column.size();i++){
                    Rectangle columns=column.get(i);
                    if(columns.x+columns.width<0){
                        column.remove(columns);
                        if(columns.y==0){
                            addColumn(false);
                        }
                    }
                }
                bird.y+=ymotion;
                for(Rectangle columns:column){
                    if(!gameover){
                        if(columns.y==0 &&bird.x+bird.width/2>columns.x+columns.width/2-10 && bird.x+bird.width/2<columns.x+columns.width/2+10){
                            score+=1;
                        }
                    }
                    if(columns.intersects(bird)){
                        gameover=true;
                        if(bird.x<=columns.x){
                            bird.x=columns.x-bird.width;
                        }
                        else{
                            if(columns.y!=0){
                                bird.y=columns.y-bird.height;
                            }
                            else if(bird.y<columns.y){
                                bird.y=columns.y;
                            }
                        }
                    }
                }
                if(bird.y>height-120||bird.y<0){
                    gameover=true;
                }
                if(bird.y+ymotion>=height-120){
                    bird.y=height-120-bird.height;
                    gameover=true;
                }
            }
            if(score>maxscore){
                maxscore=score;
            }
            renderer.repaint();
        }
        public void repaint(Graphics g){
            g.setColor(Color.cyan);
            g.fillRect(0,0,width,height);
            g.setColor(Color.BLUE);
            g.fillRect(bird.x,bird.y, bird.width,bird.height);
            g.setColor(Color.orange);
            g.fillRect(0,height-120,width,120);
            g.setColor(Color.green);
            g.fillRect(0,height-120,width,20);
            g.setColor(Color.gray);
            g.fillRect(0,26-200,width,200);
            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman",1,75));
            if( score==0 &&!gameover) {
                g.drawString("Click to start!!", 100, height / 2 - 50);
                g.setColor(Color.BLUE);
                g.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 30));
                g.drawString("Let's go " + name + "!!", 130, height / 3);
            }
            if(gameover){
                g.drawString("GAME OVER!!",100,height/2-20);
                g.setColor(Color.BLUE);
                g.setFont(new Font("TimesRoman", Font.BOLD | Font.ITALIC, 30));
                g.drawString("Max Score: "+String.valueOf(maxscore),width/2-25,100);
                g.drawString("Score :"+String.valueOf(score),100,height/4);
            }
            if(!gameover && started){
                g.setColor(Color.BLACK);
                g.setFont(new Font("TimesRoman",1,75));
                g.drawString(String.valueOf(score),width/2-25,100);
            }

            for(Rectangle column:column){
                paintcolumn(g,column);
            }
        }
        public class Renderer extends JPanel {
            //Jpanel a GUI component function as a container to hold other components...
            private static  final long serialVersionUID=1L;
            @Override
            protected void paintComponent(Graphics g){
                super.paintComponent(g);
                flappyBird.flappyBird.repaint(g);
            }
        }

        public static void main(String[] args) {
            flappyBird=new flappyBird();
        }
        public void KeyReleased(KeyEvent e){
            if(e.getKeyCode()==KeyEvent.VK_SPACE){
                jump();
            }
        }
        public void mouseClicked(MouseEvent e){
            jump();
        }
        public void mousePressed(MouseEvent e){
        }
        public void mouseReleased(MouseEvent e){
        }
        public void mouseEntered(MouseEvent e){
        }
        public void mouseExited(MouseEvent e){
        }
        public void KeyPressed(KeyEvent e){
        }
        public void KeyTyped(KeyEvent e){
        }
        @Override
        public void keyTyped(KeyEvent e) {
        }
        @Override
        public void keyPressed(KeyEvent e) {
        }
        @Override
        public void keyReleased(KeyEvent e) {
        }




    }



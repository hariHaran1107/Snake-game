import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel implements ActionListener {
    int B_Height = 400;
    int B_Width = 400;
    int Max_Dots = 1600;
    int Dot_Size = 10;
    int Dots;
    int x[] = new int[Max_Dots];
    int y[] = new int[Max_Dots];
    int apple_x;
    int apple_y;
    //Images

    Image dot, head, apple;
    Timer timer;
    int delay = 150;

    boolean leftDirection = true;
    boolean rightDirection = false;

    boolean upDirection = false;

    boolean downDirection = false;

    boolean inGame = true;

    Board() {
        TAdapter tAdapter = new TAdapter();
        addKeyListener(tAdapter);
        setFocusable(true);
        setPreferredSize(new Dimension(B_Width, B_Height));
        setBackground(Color.BLACK);
        inintgame();
        LoadImages();
    }

    //initialize the game
    public void inintgame() {
        Dots = 3;
        //initialize snake position
        x[0] = 250;
        y[0] = 250;
        for (int i = 1; i < Dots; i++) {
            x[i] = x[0] + Dot_Size * i;
            y[i] = y[0];

        }
        locateApple();
        timer = new Timer(delay, this);
        timer.start();

    }

    //load images from the resource folder to the image objects
    public void LoadImages() {
        ImageIcon dotIcon = new ImageIcon("src/resources/dot.png");
        dot = dotIcon.getImage();

        ImageIcon headIcon = new ImageIcon("src/resources/head.png");
        head = headIcon.getImage();

        ImageIcon appleIcon = new ImageIcon("src/resources/apple.png");
        apple = appleIcon.getImage();

    }

    // Draw images of snake and apple at required positions
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    // draw image
    public void doDrawing(Graphics g) {
       if(inGame){
           g.drawImage(apple, apple_x, apple_y, this);
           for (int i = 0; i < Dots; i++) {
               if (i == 0) {
                   g.drawImage(head, x[0], y[0], this);

               } else
                   g.drawImage(dot, x[i], y[i], this);
           }
       }
       else{
           gameOver(g);
           timer.stop();

       }


    }

    //Randomize apple position
    public void locateApple() {
        apple_x = ((int) (Math.random() * 39)) * Dot_Size;
        apple_y = ((int) (Math.random() * 39)) * Dot_Size;
    }
    //check collisions with border and body
    public void checkCollision(){
        //collision with body
        for(int i=1;i<Dots;i++){
            if(i>4&&x[0]==x[i]&&y[0]==y[i]){
                inGame = false;

            }
        }
        //Collision with border
        if(x[0]<0){
            inGame = false;
        }
        if(x[0]>=B_Width){
            inGame = false;
        }
        if(y[0]<0){
            inGame = false;
        }
        if(y[0]>=B_Height){
            inGame = false;
        }
    }
    //Display game over message
    public void gameOver(Graphics g){
        String msg = "Game Over";
        int score = (Dots-3)*100;
        String scoremsg = "Score:"+Integer.toString(score);
        Font small = new Font("Helvetica", Font.BOLD, 14 );
        FontMetrics fontMetrics = getFontMetrics(small);

        g.setColor(Color.WHITE);
        g.setFont(small);
        g.drawString(msg,(B_Width-fontMetrics.stringWidth(msg))/2 , B_Height/4);
        g.drawString(scoremsg,(B_Width-fontMetrics.stringWidth(scoremsg))/2 , 3*(B_Height/4));
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();

    }

    // make the snake move
    public void move() {
        for(int i=Dots-1;i>0;i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(leftDirection){
            x[0]-=Dot_Size;
        }
        if(rightDirection){
            x[0]+=Dot_Size;
        }
        if(upDirection){
            y[0]-=Dot_Size;
        }
        if(downDirection){
            y[0]+=Dot_Size;
        }

    }
    //Make the snake eat apple
    public void checkApple(){
        if(apple_x == x[0] && apple_y == y[0]){
            Dots++;
            locateApple();
        }
    }
    //Implement controls
    private class TAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent keyEvent){
            int key = keyEvent.getKeyCode();
            if(key==KeyEvent.VK_LEFT &&! rightDirection){
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key==KeyEvent.VK_RIGHT &&! leftDirection){
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if(key==KeyEvent.VK_UP &&! downDirection){
                leftDirection = false;
                upDirection = true;
                rightDirection = false;
            }
            if(key==KeyEvent.VK_DOWN &&! upDirection){
                leftDirection = false;
                rightDirection = false;
                downDirection = true;
            }

        }
    }
}







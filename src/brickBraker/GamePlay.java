package brickBraker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class GamePlay extends JPanel implements ActionListener, KeyListener {

    private boolean play = false; //Jocul nu incepe de unul singur
    private int score = 0; // Se incepe cu scorul 0
    private int totalBricks = 21; // Sunt 21 de casute de spart

    private Timer time;
    private int delay = 8;

    Random random = new Random();
    int n = random.nextInt(2+1-2) - 2; // Start aleatoriu al mingii pe axa X
    private int playerX = 310; // Pozitie de inceput pentru Slider
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXdir = n;
    private int ballYdir = -2;

    private MapGenerator map;

    public GamePlay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        time = new Timer(delay, this); // Viteza este in variabila "delay", iar contextul este "this"
        time.start();
    }

    public void paint(Graphics g) {
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        //drawing map
        map.draw((Graphics2D) g);

        //borders
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, 3, 592); // Left border
        g.fillRect(0, 0, 692, 3); // Upper border
        g.fillRect(682, 0, 3, 592); // Right border

        //score
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        //the paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        //ball
        g.setColor(Color.CYAN);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if(totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(new Color(130,210,10));
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("CONGRATS!", 240, 300);

            g.drawString( "Score: " + score, 270, 350);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Press ENTER to Restart ", 210, 400);
        }

        if(ballPosY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(new Color(130,10,10));
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GAME OVER", 240, 300);

            g.drawString( "Score: " + score, 270, 350);

            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Press ENTER to Restart ", 210, 400);
        }

        g.dispose();
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        time.start();
        if (play) {

            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            A : for (int i = 0; i < map.map.length; i++){ // Primul "map" este din clasa "GamePlay" din al doilea "map" este variabila din clasa "MapGenerator" / A : label
                for(int j = 0; j< map.map[0].length; j++){
                    if(map.map[i][j] > 0){
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;

                        if(ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballPosX + 19 <= brickRect.x || ballPosY + 1 >= brickRect.x + brickRect.width){
                                ballXdir = -ballXdir;
                            } else{
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPosX += ballXdir;
            ballPosY += ballYdir;

            if (ballPosX < 0) {
                ballXdir = -ballXdir;
            }

            if (ballPosY < 0) {
                ballYdir = -ballYdir;
            }

            if (ballPosX > 670) {
                ballXdir = -ballXdir;
            }

        }
        repaint(); // reapelarea metodei paint

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) { //Daca iese din ecran, sa se apeleze functia moveRight care ii va schimba directia
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if (!play) {
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3,7);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

}

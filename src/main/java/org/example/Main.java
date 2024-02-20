package org.example;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main extends JFrame {
    private static final int FRAME_WIDTH = 400;
    private static final int FRAME_HEIGHT = 400;
    private static final int STRING_LENGTH = 6;
    private static final int MOVE_DELAY = 100;
    private static final int SPEED = 10;
    private char[] strArray;
    private Thread[] threads;
    private int[] x;
    private int[] y;
    private int[] dx;
    private int[] dy;
    private JLabel label;



    public Main() {
    initialize();
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(FRAME_WIDTH, FRAME_HEIGHT);
    setLocationRelativeTo(null);
    setVisible(true);
    startAnimation();

    }

    // метод створення потоку для кожного символу
    private Thread createThread(final int index){
        return new Thread(() ->{
           while (true) {
               moveChar(index);
               try {
                   Thread.sleep(MOVE_DELAY);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
        });
    }
    //метод для старту всіх потоків
    private void startAnimation(){
        for (Thread thread : threads){
            thread.start();
        }
    }
    //метод для ініціалізації компонентів
    private void initialize(){
        strArray = new char[STRING_LENGTH];
        threads = new Thread[STRING_LENGTH];
        x = new int[STRING_LENGTH];
        y = new int[STRING_LENGTH];
        dx = new int[STRING_LENGTH];
        dy = new int[STRING_LENGTH];
        for (int i = 0; i < STRING_LENGTH; i++) {
            strArray[i] = randomChars();
            x[i] = 10;
            y[i] = 10;
            dx[i] = SPEED;
            dy[i] = SPEED;
            threads[i] = createThread(i);
        }

        setLayout(null);
        label = createLabel();
        add(label);
    }
    //метод для створення мітки (тектсу) зі змінним розміром
    private JLabel createLabel() {
        JLabel label = new JLabel(new String(strArray) + "   ");
        FontMetrics fm = label.getFontMetrics(label.getFont());
        int width = fm.stringWidth(new String(strArray)) + fm.stringWidth("   ");
        int height = fm.getHeight();
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }

    //метоод для випадкової зміни регістру букв
    private void changeReg(int index){
        Random random = new Random();

        if(random.nextBoolean()) {
            strArray[index] = Character.isUpperCase(strArray[index]) ? Character.toLowerCase(strArray[index]) : Character.toUpperCase(strArray[index]);
            SwingUtilities.invokeLater(() -> {
                label.setText(new String(strArray));
            });
        }
    }
    //метод для переміщення символів
    private synchronized void moveChar(int index){
        x[index] += dx[index];
        y[index] += dy[index];

        if(x[index] < 0 || x[index] + label.getWidth() > FRAME_WIDTH || y[index] < 0 || y[index] + label.getHeight() > FRAME_HEIGHT){
           changeReg(index);
           dx[index] = -dx[index];
           dy[index] = -dy[index];
        }
        SwingUtilities.invokeLater(() -> label.setBounds(x[index], y[index], label.getPreferredSize().width, label.getPreferredSize().height));

    }
    //метод для генерації випадкових символів
    private char randomChars() {
        Random random = new Random();
        return (char) (random.nextInt(26)+'a');
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
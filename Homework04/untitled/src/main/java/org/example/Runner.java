package org.example;

public class Runner extends Thread {
    public static void main(String[] args) {
        new Runner().start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(99999999);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

package Slave;

public class Slave {
    public static void main (String[] args){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int a = 3+5;
        System.out.println(a);
    }
}

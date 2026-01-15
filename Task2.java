public class Task2 {
    public static void main(String args[]) {
    int i;
    for(i=10;i<101;i++){
        if(i==50 || i==60) {
            continue;
        }
        System.out.print(i + " ");
    }
    }
}
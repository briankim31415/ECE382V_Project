public class TryCatchLoopDemo {
    public static void main(String args[]) {
        System.out.println("Start 1");
        
        try {
            for (int i = 0; i < 5; i++) {
                System.out.println("TLC Loop");
            }
        } catch (Exception e) {
            System.out.println("TLC Catch");
        }

        System.out.println("Start 2");

        for (int i = 0; i < 5; i++) {
            try {
                System.out.println("LTC Loop");
            } catch (Exception e) {
                System.out.println("LTC Catch");
            }
        }

        System.out.println("Done");
    }
}
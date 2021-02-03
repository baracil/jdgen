package perococco.jdgen.core;

public class MathTool {

    public static boolean overlap(int x1, int length1, int x2, int length2) {
        return x1<=x2+length2 && x2 <= x1+length1;
    }

    public static int separator(int x1, int length1, int x2, int length2) {
        if (x1>x2+length2) {
            return 0;
        } else if (x1+length1<x2) {
            return 0;
        }

        int d1 = (x1+length1+1) - x2;
        int d2 = (x2+length2+1) - x1;

        if (d2>d1) {
            return d1;
        }
        return -d2;
    }
}

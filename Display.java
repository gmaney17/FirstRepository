public class Display {
    public static int[] convertInput(String s) {
        if(s.length() != 2) {
            return null;
        }
        int[] val = new int[2];
        char c1 = s.charAt(0);
        switch(c1) {
            case 'a':
            val[0] = 0;
            break;
            case 'b':
            val[0] = 1;
            break;
            case 'c':
            val[0] = 2;
            break;
            case 'd':
            val[0] = 3;
            break;
            case 'e':
            val[0] = 4;
            break;
            case 'f':
            val[0] = 5;
            break;
            case 'g':
            val[0] = 6;
            break;
            case 'h':
            val[0] = 7;
            break;
            default:
            return null;
        }
        char c2 = s.charAt(1);
        switch(c2) {
            case '1':
            val[1] = 0;
            break;
            case '2':
            val[1] = 1;
            break;
            case '3':
            val[1] = 2;
            break;
            case '4':
            val[1] = 3;
            break;
            case '5':
            val[1] = 4;
            break;
            case '6':
            val[1] = 5;
            break;
            case '7':
            val[1] = 6;
            break;
            case '8':
            val[1] = 7;
            break;
            default:
            return null;
        }
        return val;
    }

    public static void printDisplay() {
        for(int i = 7; i >= 0; i--) {
            switch(i) {
                case 0:
                System.out.print("1 ");
                break;
                case 1:
                System.out.print("2 ");
                break;
                case 2:
                System.out.print("3 ");
                break;
                case 3:
                System.out.print("4 ");
                break;
                case 4:
                System.out.print("5 ");
                break;
                case 5:
                System.out.print("6 ");
                break;
                case 6:
                System.out.print("7 ");
                break;
                case 7:
                System.out.print("8 ");
                break;
            }
            System.out.println(" "+Game.Tiles[i][0].p+ 
                " "+Game.Tiles[i][1].p+ 
                " "+Game.Tiles[i][2].p+ 
                " "+Game.Tiles[i][3].p+ 
                " "+Game.Tiles[i][4].p+ 
                " "+Game.Tiles[i][5].p+ 
                " "+Game.Tiles[i][6].p+ 
                " "+Game.Tiles[i][7].p);
        }
        System.out.println("   a    b    c    d    e    f    g    h");
    }
}

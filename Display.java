public class Display {
    public static void printDisplay() {
        for(int i = 0; i <= 7; i ++) {
            System.out.println("column 0: "+i+Game.Tiles[i][0].p+ 
                "column 1: "+i+Game.Tiles[i][1].p+ 
                "column 2: "+i+Game.Tiles[i][2].p+ 
                "column 3: "+i+Game.Tiles[i][3].p+ 
                "column 4: "+i+Game.Tiles[i][4].p+ 
                "column 5: "+i+Game.Tiles[i][5].p+ 
                "column 6: "+i+Game.Tiles[i][6].p+ 
                "column 7: "+i+Game.Tiles[i][7].p);
        }
    }
}

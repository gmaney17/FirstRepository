public class Pawn extends piece {
    public Pawn(boolean w) {
        super(w);
    }
    @Override
    public String toString() {
        if(this.isWhite == true) {
            return '\u2659'+"  ";
        } else {
            return "BP  ";
        }
    }
}
public class Knight extends piece {
    public Knight(boolean w) {
        super(w);
    }
    @Override
    public String toString() {
        if(this.isWhite == true) {
            return "WK  ";
        } else {
            return "BK  ";
        }
    }
}
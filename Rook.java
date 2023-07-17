public class Rook extends piece {
    public Rook(boolean w) {
        super(w);
    }
    @Override
    public String toString() {
        if(this.isWhite == true) {
            return "WR  ";
        } else {
            return "BR  ";
        }
    }
}
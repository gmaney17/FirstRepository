public class Queen extends piece {
    public Queen(boolean w) {
        super(w);
    }
    @Override
    public String toString() {
        if(this.isWhite == true) {
            return "WQ  ";
        } else {
            return "BQ  ";
        }
    }
}
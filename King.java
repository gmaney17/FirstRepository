public class King extends piece {
    public King(boolean w) {
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
public class Bishop extends piece {
    public Bishop(boolean w) {
        super(w);
    }
    @Override
    public String toString() {
        if(this.isWhite == true) {
            return "WB  ";
        } else {
            return "BB  ";
        }
    }
}
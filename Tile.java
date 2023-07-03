public class Tile {
    public int row;
    public int column;
    public boolean canEnPassantOnto;
    public boolean isAttackedByWhite;
    public boolean isAttackedByBlack;
    public piece p;


    public Tile(int row, int column, piece p) {
        this.row = row;
        this.column = column;
        this.p = p;
    }

    public Tile(int row, int column) {
        this.row = row;
        this.column = column;
    }
    /**
     * returns true iff there is a piece on this Tile
     */
    public boolean hasPieceOn() {
        if(this.p == null) {
            return false;
        } else {
            return true;
        }
    }  

}
public class Tile {
    public int row;
    public int column;
    public int enPassantCounter; // this gets incremented to ensure a tile cannot get en passanted onto more than one move afterwards
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

    public boolean hasColoredPiece(boolean color) {
        if(this.hasPieceOn()) {
            if(this.p.isWhite == color) {
                return true;
            }
        }
        return false;
    }

    public boolean hasWhitePiece() {
        if(this.hasPieceOn()) {
            if(this.p.isWhite == true) {
                return true;
            }
        }
        return false;
    }

    public boolean hasBlackPiece() {
        if(this.hasPieceOn()) {
            if(this.p.isWhite == false) {
                return true;
            }
        }
        return false;
    }

}
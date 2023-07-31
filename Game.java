import java.util.Scanner;

public class Game {
    public static Tile[][] Tiles = new Tile[8][8];
    public static int inputRow1;
    public static int inputColumn1;
    public static int inputRow2;
    public static int inputColumn2;
    public static boolean isWhiteTurn;
    public static boolean whiteCanLongCastle;
    public static boolean whitePlayedLongCastle;
    public static boolean whiteCanShortCastle;
    public static boolean whitePlayedShortCastle;
    public static boolean blackCanLongCastle;
    public static boolean blackPlayedLongCastle;
    public static boolean blackCanShortCastle;
    public static boolean blackPlayedShortCastle;
    public static Tile enPassantTile;  // tile of the piece which is captured during en passant
    public static Tile whiteKingTile;
    public static Tile blackKingTile;

    public static void main(String[] args) {
        System.out.println("Welcome to my chess game!");
        whiteCanLongCastle = true;
        whiteCanShortCastle = true;
        blackCanLongCastle = true;
        blackCanShortCastle = true;
        Game.makeTiles();
        Game.setupPieces();
        isWhiteTurn = true;
        boolean gameIsOver = false;

        Scanner scanner = new Scanner(System.in);
        while(gameIsOver == false) {
            whitePlayedLongCastle = false;
            whitePlayedShortCastle = false;
            blackPlayedLongCastle = false;
            blackPlayedShortCastle = false;
            // display
            Display.printDisplay();
            // input
            System.out.println("Enter the row of the piece you want to move: ");
            String sInputRow1 = scanner.next();
            boolean caught = false;
            boolean castled = false;
            try {
                inputRow1 = Integer.parseInt(sInputRow1);
            } catch(Exception e) {
                caught = true;
                if(sInputRow1.equals("O-O") == true && isWhiteTurn) {
                    caught = false;
                    whitePlayedShortCastle = true;
                }
                if(sInputRow1.equals("O-O-O") == true && isWhiteTurn) {
                    caught = false;
                    whitePlayedLongCastle = true;
                }
                if(sInputRow1.equals("O-O") == true && isWhiteTurn == false) {
                    caught = false;
                    blackPlayedShortCastle = true;
                }
                if(sInputRow1.equals("O-O-O") == true && isWhiteTurn == false) {
                    caught = false;
                    blackPlayedLongCastle = true;
                }
                if(sInputRow1.equalsIgnoreCase("exit") == true) {
                    break;
                }
            }
            if(caught) {
                System.out.println("Illegal input!");
                continue;
            }
            // code for if white castles and can castle
            if((whitePlayedShortCastle || whitePlayedLongCastle) && canCastle(true)) {
                System.out.println("castled!");
                castled = true;
                castle(true);
                whiteCanLongCastle = false;
                whiteCanShortCastle = false;
                isWhiteTurn = !isWhiteTurn;
                continue;
            } else if((whitePlayedShortCastle || whitePlayedLongCastle) && (canCastle(true) == false)) { // if white castles and cannot castle
                System.out.println("Castling is an Illegal move!");
                continue;
            }

            System.out.println("Enter the column of the piece you want to move: ");
            inputColumn1 = scanner.nextInt();
            if(inputRow1 < 0 || inputRow1 > 7 || inputColumn1 < 0 || inputColumn1 > 7) {
                System.out.println("Illegal move!");
                continue;
            } 
            if(getTile(inputRow1, inputColumn1).p == null) {
                System.out.println("Illegal move!");
                continue;
            }
            if(isWhiteTurn == true && getTile(inputRow1, inputColumn1).p.isWhite == false) {
                System.out.println("Illegal move!");
                continue;
            }
            if(isWhiteTurn == false && getTile(inputRow1, inputColumn1).p.isWhite == true) {
                System.out.println("Illegal move!");
                continue;
            }
            if(castled == false) {
                System.out.println("Enter the row of the tile you want to move to: ");
                inputRow2 = scanner.nextInt();
                 System.out.println("Enter the column of the tile you want to move to: ");
                inputColumn2 = scanner.nextInt();

                // execute move
                if(Game.isLegalMoveExecuteIfLegal() == false) {
                    System.out.println("Illegal move!");
                    continue;
                }
            }
            // moves that disable castling
            if(inputRow1 == 0 && inputColumn1 == 7) {
                whiteCanShortCastle = false;
            }
            if(inputRow1 == 0 && inputColumn1 == 0) {
                whiteCanLongCastle = false;
            }
            if(inputRow1 == 0 && inputColumn1 == 4) {
                whiteCanShortCastle = false;
                whiteCanLongCastle = false;
            }
            if(inputRow1 == 7 && inputColumn1 == 7) {
                blackCanShortCastle = false;
            }
            if(inputRow1 == 7 && inputColumn1 == 0) {
                blackCanLongCastle = false;
            }
            if(inputRow1 == 7 && inputColumn1 == 4) {
                blackCanShortCastle = false;
                blackCanLongCastle = false;
            }
            // check for pawn promotion

            // check for check
            if(isInCheck(true) == true) {
                System.out.println("White is in check!");
                if(isInCheckMate(true)) {
                    System.out.println("Checkmate! Black Wins!");
                    gameIsOver = true;
                }
            }
            if(isInCheck(false) == true) {
                System.out.println("Black is in check!");
                if(isInCheckMate(false)) {
                    System.out.println("Checkmate! White Wins!");
                    gameIsOver = true;
                }
            }
            // flip turns
            isWhiteTurn = !isWhiteTurn;

        }
        scanner.close();



    }

   // pieces
   public static Pawn pWhite = new Pawn(true);
   public static Pawn pBlack = new Pawn(false);
   public static Rook rWhite = new Rook(true);
   public static Rook rBlack = new Rook(false);
   public static Knight knWhite = new Knight(true);
   public static Knight knBlack = new Knight(false);
   public static Bishop bWhite = new Bishop(true);
   public static Bishop bBlack = new Bishop(false);
   public static Queen qWhite = new Queen(true);
   public static Queen qBlack = new Queen(false);
   public static King kWhite = new King(true);
   public static King kBlack = new King(false);


    // 2d array for the Tiles
    public static void makeTiles() {
            for(int i = 0; i < 8; i++) {
                for(int j = 0; j < 8; j++) {
                    Tiles[i][j] = new Tile(i, j);
             }
        }
    }
    
    /**
     * gives rows 1-2 and 7-8 their pieces
     */
    public static void setupPieces() {
        for(int i = 0; i < 8; i++) {
        Tiles[1][i].p = pWhite;
        Tiles[6][i].p = pBlack;
        }
        Tiles[0][0].p = rWhite;
        Tiles[0][1].p = knWhite;
        Tiles[0][2].p = bWhite;
        Tiles[0][3].p = qWhite;
        Tiles[0][4].p = kWhite;
        Tiles[0][5].p = bWhite;
        Tiles[0][6].p = knWhite;
        Tiles[0][7].p = rWhite;

        Tiles[7][0].p = rBlack;
        Tiles[7][1].p = knBlack;
        Tiles[7][2].p = bBlack;
        Tiles[7][3].p = qBlack;
        Tiles[7][4].p = kBlack;
        Tiles[7][5].p = bBlack;
        Tiles[7][6].p = knBlack;
        Tiles[7][7].p = rBlack;

        whiteKingTile = Tiles[0][4];
        blackKingTile = Tiles[7][4];
    }
    /**
     * returns Tile object given row and column
     */
    public static Tile getTile(int row, int column) {
        try{
            return Tiles[row][column];
        } catch(Exception e) {
            System.out.println("Invalid Tile");
            return null;
        }
    }
    public static boolean canCastle(boolean color) {
        if(color && whitePlayedLongCastle && whiteCanLongCastle == false ||
        color && whitePlayedShortCastle && whiteCanShortCastle == false ||
        !color && blackPlayedLongCastle && blackCanLongCastle == false ||
        !color && blackPlayedShortCastle && blackCanShortCastle == false ||
        isInCheck(color) == true) { // cannot castle if in check or if variables are as so
            return false;
        }
        Tile kingTile;
        boolean shortCastlePlayed = false;
        boolean longCastlePlayed = false;
        if(color) { // get values for white if white
            kingTile = whiteKingTile;
            if(whitePlayedLongCastle)
            longCastlePlayed = true;
            if(whitePlayedShortCastle)
            shortCastlePlayed = true;
        } else { // get values for black if black
            kingTile = blackKingTile;
            if(blackPlayedLongCastle)
            longCastlePlayed = true;
            if(blackPlayedShortCastle)
            shortCastlePlayed = true;
        }
        if(shortCastlePlayed == true) { // short castle
            System.out.println("Short castle initiated");
            Tile c5 = getTile(kingTile.row, kingTile.column + 1);
            Tile c6 = getTile(kingTile.row, kingTile.column + 2);
            Tile c7 = getTile(kingTile.row, kingTile.column + 3);
            if(c5.p != null || c6.p != null || c7.p == null) {
                return false;
            }
            if(c7.hasPieceOn()) {
                if(c7.p instanceof Rook == false || c7.p.isWhite != color) {
                    return false;
                }
            }
            if(canSwapPiecesToPreventCheck(c5, kingTile, color) == false ||
            canSwapPiecesToPreventCheck(c6, kingTile, color) == false) {
                return false;
            }
        }
        if(longCastlePlayed == true) { // long castle
            Tile c3 = getTile(kingTile.row, kingTile.column - 1);
            Tile c2 = getTile(kingTile.row, kingTile.column - 2);
            Tile c1 = getTile(kingTile.row, kingTile.column - 3);
            Tile c0 = getTile(kingTile.row, kingTile.column - 4);
            if(c0.p == null || c1.p != null || c2.p != null || c3.p != null) {
                return false;
            }
            if(c0.hasPieceOn()) {
                if(c0.p instanceof Rook == false || c0.p.isWhite != color) {
                    return false;
                }
            }
            if(canSwapPiecesToPreventCheck(c1, kingTile, color) == false ||
            canSwapPiecesToPreventCheck(c2, kingTile, color) == false ||
            canSwapPiecesToPreventCheck(c3, kingTile, color) == false) {
                return false;
            }
        }
        return true;
    }
    /**
     * it is assumed that castling is a legal move
     */
    public static void castle(boolean color) {
        Tile kingTile;
        piece king;
        piece rook;
        boolean shortCastle = false;
        boolean longCastle = false;
        if(color) {
            kingTile = whiteKingTile;
            king = kWhite;
            rook = rWhite;
        } else {
            kingTile = blackKingTile;
            king = kBlack;
            rook = rBlack;
        }
        if(whitePlayedShortCastle || blackPlayedShortCastle) {
            shortCastle = true;
        }
        if(whitePlayedLongCastle || blackPlayedLongCastle) {
            longCastle = true;
        }

        if(shortCastle) {
            Tile c7 = getTile(kingTile.row, kingTile.column + 3);
            Tile c6 = getTile(kingTile.row, kingTile.column + 2);
            Tile c5 = getTile(kingTile.row, kingTile.column + 1);
            c7.p = null;
            c6.p = king;
            c5.p = rook;
            kingTile.p = null;
            if(color) {
                whiteKingTile = c6;
            } else {
                blackKingTile = c6;
            }
        }
        if(longCastle) {
            Tile c3 = getTile(kingTile.row, kingTile.column - 1);
            Tile c2 = getTile(kingTile.row, kingTile.column - 2);
            Tile c1 = getTile(kingTile.row, kingTile.column - 3);
            Tile c0 = getTile(kingTile.row, kingTile.column - 4);
            c0.p = null;
            c1.p = null;
            c2.p = king;
            c3.p = rook;
            kingTile.p = null;
            if(color) {
                whiteKingTile = c2;
            } else {
                blackKingTile = c2;
            }
        }
    }
    /**
     * input and output are assumed to be in the bounds of the board and to not be equal to each other.
     * assumed to be a piece on the tile which is the moving piece.
     * Executes move if legal, then returns true.
     */
    // TODO: EDIT THIS METHOD TO INPUT A BOOLEAN COLOR TO CUT CODE IN HALF
    public static boolean isLegalMoveExecuteIfLegal() {
        int verticalDiff = inputRow2 - inputRow1;
        int HorizontalDiff = inputColumn2 - inputColumn1;
        Tile tile1 = getTile(inputRow1, inputColumn1); // start tile
        Tile tile2 = getTile(inputRow2, inputColumn2); // destination tile
        piece p = tile1.p;

        // verify p is a piece
        if(p == null) {
            return false;
        }

        // if white piece
        if(p.isWhite == true) {
            // if pawn
            if(p instanceof Pawn) {
                // forward move 1 tile
                if((verticalDiff == 1) && (tile2.hasPieceOn() == false) && (HorizontalDiff == 0)) {
                    tile1.p = null;
                    tile2.p = pWhite;
                    if(isInCheck(true) == true) {
                        tile1.p = pWhite;
                        tile2.p = null;
                        return false;
                    }
                    return true;
                }
                // diagonal en passant capture
                if((verticalDiff == 1) && (HorizontalDiff == -1 || HorizontalDiff == 1) && (tile2.canEnPassantOnto)) {
                    tile1.p = null;
                    tile2.p = pWhite;
                    enPassantTile.p = null;
                    if(isInCheck(true) == true) {
                        tile1.p = pWhite;
                        tile2.p = null;
                        enPassantTile.p = pBlack;
                        return false;
                    }
                    return true;
                }
                // diagonal regular capture
                if((verticalDiff == 1) && (HorizontalDiff == -1 || HorizontalDiff == 1) && (tile2.hasPieceOn() == true)) {
                    if(tile2.p.isWhite == false) {
                        piece tempPiece = tile2.p;
                        tile1.p = null;
                        tile2.p = pWhite;
                         if(isInCheck(true) == true) {
                            tile1.p = pWhite;
                            tile2.p = tempPiece;
                            return false;
                        }
                        return true;
                    }
                }
                // forward move 2 tiles, enables en passant onto middle tile
                Tile middleTile = getTile((inputRow1 + 1), inputColumn1);
                if((verticalDiff == 2) && (inputRow1 == 1) && (HorizontalDiff == 0) &&(middleTile.hasPieceOn() == false) &&
                (tile2.hasPieceOn() == false)) {
                    middleTile.canEnPassantOnto = true;
                    Tile tempTile = enPassantTile;
                    enPassantTile = tile2;
                    tile1.p = null;
                    tile2.p = pWhite;
                    if(isInCheck(true) == true) {
                        middleTile.canEnPassantOnto = false;
                        enPassantTile = tempTile;
                        tile1.p = pWhite;
                        tile2.p = null;
                        return false;
                    }
                    return true;
                }
                return false;
            }
            // if knight
            if(p instanceof Knight) {
                // possible movements of knight
                if((verticalDiff == 2) && (HorizontalDiff == 1) || 
                (verticalDiff == 1) && (HorizontalDiff == 2) ||
                (verticalDiff == -1) && (HorizontalDiff == 2) ||
                (verticalDiff == -2) && (HorizontalDiff == 1) ||
                (verticalDiff == -2) && (HorizontalDiff == -1) ||
                (verticalDiff == -1) && (HorizontalDiff == -2) ||
                (verticalDiff == 1) && (HorizontalDiff == -2) ||
                (verticalDiff == 2) && (HorizontalDiff == -1)) {
                    if(tile2.hasPieceOn() == true) {
                        if(tile2.p.isWhite == true) {
                            return false;
                        }
                    }
                    piece tempPiece = tile2.p;
                    tile2.p = knWhite;
                    tile1.p = null;
                    if(isInCheck(true) == true) {
                        tile2.p = tempPiece;
                        tile1.p = knWhite;
                        return false;
                    }
                    return true;
                }
                return false;
            }
            // if bishop or queen
            if(p instanceof Bishop || p instanceof Queen) {
                // upper right
                if(verticalDiff == HorizontalDiff && verticalDiff > 0) {
                    int i = tile1.row + 1;
                    int j = tile1.column + 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i++;
                        j++;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == true) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(true) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }

                // upper Left
                if(HorizontalDiff < 0 && HorizontalDiff + verticalDiff == 0) {
                    int i = tile1.row + 1;
                    int j = tile1.column - 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i++;
                        j--;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == true) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(true) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }

                // lower right
                if(verticalDiff < 0 && HorizontalDiff + verticalDiff == 0) {
                    int i = tile1.row - 1;
                    int j = tile1.column + 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i--;
                        j++;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == true) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(true) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }

                // lower left
                if(HorizontalDiff == verticalDiff && verticalDiff < 0) {
                    int i = tile1.row - 1;
                    int j = tile1.column - 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i--;
                        j--;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == true) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(true) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }

            }
            // if rook or queen
            if(p instanceof Rook || p instanceof Queen) {
                // up
                if(HorizontalDiff == 0 && verticalDiff > 0) {
                    int i = tile1.row + 1;
                    int j = tile1.column;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i++;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == true) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(true) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
                // down
                if(HorizontalDiff == 0 && verticalDiff < 0) {
                    int i = tile1.row - 1;
                    int j = tile1.column;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i--;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == true) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(true) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
                // right
                if(HorizontalDiff > 0 && verticalDiff == 0) {
                    int i = tile1.row;
                    int j = tile1.column + 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        j++;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == true) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(true) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
                // left
                if(HorizontalDiff < 0 && verticalDiff == 0) {
                    int i = tile1.row;
                    int j = tile1.column - 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        j--;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == true) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(true) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
            }
            // if king
            if(p instanceof King) {
                if(HorizontalDiff >= -1 && HorizontalDiff <= 1 && verticalDiff >= -1 && verticalDiff <= 1) {
                    if(tile2.hasPieceOn() == true) {
                        if(tile2.p.isWhite == true) {
                            return false;
                        }
                    }
                    piece temp = tile2.p;
                    tile2.p = tile1.p;
                    tile1.p = null;
                    whiteKingTile = tile2;
                    if(isInCheck(true) == true) {
                        tile1.p = tile2.p;
                        tile2.p = temp;
                        whiteKingTile = tile1;
                        return false;
                    }
                    return true;
                }
            }

        }
        // if black piece

        // if pawn
        if(p instanceof Pawn) {
            // forward move 1 tile
                if((verticalDiff == -1) && (tile2.hasPieceOn() == false) && (HorizontalDiff == 0)) {
                    tile1.p = null;
                    tile2.p = pBlack;
                    if(isInCheck(false) == true) {
                        tile1.p = pBlack;
                        tile2.p = null;
                        return false;
                    }
                    return true;
                }
                // diagonal en passant capture
                if((verticalDiff == -1) && (HorizontalDiff == -1 || HorizontalDiff == 1) && (tile2.canEnPassantOnto)) {
                    tile1.p = null;
                    tile2.p = pBlack;
                    enPassantTile.p = null;
                    if(isInCheck(false) == true) {
                        tile1.p = pBlack;
                        tile2.p = null;
                        enPassantTile.p = pWhite;
                        return false;
                    }
                    return true;
                }
                // diagonal regular capture
                if((verticalDiff == -1) && (HorizontalDiff == -1 || HorizontalDiff == 1) && (tile2.hasPieceOn() == true)) {
                    if(tile2.p.isWhite == true) {
                        piece tempPiece = tile2.p;
                        tile1.p = null;
                        tile2.p = pBlack;
                         if(isInCheck(false) == true) {
                            tile1.p = pBlack;
                            tile2.p = tempPiece;
                            return false;
                        }
                        return true;
                    }
                }
                // forward move 2 tiles, enables en passant onto middle tile
                Tile middleTile = getTile((inputRow1 - 1), inputColumn1);
                if((verticalDiff == -2) && (inputRow1 == 6) && (HorizontalDiff == 0) && (middleTile.hasPieceOn() == false) &&
                (tile2.hasPieceOn() == false)) {
                    middleTile.canEnPassantOnto = true;
                    Tile tempTile = enPassantTile;
                    enPassantTile = tile2;
                    tile1.p = null;
                    tile2.p = pBlack;
                    if(isInCheck(false) == true) {
                        middleTile.canEnPassantOnto = false;
                        enPassantTile = tempTile;
                        tile1.p = pBlack;
                        tile2.p = null;
                        return false;
                    }
                    return true;
                }
                return false;
        }
        // if knight
        if(p instanceof Knight) {

            // possible movements of knight
                if((verticalDiff == 2) && (HorizontalDiff == 1) || 
                (verticalDiff == 1) && (HorizontalDiff == 2) ||
                (verticalDiff == -1) && (HorizontalDiff == 2) ||
                (verticalDiff == -2) && (HorizontalDiff == 1) ||
                (verticalDiff == -2) && (HorizontalDiff == -1) ||
                (verticalDiff == -1) && (HorizontalDiff == -2) ||
                (verticalDiff == 1) && (HorizontalDiff == -2) ||
                (verticalDiff == 2) && (HorizontalDiff == -1)) {
                    if(tile2.hasPieceOn() == true) {
                        if(tile2.p.isWhite == false) {
                            return false;
                        }
                    }
                    piece tempPiece = tile2.p;
                    tile2.p = knBlack;
                    tile1.p = null;
                    if(isInCheck(false) == true) {
                        tile2.p = tempPiece;
                        tile1.p = knBlack;
                        return false;
                    }
                    return true;
                }
            return false;
        }
        // if bishop or queen
        if(p instanceof Bishop || p instanceof Queen) {
            // upper right
            if(verticalDiff == HorizontalDiff && verticalDiff > 0) {
                    int i = tile1.row + 1;
                    int j = tile1.column + 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i++;
                        j++;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == false) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(false) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
            // upper left
            if(HorizontalDiff < 0 && HorizontalDiff + verticalDiff == 0) {
                    int i = tile1.row + 1;
                    int j = tile1.column - 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i++;
                        j--;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == false) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(false) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
            // lower right
            if(verticalDiff < 0 && HorizontalDiff + verticalDiff == 0) {
                    int i = tile1.row - 1;
                    int j = tile1.column + 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i--;
                        j++;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == false) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(false) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }

            // lower left
            if(HorizontalDiff == verticalDiff && verticalDiff < 0) {
                    int i = tile1.row - 1;
                    int j = tile1.column - 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i--;
                        j--;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == false) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(false) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
       
       
        }
        // if rook or queen
        if(p instanceof Rook || p instanceof Queen) {
            // up
            if(HorizontalDiff == 0 && verticalDiff > 0) {
                    int i = tile1.row + 1;
                    int j = tile1.column;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i++;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == false) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(false) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
        
            // down
            if(HorizontalDiff == 0 && verticalDiff < 0) {
                    int i = tile1.row - 1;
                    int j = tile1.column;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        i--;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == false) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(false) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
            // right
            if(HorizontalDiff > 0 && verticalDiff == 0) {
                    int i = tile1.row;
                    int j = tile1.column + 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        j++;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == false) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(false) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
            // left
            if(HorizontalDiff < 0 && verticalDiff == 0) {
                    int i = tile1.row;
                    int j = tile1.column - 1;
                    Tile tempTile = getTile(i, j);
                    while(tempTile.hasPieceOn() == false && tempTile != tile2) {
                        j--;
                        tempTile = getTile(i, j);
                    }
                    if(tempTile == tile2) {
                        if(tile2.hasPieceOn() == true) {
                            if(tile2.p.isWhite == false) {
                                return false;
                            }
                        }
                        piece temp = tile2.p;
                        tile2.p = tile1.p;
                        tile1.p = null;
                        if(isInCheck(false) == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        return true;
                    }
                }
        }
        // if king
        if(p instanceof King) {
            if(HorizontalDiff >= -1 && HorizontalDiff <= 1 && verticalDiff >= -1 && verticalDiff <= 1) {
                    if(tile2.hasPieceOn() == true) {
                        if(tile2.p.isWhite == false) {
                            return false;
                        }
                    }
                    piece temp = tile2.p;
                    tile2.p = tile1.p;
                    tile1.p = null;
                    blackKingTile = tile2;
                    if(isInCheck(false) == true) {
                        tile1.p = tile2.p;
                        tile2.p = temp;
                        blackKingTile = tile1;
                        return false;
                    }
                    return true;
                }
        }

        return false;
    }

    public static boolean isInCheckMate(boolean color) {
        
        int r = 0;
        int c = 0;
        if(color) {
            r = whiteKingTile.row;
            c = whiteKingTile.column;
        } else {
            r = blackKingTile.row;
            c = blackKingTile.column;
        }
        // first check if the king can move to any surrounding tiles
        //
        //
        if(r >= 1) { // downwards
            Tile t = getTile(r - 1, c);
            if(isInCheckMateHelper(t, color) == true) {
                return false;
            }
        }
        if(r <= 6) { // upwards
            Tile t = getTile(r + 1, c);
            if(isInCheckMateHelper(t, color) == true) {
                return false;
            }
        }
        if(c >= 1) { // left
            Tile t = getTile(r, c - 1);
            if(isInCheckMateHelper(t, color) == true) {
                return false;
            }
        }
        if(c <= 6) { // right
            Tile t = getTile(r, c + 1);
            if(isInCheckMateHelper(t, color) == true) {
                return false;
            }
        }
        if(r <= 6 && c <= 6) { // upper right
            Tile t = getTile(r + 1, c + 1);
            if(isInCheckMateHelper(t, color) == true) {
                return false;
            }
        }
        if(r <= 6 && c >= 1) { // upper left
            Tile t = getTile(r + 1, c - 1);
            if(isInCheckMateHelper(t, color) == true) {
                return false;
            }
        }
        if(r >= 1 && c >= 1) { // lower left
            Tile t = getTile(r - 1, c - 1);
            if(isInCheckMateHelper(t, color) == true) {
                return false;
            }
        }
        if(r >= 1 && c <= 6) { // lower right
            Tile t = getTile(r - 1, c + 1);
            if(isInCheckMateHelper(t, color) == true) {
                return false;
            }
        }
        // now see if there are any blocks
        //
        //
        // upwards
        int i = r + 1;
        Tile t = getTile(r, c);
        while(i <= 7 && (t.p == null || t.p instanceof King)) {
            t = getTile(i, c);
            i++;
            if(isInCheckMateHelperTwo(t, color) == true) {
                return false;
            }
        }
        // downwards
        i = r - 1;
        t = getTile(r, c);
        while(i >= 0 && (t.p == null || t.p instanceof King)) {
            t = getTile(i, c);
            i--;
            if(isInCheckMateHelperTwo(t, color) == true) {
                return false;
            }
        }
        // right
        int j = c + 1;
        t = getTile(r, c);
        while(j <= 7 && (t.p == null || t.p instanceof King)) {
            t = getTile(r, j);
            j++;
            if(isInCheckMateHelperTwo(t, color) == true) {
                return false;
            }
        }
        // left
        j = c - 1;
        t = getTile(r, c);
        while(j >= 0 && (t.p == null || t.p instanceof King)) {
            t = getTile(r, j);
            j--;
            if(isInCheckMateHelperTwo(t, color) == true) {
                return false;
            }
        }
        // up right
        i = r + 1;
        j = c + 1;
        t = getTile(r, c);
        while(i <= 7 && j <= 7 && (t.p == null || t.p instanceof King)) {
            t = getTile(i, j);
            i++;
            j++;
            if(isInCheckMateHelperTwo(t, color) == true) {
                return false;
            }
        }
        // up left
        i = r + 1;
        j = c - 1;
        t = getTile(r, c);
        while(i <= 7 && j >= 0 && (t.p == null || t.p instanceof King)) {
            t = getTile(i, j);
            i++;
            j--;
            if(isInCheckMateHelperTwo(t, color) == true) {
                return false;
            }
        }
        // down right
        i = r - 1;
        j = c + 1;
        t = getTile(r, c);
        while(i >= 0 && j <= 7 && (t.p == null || t.p instanceof King)) {
            t = getTile(i, j);
            i--;
            j++;
            if(isInCheckMateHelperTwo(t, color) == true) {
                return false;
            }
        }
        // down left
        i = r - 1;
        j = c - 1;
        t = getTile(r, c);
        while(i >= 0 && j >= 0 && (t.p == null || t.p instanceof King)) {
            t = getTile(i, j);
            i--;
            j--;
            if(isInCheckMateHelperTwo(t, color) == true) {
                return false;
            }
        }
        // now check the knight tiles if they are in bounds
        if(knightsCausedCheckMateHelper(2, 1, r, c, true) == true) { // up 2 right 1
            return false;
        }
        if(knightsCausedCheckMateHelper(1, 2, r, c, true) == true) { // up 1 right 2
            return false;
        }
        if(knightsCausedCheckMateHelper(-1, 2, r, c, true) == true) { // down 1 right 2
            return false;
        }
        if(knightsCausedCheckMateHelper(-2, 1, r, c, true) == true) { // down 2 right 1
            return false;
        }
        if(knightsCausedCheckMateHelper(-2, -1, r, c, true) == true) { // down 2 left 1
            return false;
        }
        if(knightsCausedCheckMateHelper(-1, -2, r, c, true) == true) { // down 1 left 2
            return false;
        }
        if(knightsCausedCheckMateHelper(1, -2, r, c, true) == true) { // up 1 left 2
            return false;
        }
        if(knightsCausedCheckMateHelper(2, -1, r, c, true) == true) { // up 2 left 1
            return false;
        }
        return true;
    }
    /**
     * Looks for a knight which is attacking either white or black's king,
     * if it finds the knight then it calls either of the isInCheckMateHelperTwo methods to see if any piece can capture
     * the knight and take the king out of check
     */
    private static boolean knightsCausedCheckMateHelper(int upDiff, int sideDiff, int r, int c, boolean color) {
        boolean otherTeam = !color;
        if(r + upDiff >= 0 && 
        r + upDiff <= 7 &&
        c + sideDiff >= 0 &&
        c + sideDiff <= 7) {
            Tile t = getTile(r + upDiff, c + sideDiff);
            if(t.p != null) {
                if(t.p.isWhite == otherTeam && t.p instanceof Knight) {
                    if(isInCheckMateHelperTwo(t, color)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * returns true iff king can move to t and not be in check
     */
    public static boolean isInCheckMateHelper(Tile t, boolean color) {
        Tile kingTile;
        piece king;
        if(color) {
            kingTile = whiteKingTile;
            king = kWhite;
        } else {
            kingTile = blackKingTile;
            king = kBlack;
        }

        if(t.p != null) { // if same color piece
            if(t.p.isWhite == color) {
                return false;
            }
        }
        // if null or opposite color piece(can move)
        piece temp = t.p;
        t.p = king;
        kingTile.p = null;
        if(color == true) {
            whiteKingTile = t;
        } else {
            blackKingTile = t;
        }
        if(isInCheck(color) == true) {
            if(color == true) {
                whiteKingTile = kingTile;
                whiteKingTile.p = kWhite;
                t.p = temp;
                return false;
            } else {
                blackKingTile = kingTile;
                blackKingTile.p = kBlack;
                t.p = temp;
                return false;
            }
            
        } else {
            if(color == true) {
                whiteKingTile = kingTile;
                whiteKingTile.p = kWhite;
                t.p = temp;
                return true;
            } else {
                blackKingTile = kingTile;
                blackKingTile.p = kBlack;
                t.p = temp;
                return true;
            }
        }
    }
    /**
     * returns true iff there is a same colored piece that can move onto this tile to prevent check
     */
    public static boolean isInCheckMateHelperTwo(Tile t, boolean color) {
        int r = t.row;
        int c = t.column;
        Tile original = t;
        // up + pawn if black and one tile away
        if(r <= 6) {
            int i = r + 1;
            t = getTile(i, c);
            while(i <= 7 && t.p == null) {
                t = getTile(i, c);
                i++;
            }
            if(t.p != null) {
                if(t.p.isWhite == color && ((t.p instanceof Rook || t.p instanceof Queen) || 
                (t.row == r + 1 && t.p instanceof Pawn && t.p.isWhite == false && color == false))) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                } 
            }
        }
        // down + pawn if white and one tile away 
        if(r >= 1) {
            int i = r - 1;
            t = getTile(i, c);
            while(i >= 0 && t.p == null) {
                t = getTile(i, c);
                i--;
            }
            if(t.p != null) {
                if(t.p.isWhite == color && ((t.p instanceof Rook || t.p instanceof Queen) || 
                (t.row == r - 1 && t.p instanceof Pawn && t.p.isWhite == true && color == true))) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                } 
            }
        }
        // right
        if(c <= 6) {
            int j = c + 1;
            t = getTile(r, j);
            while(j <= 7 && t.p == null) {
                t = getTile(r, j);
                j++;
            }
            if(t.p != null) {
                if((t.p instanceof Rook || t.p instanceof Queen) && t.p.isWhite == color) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                } 
            }
        }
        // left
        if(c >= 1) {
            int j = c - 1;
            t = getTile(r, j);
            while(j >= 0 && t.p == null) {
                t = getTile(r, j);
                j--;
            }
            if(t.p != null) {
                if((t.p instanceof Rook || t.p instanceof Queen) && t.p.isWhite == color) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                } 
            }
        }
        // up right + black pawn if capture
        if(r <= 6 && c <= 6) {
            int i = r + 1;
            int j = c + 1;
            t = getTile(i, j);
            if(original.p != null) { // pawn if it can capture
                if(!color && t.p instanceof Pawn && original.p.isWhite == false) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                }
            }
            while(i <= 7 && j <= 7 && t.p == null) {
                t = getTile(r, j);
                j++;
                i++;
            }
            if(t.p != null) {
                if((t.p instanceof Bishop || t.p instanceof Queen) && t.p.isWhite == color) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                } 
            }
        }
        // up left + black pawn if capture
        if(r <= 6 && c >= 1) {
            int i = r + 1;
            int j = c - 1;
            t = getTile(i, j);
            if(original.p != null) { // pawn if it can capture
                if(!color && t.p instanceof Pawn && original.p.isWhite == false) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                }
            }
            while(i <= 7 && j >= 0 && t.p == null) {
                t = getTile(r, j);
                j--;
                i++;
            }
            if(t.p != null) {
                if((t.p instanceof Bishop || t.p instanceof Queen) && t.p.isWhite == color) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                } 
            }
        }
        // down right + white pawn if capture
        if(r >= 1 && c <= 6) {
            int i = r - 1;
            int j = c + 1;
            t = getTile(i, j);
            if(original.p != null) { // pawn if it can capture
                if(color && t.p instanceof Pawn && original.p.isWhite == true) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                }
            }
            while(i >= 0 && j <= 7 && t.p == null) {
                t = getTile(r, j);
                j++;
                i--;
            }
            if(t.p != null) {
                if((t.p instanceof Bishop || t.p instanceof Queen) && t.p.isWhite == color) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                } 
            }
        }
        // down left + white pawn if capture
        if(r >= 1 && c >= 1) {
            int i = r - 1;
            int j = c - 1;
            t = getTile(i, j);
            if(original.p != null) { // pawn if it can capture
                if(color && t.p instanceof Pawn && original.p.isWhite == true) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                }
            }
            while(i >= 0 && j >= 0 && t.p == null) {
                t = getTile(r, j);
                j--;
                i--;
            }
            if(t.p != null) {
                if((t.p instanceof Bishop || t.p instanceof Queen) && t.p.isWhite == true) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                } 
            }
        }
        // knights
        if(knightCanBlockMate(color, r, c, 2, 1, original)) { // up 2 right 1
            return true;
        }
        if(knightCanBlockMate(color, r, c, 1, 2, original)) { // up 1 right 2
            return true;
        }
        if(knightCanBlockMate(color, r, c, -1, 2, original)) { // down 1 right 2
            return true;
        }
        if(knightCanBlockMate(color, r, c, -2, 1, original)) { // down 2 right 1
            return true;
        }
        if(knightCanBlockMate(color, r, c, -2, -1, original)) { // down 2 left 1
            return true;
        }
        if(knightCanBlockMate(color, r, c, -1, -2, original)) { // down 1 left 2
            return true;
        }
        if(knightCanBlockMate(color, r, c, 1, -2, original)) { // up 1 left 2
            return true;
        }
        if(knightCanBlockMate(color, r, c, 2, -1, original)) { // up 2 left 1
            return true;
        }

        return false;
    }
    /**
     * 
     * @param original The tile which is going to get t's piece put onto it
     * @param t The tile whose piece will go onto original, and then see if it prevents check
     * @param color true if white, false if black
     * @return true iff swapping t's piece onto original will get color's king out of check
     */
    public static boolean canSwapPiecesToPreventCheck(Tile original, Tile t, boolean color) {
        piece temp = original.p;
        original.p = t.p;
        t.p = null;
        if(isInCheck(color) == false) {
            t.p = original.p;
            original.p = temp;
            return true;
        } else {
            t.p = original.p;
            original.p = temp;
            return false;
        }
    }

    public static boolean knightCanBlockMate(boolean color, int r, int c, int upDiff, int sideDiff, Tile original) {
        if(r + upDiff >= 0 && 
        r + upDiff <= 7 &&
        c + sideDiff >= 0 &&
        c + sideDiff <= 7) {
            Tile t = getTile(r + upDiff, c + sideDiff);
            if(t.p != null) {
                if(t.p.isWhite == color && t.p instanceof Knight) {
                    if(canSwapPiecesToPreventCheck(original, t, color)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // maybe make a helper method for the knights in isInCheck
    public static boolean isInCheck(boolean color) {
        int r;
        int c;
        if(color) {
            r = whiteKingTile.row;
            c = whiteKingTile.column;
        } else {
            r = blackKingTile.row;
            c = blackKingTile.column;
        }
        // straights
        if(r >= 1) { // downwards
            int i = r - 1;
            Tile t = getTile(i, c);
            while(i >= 0 && t.p == null) {
                t = getTile(i, c);
                i--;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Queen || t.p instanceof Rook)) {
                    return true;
                }
            }
        }
        if(r <= 6) { // upwards
            int i = r + 1;
            Tile t = getTile(i, c);
            while(i <= 7 && t.p == null) {
                t = getTile(i, c);
                i++;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Queen || t.p instanceof Rook)) {
                    return true;
                }
            }
        }
        if(c >= 1) { // left
            int i = c - 1;
            Tile t = getTile(r, i);
            while(i >= 0 && t.p == null) {
                t = getTile(r, i);
                i--;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Queen || t.p instanceof Rook)) {
                    return true;
                }
            }
        }
        if(c <= 6) { // right
            int i = c + 1;
            Tile t = getTile(r, i);
            while(i <= 7 && t.p == null) {
                t = getTile(r, i);
                i++;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Queen || t.p instanceof Rook)) {
                    return true;
                }
            }
        }
        // diagonals
        if(c >= 1 && r >= 1) { // lower left
            int i = r - 1;
            int j = c - 1;
            Tile t = getTile(i, j);
            if(t.hasPieceOn() == true) { // if pawn check
                if(!color && t.p.isWhite == !color && (t.p instanceof Pawn)) {
                    return true;
                }
            }
            while(i >= 0 && j >= 0 && t.p == null) {
                t = getTile(i, j);
                i--;
                j--;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        if(c <= 6 && r >= 1) { // lower right
            int i = r - 1;
            int j = c + 1;
            Tile t = getTile(i, j);
            if(t.hasPieceOn() == true) { // if pawn check
                if(!color && t.p.isWhite == !color && (t.p instanceof Pawn)) {
                    return true;
                }
            }
            while(i >= 0 && j <= 7 && t.p == null) {
                t = getTile(i, j);
                i--;
                j++;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        if(c >= 1 && r <= 6) { // upper left
            int i = r + 1;
            int j = c - 1;
            Tile t = getTile(i, j);

            if(t.hasPieceOn() == true) { // if pawn check
                if(color && t.p.isWhite == !color && (t.p instanceof Pawn)) {
                    return true;
                }
            }
            while(i <= 7 && j >= 0 && t.p == null) {
                t = getTile(i, j);
                i++;
                j--;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        if(c <= 6 && r <= 6) { // upper right
            int i = r + 1;
            int j = c + 1;
            Tile t = getTile(i, j);

            if(t.hasPieceOn() == true) { // if pawn check
                if(color && t.p.isWhite == !color && (t.p instanceof Pawn)) {
                    return true;
                }
            }
            while(i <= 7 && j <= 7 && t.p == null) {
                t = getTile(i, j);
                i++;
                j++;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        // knights
        if(r + 2 <= 7 && c + 1 <= 7) { // up 2 right 1
            Tile t = getTile(r + 2, c + 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r + 1 <= 7 && c + 2 <= 7) { // up 1 right 2
            Tile t = getTile(r + 1, c + 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 1 >= 0 && c + 2 <= 7) { // down 1 right 2
            Tile t = getTile(r - 1, c + 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 2 >= 0 && c + 1 <= 7) { // down 2 right 1
            Tile t = getTile(r - 2, c + 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 2 >= 0 && c - 1 >= 0) { // down 2 left 1
            Tile t = getTile(r - 2, c - 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 1 >= 0 && c - 2 >= 0) { // down 1 left 2
            Tile t = getTile(r - 1, c - 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r + 1 <= 7 && c - 2 >= 0) { // up 1 left 2
            Tile t = getTile(r + 1, c - 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r + 2 <= 7 && c - 1 >= 0) { // up 2 left 1
            Tile t = getTile(r + 2, c - 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == !color && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }

        return false;
    }

}
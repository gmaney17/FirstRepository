import java.util.Scanner;

public class Game {
    public static Tile[][] Tiles = new Tile[8][8];
    public static int inputRow1;
    public static int inputColumn1;
    public static int inputRow2;
    public static int inputColumn2;
    public static boolean isWhiteTurn;
    public static boolean whiteCanCastle;
    public static boolean blackCanCastle;
    public static Tile enPassantTile;  // tile of the piece which is captured during en passant
    public static Tile whiteKingTile;
    public static Tile blackKingTile;

    public static void main(String[] args) {
        System.out.println("Welcome to my chess game!");
        Game.whiteCanCastle = true;
        Game.blackCanCastle = true;
        Game.makeTiles();
        Game.setupPieces();
        isWhiteTurn = true;
        boolean gameIsOver = false;

        Scanner scanner = new Scanner(System.in);
        while(gameIsOver == false) {

            // display
            int k = 0;
            while(k <= 7) {
                System.out.println("column 0: "+k+Tiles[k][0].p+ 
                "column 1: "+k+Tiles[k][1].p+ 
                "column 2: "+k+Tiles[k][2].p+ 
                "column 3: "+k+Tiles[k][3].p+ 
                "column 4: "+k+Tiles[k][4].p+ 
                "column 5: "+k+Tiles[k][5].p+ 
                "column 6: "+k+Tiles[k][6].p+ 
                "column 7: "+k+Tiles[k][7].p);
                k++;
            }

            // input
            System.out.println("Enter the row of the piece you want to move: ");
            inputRow1 = scanner.nextInt();
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

            System.out.println("Enter the row of the tile you want to move to: ");
            inputRow2 = scanner.nextInt();
            System.out.println("Enter the column of the tile you want to move to: ");
            inputColumn2 = scanner.nextInt();

            // execute move
            if(Game.isLegalMoveExecuteIfLegal() == false) {
                System.out.println("Illegal move!");
                continue;
            }

            // check for pawn promotion

            // check for check
            if(whiteIsInCheck() == true) {
                System.out.println("White is in check!");
                if(whiteIsInCheckMate() == true) {
                    System.out.println("Checkmate! Black Wins!");
                    gameIsOver = true;
                }
            }
            if(blackIsInCheck() == true) {
                System.out.println("Black is in check!");
            }
            // flip turns
            isWhiteTurn = !isWhiteTurn;

        }




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
    /**
     * input and output are assumed to be in the bounds of the board and to not be equal to each other.
     * assumed to be a piece on the tile which is the moving piece.
     * Executes move if legal, then returns true.
     */
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
                    if(Game.whiteIsInCheck() == true) {
                        tile1.p = pWhite;
                        tile2.p = null;
                        return false;
                    }
                    Game.updatePawnAttackingTiles(tile1, tile2, true);
                    return true;
                }
                // diagonal en passant capture
                if((verticalDiff == 1) && (HorizontalDiff == -1 || HorizontalDiff == 1) && (tile2.canEnPassantOnto)) {
                    tile1.p = null;
                    tile2.p = pWhite;
                    enPassantTile.p = null;
                    if(Game.whiteIsInCheck() == true) {
                        tile1.p = pWhite;
                        tile2.p = null;
                        enPassantTile.p = pBlack;
                        return false;
                    }
                    Game.updatePawnAttackingTiles(tile1, tile2, true);
                    return true;
                }
                // diagonal regular capture
                if((verticalDiff == 1) && (HorizontalDiff == -1 || HorizontalDiff == 1) && (tile2.hasPieceOn() == true)) {
                    if(tile2.p.isWhite == false) {
                        piece tempPiece = tile2.p;
                        tile1.p = null;
                        tile2.p = pWhite;
                         if(Game.whiteIsInCheck() == true) {
                            tile1.p = pWhite;
                            tile2.p = tempPiece;
                            return false;
                        }
                        Game.updatePawnAttackingTiles(tile1, tile2, true);
                        return true;
                    }
                }
                // forward move 2 tiles, enables en passant onto middle tile
                Tile middleTile = getTile((inputRow1 + 1), inputColumn1);
                if((verticalDiff == 2) && (inputRow1 == 1) && (middleTile.hasPieceOn() == false) &&
                (tile2.hasPieceOn() == false)) {
                    middleTile.canEnPassantOnto = true;
                    Tile tempTile = enPassantTile;
                    enPassantTile = tile2;
                    tile1.p = null;
                    tile2.p = pWhite;
                    if(Game.whiteIsInCheck() == true) {
                        middleTile.canEnPassantOnto = false;
                        enPassantTile = tempTile;
                        tile1.p = pWhite;
                        tile2.p = null;
                        return false;
                    }
                    Game.updatePawnAttackingTiles(tile1, tile2, true);
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
                    if(Game.whiteIsInCheck() == true) {
                        tile2.p = tempPiece;
                        tile1.p = knWhite;
                        return false;
                    }
                    Game.updateKnightAttackingTiles(tile1, tile2, true);
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
                        if(Game.whiteIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Bishop)
                        Game.updateBishopAttackingTiles(tile1, tile2, true);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, true);
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
                        if(Game.whiteIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Bishop)
                        Game.updateBishopAttackingTiles(tile1, tile2, true);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, true);
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
                        if(Game.whiteIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Bishop)
                        Game.updateBishopAttackingTiles(tile1, tile2, true);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, true);
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
                        if(Game.whiteIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Bishop)
                        Game.updateBishopAttackingTiles(tile1, tile2, true);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, true);
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
                        if(Game.whiteIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Rook)
                        Game.updateRookAttackingTiles(tile1, tile2, true);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, true);
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
                        if(Game.whiteIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Rook)
                        Game.updateRookAttackingTiles(tile1, tile2, true);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, true);
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
                        if(Game.whiteIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Rook)
                        Game.updateRookAttackingTiles(tile1, tile2, true);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, true);
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
                        if(Game.whiteIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Rook)
                        Game.updateRookAttackingTiles(tile1, tile2, true);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, true);
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
                    if(Game.whiteIsInCheck() == true) {
                        tile1.p = tile2.p;
                        tile2.p = temp;
                        whiteKingTile = tile1;
                        return false;
                    }
                    Game.updateKingAttackingTiles(tile1, tile2, true);
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
                    if(Game.blackIsInCheck() == true) {
                        tile1.p = pBlack;
                        tile2.p = null;
                        return false;
                    }
                    Game.updatePawnAttackingTiles(tile1, tile2, false);
                    return true;
                }
                // diagonal en passant capture
                if((verticalDiff == -1) && (HorizontalDiff == -1 || HorizontalDiff == 1) && (tile2.canEnPassantOnto)) {
                    tile1.p = null;
                    tile2.p = pBlack;
                    enPassantTile.p = null;
                    if(Game.blackIsInCheck() == true) {
                        tile1.p = pBlack;
                        tile2.p = null;
                        enPassantTile.p = pWhite;
                        return false;
                    }
                    Game.updatePawnAttackingTiles(tile1, tile2, false);
                    return true;
                }
                // diagonal regular capture
                if((verticalDiff == -1) && (HorizontalDiff == -1 || HorizontalDiff == 1) && (tile2.hasPieceOn() == true)) {
                    if(tile2.p.isWhite == true) {
                        piece tempPiece = tile2.p;
                        tile1.p = null;
                        tile2.p = pBlack;
                         if(Game.blackIsInCheck() == true) {
                            tile1.p = pBlack;
                            tile2.p = tempPiece;
                            return false;
                        }
                        Game.updatePawnAttackingTiles(tile1, tile2, false);
                        return true;
                    }
                }
                // forward move 2 tiles, enables en passant onto middle tile
                Tile middleTile = getTile((inputRow1 - 1), inputColumn1);
                if((verticalDiff == -2) && (inputRow1 == 6) && (middleTile.hasPieceOn() == false) &&
                (tile2.hasPieceOn() == false)) {
                    middleTile.canEnPassantOnto = true;
                    Tile tempTile = enPassantTile;
                    enPassantTile = tile2;
                    tile1.p = null;
                    tile2.p = pBlack;
                    if(Game.blackIsInCheck() == true) {
                        middleTile.canEnPassantOnto = false;
                        enPassantTile = tempTile;
                        tile1.p = pBlack;
                        tile2.p = null;
                        return false;
                    }
                    Game.updatePawnAttackingTiles(tile1, tile2, false);
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
                    if(Game.blackIsInCheck() == true) {
                        tile2.p = tempPiece;
                        tile1.p = knBlack;
                        return false;
                    }
                    Game.updateKnightAttackingTiles(tile1, tile2, false);
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
                        if(Game.blackIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Bishop)
                        Game.updateBishopAttackingTiles(tile1, tile2, false);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, false);
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
                        if(Game.blackIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Bishop)
                        Game.updateBishopAttackingTiles(tile1, tile2, false);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, false);
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
                        if(Game.blackIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Bishop)
                        Game.updateBishopAttackingTiles(tile1, tile2, false);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, false);
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
                        if(Game.blackIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Bishop)
                        Game.updateBishopAttackingTiles(tile1, tile2, false);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, false);
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
                        if(Game.blackIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Rook)
                        Game.updateRookAttackingTiles(tile1, tile2, false);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, false);
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
                        if(Game.blackIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Rook)
                        Game.updateRookAttackingTiles(tile1, tile2, false);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, false);
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
                        if(Game.blackIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Rook)
                        Game.updateRookAttackingTiles(tile1, tile2, false);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, false);
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
                        if(Game.blackIsInCheck() == true) {
                            tile1.p = tile2.p;
                            tile2.p = temp;
                            return false;
                        }
                        if(p instanceof Rook)
                        Game.updateRookAttackingTiles(tile1, tile2, false);
                        if(p instanceof Queen)
                        Game.updateQueenAttackingTiles(tile1, tile2, false);
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
                    if(Game.blackIsInCheck() == true) {
                        tile1.p = tile2.p;
                        tile2.p = temp;
                        blackKingTile = tile1;
                        return false;
                    }
                    Game.updateKingAttackingTiles(tile1, tile2, false);
                    return true;
                }
        }

        return false;
    }

    public static boolean whiteIsInCheckMate() {
        Tile kingTile = whiteKingTile;
        int r = whiteKingTile.row;
        int c = whiteKingTile.column;
        // first check if the king can move to any surrounding tiles
        //
        //
        if(r >= 1) { // downwards
            Tile t = getTile(r - 1, c);
            if(whiteIsInCheckMateHelper(t) == true) {
                return false;
            }
        }
        if(r <= 6) { // upwards
            Tile t = getTile(r + 1, c);
            if(whiteIsInCheckMateHelper(t) == true) {
                return false;
            }
        }
        if(c >= 1) { // left
            Tile t = getTile(r, c - 1);
            if(whiteIsInCheckMateHelper(t) == true) {
                return false;
            }
        }
        if(c <= 6) { // right
            Tile t = getTile(r, c + 1);
            if(whiteIsInCheckMateHelper(t) == true) {
                return false;
            }
        }
        if(r <= 6 && c <= 6) { // upper right
            Tile t = getTile(r + 1, c + 1);
            if(whiteIsInCheckMateHelper(t) == true) {
                return false;
            }
        }
        if(r <= 6 && c >= 1) { // upper left
            Tile t = getTile(r + 1, c - 1);
            if(whiteIsInCheckMateHelper(t) == true) {
                return false;
            }
        }
        if(r >= 1 && c >= 1) { // lower left
            Tile t = getTile(r - 1, c - 1);
            if(whiteIsInCheckMateHelper(t) == true) {
                return false;
            }
        }
        if(r >= 1 && c <= 6) { // lower right
            Tile t = getTile(r - 1, c + 1);
            if(whiteIsInCheckMateHelper(t) == true) {
                return false;
            }
        }
        // now see if there are any blocks
        //
        //
        // upwards
        boolean upwardsCheck = false;
        int i = r + 1;
        Tile t = getTile(i, c);
        while(i <= 7 && t.p == null) {
            t = getTile(i, c);
            i++;
        }
        if(t.hasPieceOn() == true) {
             if(t.p.isWhite == false && (t.p instanceof Queen || t.p instanceof Rook)) {
              upwardsCheck = true;
            }
        }
        if(upwardsCheck == true) {
            int i = r + 1;
            Tile t = getTile(i, c);
            while(i <= 7 && t.p == null) {
                t = getTile(i, c);
                i++;
                if(whiteIsInCheckMateHelperTwo(t) == true) {
                    return false;
                }
            }

        }



        return true;
    }
    /**
     * returns true iff king can move to t and not be in check
     */
    public static boolean whiteIsInCheckMateHelper(Tile t) {
        Tile kingTile = whiteKingTile;

        if(t.p != null) { // if white piece
            if(t.p.isWhite == true) {
                return false;
            }
        }
        // if null or black piece(can move)
        piece temp = t.p;
        t.p = kWhite;
        whiteKingTile.p = temp;
        whiteKingTile = t;
        if(whiteIsInCheck() == true) {
            whiteKingTile = kingTile;
            whiteKingTile.p = kWhite;
            t.p = temp;
            return false;
        } else {
            return true;
        }
    }
    /**
     * returns true iff there is a white piece that can move onto this tile to prevent check
     */
    public static boolean whiteIsInCheckMateHelperTwo(Tile t) {
        int r = t.row;
        int c = t.column;
        // up
        if(r <= 6) {
            int i = r + 1;
            Tile t = getTile(i, c);
            while(i <= 7 && t.p == null) {
                t = getTile(i, c);
                i++;
            }
            if((t.p instanceof Rook || t.p instanceof Queen) && t.p.isWhite == true) {
                return true;
            } 
        }
        // down + pawn if one tile away 
        if(r >= 1) {
            int i = r - 1;
            Tile t = getTile(i, c);
            while(i >= 0 && t.p == null) {
                t = getTile(i, c);
                i--;
            }
            if(t.p.isWhite == true && ((t.p instanceof Rook || t.p instanceof Queen) || (t.row == r - 1 && t.p instanceof pawn))) {
                return true;
            } 
        }
        // right

        // left

        // up right

        // up left

        // down right + pawn if capture

        // down left + pawn if capture

        // knights
    }

    public static boolean whiteIsInCheck() {
        int r = whiteKingTile.row;
        int c = whiteKingTile.column;
        // straights
        if(r >= 1) { // downwards
            int i = r - 1;
            Tile t = getTile(i, c);
            while(i >= 0 && t.p == null) {
                t = getTile(i, c);
                i--;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Queen || t.p instanceof Rook)) {
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
                if(t.p.isWhite == false && (t.p instanceof Queen || t.p instanceof Rook)) {
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
                if(t.p.isWhite == false && (t.p instanceof Queen || t.p instanceof Rook)) {
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
                if(t.p.isWhite == false && (t.p instanceof Queen || t.p instanceof Rook)) {
                    return true;
                }
            }
        }
        // diagonals
        if(c >= 1 && r >= 1) { // lower left
            int i = r - 1;
            int j = c - 1;
            Tile t = getTile(i, j);
            while(i >= 0 && j >= 0 && t.p == null) {
                t = getTile(i, j);
                i--;
                j--;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        if(c <= 6 && r >= 1) { // lower right
            int i = r - 1;
            int j = c + 1;
            Tile t = getTile(i, j);
            while(i >= 0 && j <= 7 && t.p == null) {
                t = getTile(i, j);
                i--;
                j++;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        if(c >= 1 && r <= 6) { // upper left
            int i = r + 1;
            int j = c - 1;
            Tile t = getTile(i, j);

            if(t.hasPieceOn() == true) { // if pawn check
                if(t.p.isWhite == false && (t.p instanceof Pawn)) {
                    return true;
                }
            }
            while(i <= 7 && j >= 0 && t.p == null) {
                t = getTile(i, j);
                i++;
                j--;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        if(c <= 6 && r<= 6) { // upper right
            int i = r + 1;
            int j = c + 1;
            Tile t = getTile(i, j);

            if(t.hasPieceOn() == true) { // if pawn check
                if(t.p.isWhite == false && (t.p instanceof Pawn)) {
                    return true;
                }
            }
            while(i <= 7 && j <= 7 && t.p == null) {
                t = getTile(i, j);
                i++;
                j++;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        // knights
        if(r + 2 <= 7 && c + 1 <= 7) { // up 2 right 1
            Tile t = getTile(r + 2, c + 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r + 1 <= 7 && c + 2 <= 7) { // up 1 right 2
            Tile t = getTile(r + 1, c + 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 1 >= 0 && c + 2 <= 7) { // down 1 right 2
            Tile t = getTile(r - 1, c + 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 2 >= 0 && c + 1 <= 7) { // down 2 right 1
            Tile t = getTile(r - 2, c + 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 2 >= 0 && c - 1 >= 0) { // down 2 left 1
            Tile t = getTile(r - 2, c - 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 1 >= 0 && c - 2 >= 0) { // down 1 left 2
            Tile t = getTile(r - 1, c - 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r + 1 <= 7 && c - 2 >= 0) { // up 1 left 2
            Tile t = getTile(r + 1, c - 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r + 2 <= 7 && c - 1 >= 0) { // up 2 left 1
            Tile t = getTile(r + 2, c - 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == false && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean blackIsInCheck() {
        int r = blackKingTile.row;
        int c = blackKingTile.column;
        // straights
        if(r >= 1) { // downwards
            int i = r - 1;
            Tile t = getTile(i, c);
            while(i >= 0 && t.p == null) {
                t = getTile(i, c);
                i--;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Queen || t.p instanceof Rook)) {
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
                if(t.p.isWhite == true && (t.p instanceof Queen || t.p instanceof Rook)) {
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
                if(t.p.isWhite == true && (t.p instanceof Queen || t.p instanceof Rook)) {
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
                if(t.p.isWhite == true && (t.p instanceof Queen || t.p instanceof Rook)) {
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
                if(t.p.isWhite == true && (t.p instanceof Pawn)) {
                    return true;
                }
            }
            while(i >= 0 && j >= 0 && t.p == null) {
                t = getTile(i, j);
                i--;
                j--;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        if(c <= 6 && r >= 1) { // lower right
            int i = r - 1;
            int j = c + 1;
            Tile t = getTile(i, j);
            if(t.hasPieceOn() == true) { // if pawn check
                if(t.p.isWhite == true && (t.p instanceof Pawn)) {
                    return true;
                }
            }
            while(i >= 0 && j <= 7 && t.p == null) {
                t = getTile(i, j);
                i--;
                j++;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        if(c >= 1 && r <= 6) { // upper left
            int i = r + 1;
            int j = c - 1;
            Tile t = getTile(i, j);

            while(i <= 7 && j >= 0 && t.p == null) {
                t = getTile(i, j);
                i++;
                j--;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        if(c <= 6 && r<= 6) { // upper right
            int i = r + 1;
            int j = c + 1;
            Tile t = getTile(i, j);

            while(i <= 7 && j <= 7 && t.p == null) {
                t = getTile(i, j);
                i++;
                j++;
            }
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Queen || t.p instanceof Bishop)) {
                    return true;
                }
            }
        }
        // knights
        if(r + 2 <= 7 && c + 1 <= 7) { // up 2 right 1
            Tile t = getTile(r + 2, c + 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r + 1 <= 7 && c + 2 <= 7) { // up 1 right 2
            Tile t = getTile(r + 1, c + 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 1 >= 0 && c + 2 <= 7) { // down 1 right 2
            Tile t = getTile(r - 1, c + 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 2 >= 0 && c + 1 <= 7) { // down 2 right 1
            Tile t = getTile(r - 2, c + 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 2 >= 0 && c - 1 >= 0) { // down 2 left 1
            Tile t = getTile(r - 2, c - 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r - 1 >= 0 && c - 2 >= 0) { // down 1 left 2
            Tile t = getTile(r - 1, c - 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r + 1 <= 7 && c - 2 >= 0) { // up 1 left 2
            Tile t = getTile(r + 1, c - 2);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }
        if(r + 2 <= 7 && c - 1 >= 0) { // up 2 left 1
            Tile t = getTile(r + 2, c - 1);
            if(t.hasPieceOn() == true) {
                if(t.p.isWhite == true && (t.p instanceof Knight)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void updatePawnAttackingTiles(Tile tile1, Tile tile2, boolean isPieceWhite) {

    }

    public static void updateKnightAttackingTiles(Tile tile1, Tile tile2, boolean isPieceWhite) {

    }

    public static void updateBishopAttackingTiles(Tile tile1, Tile tile2, boolean isPieceWhite) {

    }

    public static void updateQueenAttackingTiles(Tile tile1, Tile tile2, boolean isPieceWhite) {

    }

    public static void updateRookAttackingTiles(Tile tile1, Tile tile2, boolean isPieceWhite) {

    }

    public static void updateKingAttackingTiles(Tile tile1, Tile tile2, boolean isPieceWhite) {

    }
}
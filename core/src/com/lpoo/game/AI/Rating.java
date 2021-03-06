package com.lpoo.game.AI;

/**
 * Created by FranciscoSilva on 10/06/17.
 */

/**
 * 01 function alphabeta(node, depth, α, β, maximizingPlayer)
 02      if depth = 0 or node is a terminal node
 03          return the heuristic value of node
 04      if maximizingPlayer
 05          v := -∞
 06          for each child of node
 07              v := max(v, alphabeta(child, depth – 1, α, β, FALSE))
 08              α := max(α, v)
 09              if β ≤ α
 10                  break (* β cut-off *)
 11          return v
 12      else
 13          v := +∞
 14          for each child of node
 15              v := min(v, alphabeta(child, depth – 1, α, β, TRUE))
 16              β := min(β, v)
 17              if β ≤ α
 18                  break (* α cut-off *)
 19          return v

 (* Initial call *)
 alphabeta(origin, depth, -∞, +∞, TRUE)

 */
public class Rating {
    static int pawnBoard[][]={//attribute to http://chessprogramming.wikispaces.com/Simplified+evaluation+function
            { 0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            { 5,  5, 10, 25, 25, 10,  5,  5},
            { 0,  0,  0, 20, 20,  0,  0,  0},
            { 5, -5,-10,  0,  0,-10, -5,  5},
            { 5, 10, 10,-20,-20, 10, 10,  5},
            { 0,  0,  0,  0,  0,  0,  0,  0}};
    static int rookBoard[][]={
            { 0,  0,  0,  0,  0,  0,  0,  0},
            { 5, 10, 10, 10, 10, 10, 10,  5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            {-5,  0,  0,  0,  0,  0,  0, -5},
            { 0,  0,  0,  5,  5,  0,  0,  0}};
    static int knightBoard[][]={
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}};
    static int bishopBoard[][]={
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-10,-10,-10,-10,-10,-20}};
    static int queenBoard[][]={
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}};
    static int kingMidBoard[][]={
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-30,-40,-40,-50,-50,-40,-40,-30},
            {-20,-30,-30,-40,-40,-30,-30,-20},
            {-10,-20,-20,-20,-20,-20,-20,-10},
            { 20, 20,  0,  0,  0,  0, 20, 20},
            { 20, 30, 10,  0,  0, 10, 30, 20}};
    static int kingEndBoard[][]={
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50}};
    public static int rating(int list, int depth) {
        int counter=0, material=rateMaterial();
        counter+=rateAttack();
        counter+=material;
        counter+=rateMoveablitly(list, depth, material);
        counter+=ratePositional(material);
        HintMove.flipBoard();
        material=rateMaterial();
        counter-=rateAttack();
        counter-=material;
        counter-=rateMoveablitly(list, depth, material);
        counter-=ratePositional(material);
        HintMove.flipBoard();
        return -(counter+depth*50);
    }
    public static int rateAttack() {
        int counter=0;
        int tempPositionC=HintMove.kingPositionC;
        for (int i=0;i<64;i++) {
            if (HintMove.chessBoard[i / 8][i % 8].equals("P")) {
                HintMove.kingPositionC = i;
                if (!HintMove.kingSafe()) {
                    counter -= 64;
                }

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("R")) {
                HintMove.kingPositionC = i;
                if (!HintMove.kingSafe()) {
                    counter -= 500;
                }

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("K")) {
                HintMove.kingPositionC = i;
                if (!HintMove.kingSafe()) {
                    counter -= 300;
                }

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("B")) {
                HintMove.kingPositionC = i;
                if (!HintMove.kingSafe()) {
                    counter -= 300;
                }

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("Q")) {
                HintMove.kingPositionC = i;
                if (!HintMove.kingSafe()) {
                    counter -= 900;
                }

            }
        }
        HintMove.kingPositionC=tempPositionC;
        if (!HintMove.kingSafe()) {counter-=200;}
        return counter/2;
    }
    public static int rateMaterial() {
        int counter=0, bishopCounter=0;
        for (int i=0;i<64;i++) {
            if (HintMove.chessBoard[i / 8][i % 8].equals("P")) {
                counter += 100;

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("R")) {
                counter += 500;

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("K")) {
                counter += 300;

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("B")) {
                bishopCounter += 1;

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("Q")) {
                counter += 900;

            }
        }
        if (bishopCounter>=2) {
            counter+=300*bishopCounter;
        } else {
            if (bishopCounter==1) {counter+=250;}
        }
        return counter;
    }
    public static int rateMoveablitly(int listLength, int depth, int material) {
        int counter=0;
        counter+=listLength;//5 pointer per valid move
        if (listLength==0) {//current side is in checkmate or stalemate
            if (!HintMove.kingSafe()) {//if checkmate
                counter+=-200000*depth;
            } else {//if stalemate
                counter+=-150000*depth;
            }
        }
        return 0;
    }
    public static int ratePositional(int material) {
        int counter=0;
        for (int i=0;i<64;i++) {
            if (HintMove.chessBoard[i / 8][i % 8].equals("P")) {
                counter += pawnBoard[i / 8][i % 8];

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("R")) {
                counter += rookBoard[i / 8][i % 8];

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("K")) {
                counter += knightBoard[i / 8][i % 8];

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("B")) {
                counter += bishopBoard[i / 8][i % 8];

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("Q")) {
                counter += queenBoard[i / 8][i % 8];

            } else if (HintMove.chessBoard[i / 8][i % 8].equals("A")) {
                if (material >= 1750) {
                    counter += kingMidBoard[i / 8][i % 8];
                    counter += HintMove.posibleA(HintMove.kingPositionC).length() * 10;
                } else {
                    counter += kingEndBoard[i / 8][i % 8];
                    counter += HintMove.posibleA(HintMove.kingPositionC).length() * 30;
                }

            }
        }
        return counter;
    }
}

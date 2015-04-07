package com.mycompanioncube.chess;

enum PIECES {
	PAWN_W, ROOK_W, KNIGHT_W, BISHOP_W, QUEEN_W, KING_W, PAWN_B, ROOK_B, KNIGHT_B, BISHOP_B, QUEEN_B, KING_B;

	public static int[][] emptyBoard() {
		int[][] board = new int[8][8];

		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				board[x][y] = -1;
			}
		}

		for (int x = 0; x < 8; x++) {
			board[x][1] = PIECES.PAWN_W.ordinal();
			board[x][6] = PIECES.PAWN_B.ordinal();
		}

		board[0][0] = PIECES.ROOK_W.ordinal();
		board[7][0] = PIECES.ROOK_W.ordinal();
		board[0][7] = PIECES.ROOK_B.ordinal();
		board[7][7] = PIECES.ROOK_B.ordinal();

		board[1][0] = PIECES.KNIGHT_W.ordinal();
		board[6][0] = PIECES.KNIGHT_W.ordinal();
		board[1][7] = PIECES.KNIGHT_B.ordinal();
		board[6][7] = PIECES.KNIGHT_B.ordinal();

		board[2][0] = PIECES.BISHOP_W.ordinal();
		board[5][0] = PIECES.BISHOP_W.ordinal();
		board[2][7] = PIECES.BISHOP_B.ordinal();
		board[5][7] = PIECES.BISHOP_B.ordinal();

		board[3][0] = PIECES.QUEEN_W.ordinal();
		board[4][0] = PIECES.KING_W.ordinal();
		board[3][7] = PIECES.QUEEN_B.ordinal();
		board[4][7] = PIECES.KING_B.ordinal();

		return board;
	}
}

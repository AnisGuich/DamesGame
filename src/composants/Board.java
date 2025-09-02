package composants;

import java.util.ArrayList;
import java.util.List;

public class Board {
	/* constantes pour le type des cases */
	public static final int INVALID = -1;
	public static final int EMPTY = 0;
	public static final int WHITE_CHECKER = 1;
	public static final int WHITE_DAME = 2;
	public static final int BLACK_CHECKER = 3;
	public static final int BLACK_DAME = 4;

	// Tableau 2D représentant le plateau (8x8)
	private int[][] board;

	public Board() {
		reset();
	}

	/* copie du board */
	public Board copy() {
		Board copy = new Board();
		// Créer un nouveau tableau 2D
		copy.board = new int[8][8];
		// Copier chaque valeur
		for (int x = 0; x < 8; x++) {
            System.arraycopy(this.board[x], 0, copy.board[x], 0, 8);
		}
		return copy;
	}

	public void reset() {
		this.board = new int[8][8];
		// Initialiser toutes les cases à INVALID (cases blanches)
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				board[x][y] = INVALID;
			}
		}

		// Mettre les cases noires à EMPTY parceque sont les seule a pouvoir aller dessus 
		for (int x = 0; x < 8; x++) {
			for (int y = (x + 1) % 2; y < 8; y += 2) {
				board[x][y] = EMPTY;
			}
		}
		
		// Placer les pions noirs (haut du plateau)
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 3; y++) {
				if ((x + y) % 2 == 1) {
					board[x][y] = BLACK_CHECKER;
				}
			}
		}
		
		// Placer les pions blancs (bas du plateau)
		for (int x = 0; x < 8; x++) {
			for (int y = 5; y < 8; y++) {
				if ((x + y) % 2 == 1) {
					board[x][y] = WHITE_CHECKER;
				}
			}
		}
	}

	/*Retourne la liste des positions des pions d'un type donne  */
	public List<Point> find(int id) {
		List<Point> points = new ArrayList<>();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if ((x + y) % 2 == 1 && board[x][y] == id) {
					points.add(new Point(x, y));
				}
			}
		}
		return points;
	}

	/*Définit l'état d'une case  */
	public void set(Point p, int id) {
		if (isValidPoint(p)) {
			board[p.getX()][p.getY()] = id;
		}
	}
	/*get l'état d'une case  */
	public int get(Point p) {
		if (!isValidPoint(p)) {
			return INVALID;
		}
		return board[p.getX()][p.getY()];
	}

	/* Verifie si un point est valide  */
	public static boolean isValidPoint(Point p) {
		return p != null && isValidPoint_1(p.getX(), p.getY());
	}

	/* Verifie si un point est dans le plateau 8*8  */
	public static boolean isValidPoint_1(int x, int y) {
		return x >= 0 && x < 8 && y >= 0 && y < 8;
	}

	/*Retourne le point milieu de deux position start et end  */
	public static Point middle(Point p1, Point p2) {
		if (p1 == null || p2 == null) {
			return null;
		}
		return new Point((p1.getX() + p2.getX()) / 2, (p1.getY() + p2.getY()) / 2);
	}

	public static boolean isWhiteChecker(int id) {
		return id == WHITE_CHECKER || id == WHITE_DAME;
	}

	public static boolean isBlackChecker(int id) {
		return id == BLACK_CHECKER || id == BLACK_DAME;
	}

	public static boolean isDameChecker(int id) {
		return id == WHITE_DAME || id == BLACK_DAME;
	}

	/*Akram Gay */
}
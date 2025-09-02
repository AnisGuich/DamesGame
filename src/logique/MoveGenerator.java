package logique;

import java.util.ArrayList;
import java.util.List;
import composants.Board;
import composants.Point;

public class MoveGenerator {

	/**
	 *                        Génère tous les mouvements normaux possibles pour une pièce.
	 */

/*il prend comme parametre :
list<point>: c'est la liste des mouvements  normaux possibles
getMoves(board,point start): c le plateau et un point de depart
et la fonction retourne les la liste des mouvement possibles selon 2 condiotions
la &ére si le plateau est null ou le point de depart est invalide
La 2eme le point de depart est null ou invalide
*/

	public static List<Point> getMoves(Board board, Point start) {
		List<Point> possibleMoves = new ArrayList<>();
		if (board == null || !Board.isValidPoint(start)) {
			return possibleMoves;
		}

		int piece = board.get(start);
		if (piece == Board.EMPTY || piece == Board.INVALID) {
			return possibleMoves;
		}
//retourner la liste de tous les mouveemnts possibles de la piece du point de depart

		generatePossibleMoves(possibleMoves, start, piece);
//supprimer la liste si  y a aucun mouvement pnormal possible de ce point

		possibleMoves.removeIf(end -> !isValidNormalMove(board, start, end));

		return possibleMoves;
	}
/**
 *                        Génère toutes les captures possibles pour une pièce.
 */
	/**
	 il prend une liste appelee getSkip il fait les memes verifications que les mouvements normaux
	 */

	public static List<Point> getSkips(Board board, Point start) {
		List<Point> possibleSkips = new ArrayList<>();
		if (board == null || !Board.isValidPoint(start)) {
			return possibleSkips;
		}

		int piece = board.get(start);
		if (piece == Board.EMPTY || piece == Board.INVALID) {
			return possibleSkips;
		}

		generatePossibleSkips(possibleSkips, start, piece);
		possibleSkips.removeIf(end -> !isValidSkip(board, start, end));

		return possibleSkips;
	}

	/**
	 * Vérifie si une capture est valide.
	 */

/*
c'est la fonction logique qui prend en le plateau un poin de depart et un point de d'arrivé
retourne False si :
	1 si le plateau n'existe pas ou n'est pas initialisé
	2 si end est la fin du platau ou ce n'est pas valide
	3 si les sauts de la diagonales son differnets de 2
	4 si le point appele milieu ,n'existe pas ou le saut plus grand
	5 si le point milieu est est null ou invalide
	6 si le point milieu(a capturer)ist vide ou invalide
	7 si le mouvement est sur la bonne direction

*/

	public static boolean isValidSkip(Board board, Point start, Point end) {
		if (board == null || !Board.isValidPoint(start) || !Board.isValidPoint(end)) {
			return false;
		}

		if (board.get(end) != Board.EMPTY) {
			return false;
		}

		int dx = Math.abs(end.getX() - start.getX());
		int dy = Math.abs(end.getY() - start.getY());
		if (dx != 2 || dy != 2) {
			return false;
		}

		Point middle = Board.middle(start, end);
		if (middle == null) {
			return false;
		}

		int startPiece = board.get(start);
		int capturedPiece = board.get(middle);

		if (capturedPiece == Board.EMPTY || capturedPiece == Board.INVALID) {
			return false;
		}

		boolean isAttackerBlack = Board.isBlackChecker(startPiece);
		boolean isVictimBlack = Board.isBlackChecker(capturedPiece);
		if (isAttackerBlack == isVictimBlack) {
			return false;
		}

		if (!Board.isDameChecker(startPiece)) {
			int dy2 = end.getY() - start.getY();
			boolean isMovingForward = (startPiece == Board.BLACK_CHECKER) ? (dy2 > 0) : (dy2 < 0);
			if (!isMovingForward) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Vérifie si un mouvement normal est valide.
	 */
	private static boolean isValidNormalMove(Board board, Point start, Point end) {
		if (!Board.isValidPoint(end) || board.get(end) != Board.EMPTY) {
			return false;
		}

		int dx = Math.abs(end.getX() - start.getX());
		int dy = Math.abs(end.getY() - start.getY());
		if (dx != 1 || dy != 1) {
			return false;
		}

		int piece = board.get(start);
		if (!Board.isDameChecker(piece)) {
			int actualDy = end.getY() - start.getY();
			if (piece == Board.BLACK_CHECKER && actualDy < 0) {
				return false;
			}
			if (piece == Board.WHITE_CHECKER && actualDy > 0) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Génère les positions possibles pour les mouvements normaux.
	 */
	private static void generatePossibleMoves(List<Point> moves, Point start, int piece) {
		int x = start.getX();
		int y = start.getY();

		if (Board.isDameChecker(piece)) {
			moves.add(new Point(x + 1, y + 1));
			moves.add(new Point(x - 1, y + 1));
			moves.add(new Point(x + 1, y - 1));
			moves.add(new Point(x - 1, y - 1));
			return;
		}

		if (piece == Board.BLACK_CHECKER) {
			moves.add(new Point(x + 1, y + 1));
			moves.add(new Point(x - 1, y + 1));
		}

		if (piece == Board.WHITE_CHECKER) {
			moves.add(new Point(x + 1, y - 1));
			moves.add(new Point(x - 1, y - 1));
		}
	}

	/**
	 * Génère les positions possibles pour les captures.
	 */
	private static void generatePossibleSkips(List<Point> skips, Point start, int piece) {
		int x = start.getX();
		int y = start.getY();

		if (Board.isDameChecker(piece)) {
			skips.add(new Point(x + 2, y + 2));
			skips.add(new Point(x - 2, y + 2));
			skips.add(new Point(x + 2, y - 2));
			skips.add(new Point(x - 2, y - 2));
			return;
		}

		if (piece == Board.BLACK_CHECKER) {
			skips.add(new Point(x + 2, y + 2));
			skips.add(new Point(x - 2, y + 2));
		}

		if (piece == Board.WHITE_CHECKER) {
			skips.add(new Point(x + 2, y - 2));
			skips.add(new Point(x - 2, y - 2));
		}
	}
}
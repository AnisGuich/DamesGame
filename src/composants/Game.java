package composants;

import java.util.List;

import logique.MoveGenerator;
import logique.MoveLogic;

/* Classe principale gérant une partie de dames */
public class Game {

	// État du jeu
	private Board board;          // Plateau de jeu
	private boolean isP1Turn;     // Tour du joueur 1 (noir)
	private Point skipPoint;      // Point de capture multiple
	private boolean gameOver;     // Partie terminée

	/* Cree une nouvelle partie */
	public Game() {
		restart();
	}

	/* Cree une partie */
	public Game(Board board, boolean isP1Turn, Point skipPoint) {
		this.board = (board == null)? new Board() : board;
		this.isP1Turn = isP1Turn;
		this.skipPoint = skipPoint;
		this.gameOver = false;
	}
	/* Retourne une copie du plateau utile pour la sauvegarde*/
	public Board getBoard() {
		return board.copy();
	}

	/* Indique si cest le tour du joueur 1 */
	public boolean isP1Turn() {
		return isP1Turn;
	}

	/* Retourne le point de capture  */
	public Point getSkipPoint() {
		return skipPoint;
	}

	/* Indique si la partie est terminee */
	public boolean isGameFinished() {
		return gameOver;
	}


	/* Cree une copie */
	public Game copy() {
		return new Game(board.copy(), isP1Turn, skipPoint);
	}

	/* Reinitialise la partie */
	public void restart() {
		this.board = new Board();
		this.isP1Turn = true;
		this.skipPoint = null;
		this.gameOver = false;
	}

	/* Deplace un pion d'une position a une autre en utilisant 
	les methode verification des autre mouvements */
	public boolean move(Point start, Point end) {
		if (gameOver || start == null || end == null) {
			return false;
		}

		if (!MoveLogic.isValidMove(this, start, end)) {
			return false;
		}

		int piece = board.get(start);
		boolean isCapture = Math.abs(end.getX() - start.getX()) == 2;
		
		board.set(end, piece);
		board.set(start, Board.EMPTY);

		// Gestion la capture
		if (isCapture) {
			Point middle = Board.middle(start, end);
			if (middle != null && board.get(middle) != Board.EMPTY) {
				board.set(middle, Board.EMPTY);
			}
		}

		boolean wasPromoted = handlePromotion(end, piece);

		// Gestion des captures multiples
		if (isCapture && !wasPromoted) {
			List<Point> additionalSkips = MoveGenerator.getSkips(board, end);
			if (!additionalSkips.isEmpty()) {
				skipPoint = end;
				return true;
			}
		}

		isP1Turn = !isP1Turn;
		skipPoint = null;
		gameOver = isGameOver();

		return true;
	}

	/* Gere la promotion d'un pion en dame */
	private boolean handlePromotion(Point position, int piece) {
		if (position.getY() == 0 && piece == Board.WHITE_CHECKER) {
			board.set(position, Board.WHITE_DAME);
			return true;
		} 
		if (position.getY() == 7 && piece == Board.BLACK_CHECKER) {
			board.set(position, Board.BLACK_DAME);
			return true;
		}
		return false;
	}

	/* Verifie si la partie est terminee( 3 cas , plus de pions noirs ou blancs ou plus 
	de mouvement possible pour le joueur actif)*/
	public boolean isGameOver() {
		// Verifie sil reste des pions noirs
		List<Point> black = board.find(Board.BLACK_CHECKER);
		black.addAll(board.find(Board.BLACK_DAME));
		if (black.isEmpty()) {
			return true;
		}

		// Vérifie s'il reste des pions blancs
		List<Point> white = board.find(Board.WHITE_CHECKER);
		white.addAll(board.find(Board.WHITE_DAME));
		if (white.isEmpty()) {
			return true;
		}

		// Vérifie si le joueur actuel peut bouger
		List<Point> currentPieces = isP1Turn ? black : white;
		for (Point p : currentPieces) {
			if (!MoveGenerator.getMoves(board, p).isEmpty() || !MoveGenerator.getSkips(board, p).isEmpty()) {
				return false;
			}
		}
		return true;
	}
}
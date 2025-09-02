package logique;

import java.util.ArrayList;
import java.util.List;

import composants.Board;
import composants.Game;
import composants.Point;

public class MoveLogic {

	// Si pour une partie un mouvement est valide
	// il prend la partie en cours un point de depart et un point d'arrivé
	public static boolean isValidMove(Game game, Point start, Point end) {
		if (game == null) return false;
		//retourner le plateau apr le mouvment ,le tour du joueur1,point de depart ,point darrivee,si le joueur doit capturer
		return isValidMove(game.getBoard(), game.isP1Turn(), start, end, game.getSkipPoint());
	}

	//si un mouvement est valide
	public static boolean isValidMove(Board board, boolean isP1Turn, Point start, Point end, Point skipPoint) {
		//valider un mouvement basique
		//verifie:
		//si le plateau est valide,le point de depart et d'arrivé
		//si le point de départ est different a a ceului d'arrivé
		//si y a pas de pion ou la case est non jouable
		//sinon la valeur logique de la case darrivee est vide
		if (board == null || !Board.isValidPoint(start) || !Board.isValidPoint(end)) {
			return false;
		}
		if (start.equals(end)) {
			return false;
		}

		int startId = board.get(start);

		if (startId == Board.EMPTY || startId == Board.INVALID) {
			return false;
		}

		//valider le tour du joueur et si la piece lui appartient
		boolean isBlackPiece = Board.isBlackChecker(startId);
		boolean isWhitePiece = Board.isWhiteChecker(startId);

		if (isP1Turn && !isBlackPiece) {
			return false;
		}
		if (!isP1Turn && !isWhitePiece) {
			return false;
		}

		if (board.get(end) != Board.EMPTY) {
			return false;
		}

		if (skipPoint != null && !skipPoint.equals(start)) {
			return false;
		}

		int dx = end.getX() - start.getX();
		int dy = end.getY() - start.getY();
		int absDx = Math.abs(dx);
		int absDy = Math.abs(dy);

		//verifier si la direction des joueurs est correcte
		if (absDx != absDy) {
			return false;
		}

		if (!Board.isDameChecker(startId)) {
			if (startId == Board.BLACK_CHECKER && dy < 0) {
				return false;
			}
			if (startId == Board.WHITE_CHECKER && dy > 0) {
				return false;
			}
		}

		//verifier si y a au moins une capture possible
		//reccupere toutes les les piec dun joueur y compris les dames
		List<Point> allPieces = new ArrayList<>();
		if (isP1Turn) {
			allPieces.addAll(board.find(Board.BLACK_CHECKER));
			allPieces.addAll(board.find(Board.BLACK_DAME));
		} else {
			allPieces.addAll(board.find(Board.WHITE_CHECKER));
			allPieces.addAll(board.find(Board.WHITE_DAME));
		}

		boolean hasCapture = false;
		for (Point piece : allPieces) {
			if (!MoveGenerator.getSkips(board, piece).isEmpty()) {
				hasCapture = true;
				break;
			}
		}

		if (hasCapture) {
			// si la distance=2
			if (absDx != 2) {
				return false;
			}

			// is y a une case entre debut et fin
			Point middle = Board.middle(start, end);
			if (middle == null) {
				return false;
			}

			//recuperer la case du milieu et voir si yen a une piece
			int middlePiece = board.get(middle);

			if (middlePiece == Board.EMPTY) {
				return false;
			}

			//si le joueur ne capture pas sa piece
			boolean isCapturingOwnPiece = (isP1Turn && Board.isBlackChecker(middlePiece)) ||
					(!isP1Turn && Board.isWhiteChecker(middlePiece));
			if (isCapturingOwnPiece) {
				return false;
			}
		} else {
			if (absDx != 1) {
				return false;
			}
		}

		return true;
	}

	public static boolean isSafe(Board board, Point checker) {
		// Si le plateau est vide la piéce n'existe pas ou est vide
		if (board == null || checker == null) return true;
		if (board.get(checker) == Board.EMPTY) return true;

		// Récupère la position de la pièce sur la case
		int id = board.get(checker);
		// Détermine si la piece est noire ou blanche
		boolean isBlack = Board.isBlackChecker(id);

		List<Point> check = new ArrayList<>();
		int x = checker.getX();
		int y = checker.getY();

		// Les 4 cases diagonales autour de la pièce
		check.add(new Point(x + 1, y + 1));
		check.add(new Point(x - 1, y + 1));
		check.add(new Point(x + 1, y - 1));
		check.add(new Point(x - 1, y - 1));

		// Vérifie chaque direction diagonale
		for (Point p : check) {
			if (!Board.isValidPoint(p)) continue;

			int pid = board.get(p);
			// S'il y a rien ou que c'est invalide
			if (pid == Board.EMPTY || pid == Board.INVALID) continue;

			// Vérifie si la pièce adjacente est de l'adverse
			boolean isBlackChecker = Board.isBlackChecker(pid);
			if (isBlack == isBlackChecker) continue;

			// Calcule la case entre la piece de ladverse et la piece du joueur
			Point skip = Board.middle(p, checker);
			if (skip == null) continue;

			// Pour qu'il y ait menace la case de capture doit etre vide
			if (Board.isValidPoint(skip) && board.get(skip) == Board.EMPTY) {
				return false;
			}
		}

		return true;
	}
}
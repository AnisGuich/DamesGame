package composants;

import java.util.ArrayList;
import java.util.List;
import logique.MoveGenerator;

public class ComputerPlayer extends Player {

	/* ----- POINTS SIMPLIFIÉS ----- */
	private static final int POINTS_CAPTURE = 3;     // Points pour une capture
	private static final int POINTS_DAME = 2;        // Points pour devenir une dame
	private static final int POINTS_AVANCER = 1;     // Points pour avancer
	/* ------------ */

	// Identifie l'IA
	@Override
	public boolean isHuman() { return false; }

	@Override
	public void updateGame(Game game) {
		if (game == null || game.isGameOver() || game.isGameFinished()) { return; }

		Move meilleurCoup = findBestMove(game);
		if (meilleurCoup != null) {
			game.move(meilleurCoup.getStart(), meilleurCoup.getEnd());
		}
	}

	/*Utilise une copie du jeu pour simuler les coups
	 sans affecter l'etat réel.
	Compare les scores pour garder le meilleur mouvement. */
	private Move findBestMove(Game game) {
		List<Move> moves = getMoves(game);// tout les moves possibles 
		if (moves.isEmpty()) { // s'il ya aucune 
			return null;
		}

		Move meilleurCoup = null;
		int meilleurScore = Integer.MIN_VALUE; 

		for (Move move : moves) { //parcourir les moves possibles pour trouver le meilleur 
			Game copieJeu = game.copy();
			if (copieJeu.move(move.getStart(), move.getEnd())) {
				int score = evaluerPosition(copieJeu, move);
				if (score > meilleurScore) {
					meilleurScore = score;
					meilleurCoup = move;
				}
			}
		}
		return meilleurCoup;
	}

	/*Evaluer la position du jeu en fonction des moves possible */ 
	private int evaluerPosition(Game game, Move move) {
		int score = 0;
		Board board = game.getBoard();
		
		// Points pour capture (si le move est une capture on augmente le score de POINTS_CAPTURE)
		if (Math.abs(move.getEnd().getX() - move.getStart().getX()) == 2) {
			score += POINTS_CAPTURE;
		}
		
		// Points pour promotion en dame
		if (peutDevenirDame(board, move.getEnd())) {
			score += POINTS_DAME;
		}
		
		// Points pour avancer 
		if (avancerVersPromotion(move, game.isP1Turn())) {
			score += POINTS_AVANCER;
		}
		
		return score;
	}

	/* Verifier si une piece peut devenir une dame s'il elle arrive vers la fin de la ligne des blancs ou des noirs  */
	private boolean peutDevenirDame(Board board, Point position) {
		int piece = board.get(position);
		return (piece == Board.BLACK_CHECKER && position.getY() == 7) ||
			   (piece == Board.WHITE_CHECKER && position.getY() == 0);
	}

	/* Verifier si une piece avance vers la promotion */
	private boolean avancerVersPromotion(Move move, boolean isP1Turn) {
		int dy = move.getEnd().getY() - move.getStart().getY();
		/* si la piece est noir et quelle avance vers le haut (dy > 0) 
		ou si la piece est blanche et qu'elle avance vers le bas (dy < 0) */
		return (isP1Turn && dy > 0) || (!isP1Turn && dy < 0);
	}

	/* get moves  */
	private List<Move> getMoves(Game game) {
		List<Move> moves = new ArrayList<>();
		if (game == null || game.isGameOver()) {
			return moves;
		}
		/* get le board et le joueur actif */
		Board board = game.getBoard();
		boolean isP1Turn = game.isP1Turn();
		/* get le point de capture */
		Point skipPoint = game.getSkipPoint();

		// Gérer les captures obligatoires
		if (skipPoint != null) {
			List<Point> skips = MoveGenerator.getSkips(board, skipPoint);
			for (Point end : skips) {
				moves.add(new Move(skipPoint, end));
			}
			return moves;
		}

		// Récupérer toutes les pièces du joueur
		List<Point> pieces = getPieces(board, isP1Turn);

		// priorité a les captures Verifier d'abord les captures possibles
		boolean hasCapture = false;
		for (Point piece : pieces) {
			List<Point> captures = MoveGenerator.getSkips(board, piece);
			if (!captures.isEmpty()) {
				hasCapture = true;
				for (Point end : captures) {
					moves.add(new Move(piece, end));
				}
			}
		}

		// Si pas de capture, chercher les mouvements normaux
		if (!hasCapture) {
			for (Point piece : pieces) {
				List<Point> regularMoves = MoveGenerator.getMoves(board, piece);
				for (Point end : regularMoves) {
					moves.add(new Move(piece, end));
				}
			}
		}
		return moves;
	}

	/* get les pieces du joueur actif */
	private List<Point> getPieces(Board board, boolean isP1Turn) {
		List<Point> pieces = new ArrayList<>();
		if (isP1Turn) {
			pieces.addAll(board.find(Board.BLACK_CHECKER));
			pieces.addAll(board.find(Board.BLACK_DAME));
		} else {
			pieces.addAll(board.find(Board.WHITE_CHECKER));
			pieces.addAll(board.find(Board.WHITE_DAME));
		}
		return pieces;
	}
}
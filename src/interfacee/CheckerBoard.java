package interfacee;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.animation.PauseTransition;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import logique.MoveGenerator;
import composants.Board;
import composants.Game;
import composants.HumanPlayer;
import composants.Player;
import composants.Point;
import java.util.List;
import java.util.ArrayList;

// Classe représentant le plateau de jeu graphique (COTE HAUT)
//StackPane de JavaFX
public class CheckerBoard extends StackPane {

	// Constantes pour le padding et le délai du timer
	private static final int PADDING = 16;
	private static final int tempsAttenteIA = 1000;

	// Références aux objets de jeu
	private Game game; //état du jeu
	private CheckersWindow window; //la fenêtre principale
	private Player player1;
	private Player player2;

	// Gestion de la sélection
	private Point pointSelected; //case sélectionnée
	private boolean selectionValid;
	private Color light;
	private Color dark;
	private boolean isGameOver;
	private int scoreBlack;
	private int scoreWhite;
	private boolean alertShown = false;

	private Canvas canvas;
	// Canvas pour le dessin du plateau

	//Constructeur avec la fenêtre principale.
	public CheckerBoard(CheckersWindow window) {
		this(window, new Game(), null, null);
	}

	//Constructeur complet.
	public CheckerBoard(CheckersWindow window, Game game, Player player1, Player player2) {
		this.window = window;
		this.game = (game == null) ? new Game() : game;
		this.light = Color.BEIGE;
		this.dark = Color.SADDLEBROWN;

		// Initialisation du canvas
		this.canvas = new Canvas();
		this.getChildren().add(canvas);

		// Configuration des joueurs
		setPlayer1(player1);
		setPlayer2(player2);

		// Liaison des propriétés de taille
		canvas.widthProperty().bind(this.widthProperty());
		canvas.heightProperty().bind(this.heightProperty());

		// Redessiner quand la taille change
		canvas.widthProperty().addListener(obs -> draw());
		canvas.heightProperty().addListener(obs -> draw());

		// Gestion du clic souris
		canvas.setOnMouseClicked(this::handleClick);
	}

	//Met à jour l'état du jeu et l'affichage.
	public void update() {
		runPlayer();             // Fait jouer le joueur courant (si IA)
		this.isGameOver = game.isGameOver();
		updateScores();         // Met à jour les scores
		draw();                 // Redessine le plateau
	}

	//Met à jour les scores des joueurs
	private void updateScores() {
		Board board = game.getBoard();
		// Calcul des scores
		scoreBlack = board.find(Board.BLACK_CHECKER).size() + board.find(Board.BLACK_DAME).size();
		scoreWhite = board.find(Board.WHITE_CHECKER).size() + board.find(Board.WHITE_DAME).size();

		// Mise à jour de l'interface si la fenêtre existe
		if (window != null) {
			window.getOptionsPanel().updateScores(
					player1.toString(),
					scoreBlack,
					player2.toString(),
					scoreWhite
			);
		}
	}


	//Dessine le plateau de jeu et les pièces.
	private void draw() {
		//Nettoie le canvas
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

		Game game = this.game.copy();
		Board board = game.getBoard();

		// Calcul des dimensions de composants a dessiner
		final int BOX_PADDING = 4;
		final double W = canvas.getWidth(), H = canvas.getHeight();
		final double DIM = Math.min(W, H);
		final double BOX_SIZE = (DIM - 2 * PADDING) / 8;
		final double OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final double OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		final double CHECKER_SIZE = Math.max(0, BOX_SIZE - 2 * BOX_PADDING);

		// Dessin de la grille (LES CARRES)
		gc.setStroke(Color.DARKGRAY);
		gc.setLineWidth(2);
		gc.strokeRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);

		// Remplissage des cases
		gc.setFill(light);
		gc.fillRect(OFFSET_X, OFFSET_Y, BOX_SIZE * 8, BOX_SIZE * 8);
		gc.setFill(dark);

		// Cases foncées en damier
		for (int y = 0; y < 8; y++) {
			for (int x = (y + 1) % 2; x < 8; x += 2) {
				gc.fillRect(OFFSET_X + x * BOX_SIZE, OFFSET_Y + y * BOX_SIZE, BOX_SIZE, BOX_SIZE);
			}
		}


		// Mise en évidence de la case sélectionnée
		// (uniquement en vert si valide)
		if (pointSelected != null && selectionValid) {
			gc.setFill(Color.GREEN);
			gc.fillRect(
					OFFSET_X + pointSelected.getX() * BOX_SIZE,
					OFFSET_Y + pointSelected.getY() * BOX_SIZE,
					BOX_SIZE, BOX_SIZE
			);
		}

		// Dessin des pièces
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				Point p = new Point(x, y);
				int piece = board.get(p);
				if (piece != Board.EMPTY && piece != Board.INVALID) {
					boolean isBlack = Board.isBlackChecker(piece);
					boolean isDame = Board.isDameChecker(piece);

					// Couleur de la pièce et contour
					gc.setFill(isBlack ? Color.BLACK : Color.WHITE);
					gc.setStroke(isBlack ? Color.WHITE : Color.BLACK);

					double centerX = OFFSET_X + x * BOX_SIZE + BOX_SIZE / 2;
					double centerY = OFFSET_Y + y * BOX_SIZE + BOX_SIZE / 2;

					// Dessin du pion
					gc.fillOval(centerX - CHECKER_SIZE / 2, centerY - CHECKER_SIZE / 2,
							CHECKER_SIZE, CHECKER_SIZE);
					gc.strokeOval(centerX - CHECKER_SIZE / 2, centerY - CHECKER_SIZE / 2,
							CHECKER_SIZE, CHECKER_SIZE);

					// Marquage des dames (DIFFERENTES DES PIONS DE DEBUT)
					if (isDame) {
						// Double bordure
						gc.setLineWidth(3);
						gc.setStroke(isBlack ? Color.GOLD : Color.GOLD);
						gc.strokeOval(centerX - CHECKER_SIZE/2, centerY - CHECKER_SIZE/2, CHECKER_SIZE, CHECKER_SIZE);

						// Petite bordure intérieure
						double innerSize = CHECKER_SIZE * 0.7;
						gc.strokeOval(centerX - innerSize/2, centerY - innerSize/2, innerSize, innerSize);
						gc.setLineWidth(1); // Réinitialise l'épaisseur
					}
				}
			}
		}

		// Gestion de la fin de partie
		if (isGameOver && !alertShown) {
			alertShown = true;
			// Pour éviter les répétitions

			// Calcul des pièces restantes
			List<Point> black = board.find(Board.BLACK_CHECKER);
			black.addAll(board.find(Board.BLACK_DAME));
			List<Point> white = board.find(Board.WHITE_CHECKER);
			white.addAll(board.find(Board.WHITE_DAME));

			// Détermination du gagnant
			String winner;
			if (black.isEmpty()) {
				winner = "Les Pions Blancs";
			} else if (white.isEmpty()) {
				winner = "Les Pions Noirs";
			} else {
				winner = game.isP1Turn() ? "Les Blancs" : "Les Noirs";
			}

			// Affichage de l'alerte
			Platform.runLater(() -> {
				Alert alert = new Alert(Alert.AlertType.INFORMATION);
				alert.setTitle("Fin de la partie");
				alert.setHeaderText("Partie terminée !");
				alert.setContentText("Félicitations ! " + winner + " ont gagné la partie !");
				alert.show();
			});
		}
	}

	// Gère les clics de souris sur le plateau.
	private void handleClick(MouseEvent event) {
		if (isGameOver || !getCurrentPlayer().isHuman()) {
			return;  // Ignorer si partie finie ou joueur IA
		}

		Point clickedPoint = getClickedPoint(event);
		if (!Board.isValidPoint(clickedPoint)) return;

		if (pointSelected == null) {
			handleFirstClick(clickedPoint);  // Première sélection
		} else {
			handleSecondClick(clickedPoint); // Tentative de mouvement
		}
		update();
	}

	//Convertit les coordonnées souris en coordonnées plateau.
	private Point getClickedPoint(MouseEvent event) {
		final double W = canvas.getWidth(), H = canvas.getHeight();
		final double DIM = Math.min(W, H);
		final double BOX_SIZE = (DIM - 2 * PADDING) / 8;
		final double OFFSET_X = (W - BOX_SIZE * 8) / 2;
		final double OFFSET_Y = (H - BOX_SIZE * 8) / 2;
		int x = (int) ((event.getX() - OFFSET_X) / BOX_SIZE);
		int y = (int) ((event.getY() - OFFSET_Y) / BOX_SIZE);
		return new Point(x, y);
	}


	// Gère le premier clic (sélection d'une pièce).
	private void handleFirstClick(Point clickedPoint) {
		if (isValidSelection(game.getBoard(), game.isP1Turn(), clickedPoint)) {
			pointSelected = clickedPoint;
			selectionValid = true;
		}
	}

	// Gère le second clic (tentative de mouvement).
	private void handleSecondClick(Point clickedPoint) {
		boolean moveSuccess = game.move(pointSelected, clickedPoint);
		if (moveSuccess) {
			pointSelected = null;        // Réussite: effacer la sélection
			selectionValid = false;
		} else {
			// Échec: vérifier si nouvelle sélection valide
			selectionValid = isValidSelection(game.getBoard(), game.isP1Turn(), clickedPoint);
			pointSelected = selectionValid ? clickedPoint : null;
		}
	}


	//Fait jouer le joueur courant (si c'est une IA).
	//avec un délai pour permettre la visualisation.
	private void runPlayer() {
		Player player = getCurrentPlayer();
		if (player == null || player.isHuman()) {
			return;  // Ne rien faire pour les humains
		}

		// Délai avant que l'IA ne joue (pour visualisation)
		PauseTransition delay = new PauseTransition(Duration.millis(tempsAttenteIA));
		delay.setOnFinished(e -> {
			getCurrentPlayer().updateGame(game);
			this.isGameOver = game.isGameOver();
			update();
			// Mise à jour après le coup de l'IA
		});
		delay.play();
	}


	// Vérifie si une sélection est valide.
	private boolean isValidSelection(Board board, boolean isP1Turn, Point point) {
		// Vérifie Si la case contient une pièce valide
		int id = board.get(point);
		if (id == Board.EMPTY || id == Board.INVALID) {
			return false;
		}

		// Si la pièce appartient au joueur courant
		boolean isBlackPiece = Board.isBlackChecker(id);
		if ((isP1Turn && !isBlackPiece) || (!isP1Turn && isBlackPiece)) {
			return false;
		}

		// Vérifie les captures obligatoires
		List<Point> allPieces = getAllPlayerPieces(board, isP1Turn);
		boolean hasMandatoryCaptures = hasMandatoryCaptures(board, allPieces);

		// Si captures obligatoires, la pièce doit pouvoir capturer
		if (hasMandatoryCaptures) {
			return !MoveGenerator.getSkips(board, point).isEmpty();
		}

		// Sinon, vérifie qu'il y a au moins un mouvement possible
		return !MoveGenerator.getMoves(board, point).isEmpty();
	}

	// Récupère toutes les pièces d'un joueur.
	//pour les utliser dans la verification des captures obligatoires
	private List<Point> getAllPlayerPieces(Board board, boolean isP1Turn) {
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

	// Vérifie si un joueur a des captures obligatoires.
	private boolean hasMandatoryCaptures(Board board, List<Point> pieces) {
		return pieces.stream().anyMatch(p -> !MoveGenerator.getSkips(board, p).isEmpty());
	}

	// Méthodes d'accès et de modification
	public Game getGame() {
		return game;
	}


	//essentiel pour Configurer le type de partie
	//(humain vs humain, humain vs IA, IA vs IA)
	public void setPlayer1(Player player1) {
		this.player1 = (player1 == null) ? new HumanPlayer() : player1;
		if (game.isP1Turn() && !this.player1.isHuman()) {
			this.pointSelected = null;
		}
	}
	public void setPlayer2(Player player2) {
		this.player2 = (player2 == null) ? new HumanPlayer() : player2;
		if (!game.isP1Turn() && !this.player2.isHuman()) {
			this.pointSelected = null;
		}
	}

	/*Récupère le joueur courant.*/
	public Player getCurrentPlayer() {
		return game.isP1Turn() ? player1 : player2;
	}

}
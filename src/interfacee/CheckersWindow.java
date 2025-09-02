package interfacee;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import composants.Player;

public class CheckersWindow extends Stage {
	//Définissent les dimensions par défaut et le titre de la fenêtre
	public static final int DEFAULT_WIDTH = 500;
	public static final int DEFAULT_HEIGHT = 600;
	public static final String DEFAULT_TITLE = "Jeu De Dames";

	private CheckerBoard board;
	private OptionPanel opts = new OptionPanel(this);

	//Permet à d'autres composants d'interagir avec le panneau d'options
	public OptionPanel getOptionsPanel() {
		return opts;
	}

	//constructeur par defaut
	public CheckersWindow() {
		this(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_TITLE);
	}
	// Constructeur avec joueurs spécifiques
	public CheckersWindow(Player player1, Player player2) {
		this();
		setPlayer1(player1);
		setPlayer2(player2);
	}

	// Constructeur principal avec configuration complète
	public CheckersWindow(int width, int height, String title) {
		super();
		this.setTitle(title);

		BorderPane root = new BorderPane();
		this.board = new CheckerBoard(this);
		this.opts = new OptionPanel(this);

		root.setCenter(board); // Plateau au centre
		root.setBottom(opts); // Options en bas

		Scene scene = new Scene(root, width, height);
		this.setScene(scene);
	}


	//assigner le premier joueur (noirs) au plateau de jeu et de
	// déclencher une mise à jour de l'affichage. Si le joueur est une
	// IA et que c'est son tour, la méthode désélectionne
	// aussi toute pièce sélectionnée pour permettre le jeu automatique
	public void setPlayer1(Player player1) {
		this.board.setPlayer1(player1);
		this.board.update();
	}

	public void setPlayer2(Player player2) {
		this.board.setPlayer2(player2);
		this.board.update();
	}
	//Réinitialise l'état du jeu
	//Rafraîchit l'affichage
	public void restart() {
		this.board.getGame().restart();
		this.board.update();
	}

}
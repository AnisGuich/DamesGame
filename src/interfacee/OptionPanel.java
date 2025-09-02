package interfacee;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import composants.ComputerPlayer;
import composants.HumanPlayer;
import composants.Player;

public class OptionPanel extends GridPane {

	private static final String NOIR = "#2E2E2E";
	private static final String BLANC = "#F5F5F5";
	private static final String MARRON = "#8B6B4A";

	private CheckersWindow window;
	private Button redemarrerBtn;
	private ComboBox<String> player1Opts; //Combo-box pour choisir le type de joueur
	private ComboBox<String> player2Opts;
	private Label scoreDesNoirs;
	private Label scoreDesBlancs;

	public OptionPanel(CheckersWindow window) {
		this.window = window;
		this.setStyle("-fx-background-color: " + BLANC + "; -fx-padding: 15;");

		// Initialisation des composants
		final String[] playerTypeOpts = {"Humain", "Ordinateur"};

		this.redemarrerBtn = createStyledButton("REDÉMARRER", MARRON);
		this.player1Opts = createStyledComboBox();
		this.player2Opts = createStyledComboBox();
		this.scoreDesNoirs = createScoreLabel("Noir: 0");
		this.scoreDesBlancs = createScoreLabel("Blanc: 0");

		player1Opts.getItems().addAll(playerTypeOpts);
		player2Opts.getItems().addAll(playerTypeOpts);
		player1Opts.getSelectionModel().selectFirst();
		player2Opts.getSelectionModel().selectFirst();

		// Gestion des événements
		redemarrerBtn.setOnAction(e -> window.restart());
		player1Opts.setOnAction(e -> window.setPlayer1(getPlayer(player1Opts)));
		player2Opts.setOnAction(e -> window.setPlayer2(getPlayer(player2Opts)));

		// Organisation de l'interface
		HBox player1Box = new HBox(10,
				new Label("Joueur 1:"), player1Opts, scoreDesNoirs);
		player1Box.setAlignment(Pos.CENTER_LEFT);

		HBox player2Box = new HBox(10,
				new Label("Joueur 2:"), player2Opts, scoreDesBlancs);
		player2Box.setAlignment(Pos.CENTER_LEFT);

		VBox controlBox = new VBox(15, redemarrerBtn, player1Box, player2Box);
		controlBox.setAlignment(Pos.CENTER);
		controlBox.setPadding(new Insets(10));
		this.add(controlBox, 0, 0);
	}


	//Bouton stylisé
	private Button createStyledButton(String text, String color) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: " + color + "; " +
				"-fx-text-fill: " + BLANC + "; " +
				"-fx-font-family: 'Georgia'; " +
				"-fx-font-size: 14px; " +
				"-fx-padding: 8px 20px; ");


		return button;
	}


	//ComboBox stylisée
	private ComboBox<String> createStyledComboBox() {
		ComboBox<String> comboBox = new ComboBox<>();
		comboBox.setStyle("-fx-background-color: " + BLANC + "; " +
				"-fx-border-color: " + NOIR + "; " +
				"-fx-font-family: 'Georgia'; " +
				"-fx-min-width: 120px;");
		return comboBox;
	}

	//label score
	private Label createScoreLabel(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-font-family: 'Georgia'; " +
				"-fx-font-size: 14px; " +
				"-fx-text-fill: " + NOIR + "; " +
				"-fx-min-width: 80px;");
		return label;
	}


	//Gestion des Joueurs
	private Player getPlayer(ComboBox<String> playerOpts) {
		return playerOpts.getValue().equals("Ordinateur")
				? new ComputerPlayer()
				: new HumanPlayer();
	}

	// Modifiez la méthode updateScores pour afficher les noms :
	public void updateScores(String blackName, int blackScore, String whiteName, int whiteScore) {
		scoreDesNoirs.setText(blackName + ": " + blackScore);
		scoreDesBlancs.setText(whiteName + ": " + whiteScore);
	}
}
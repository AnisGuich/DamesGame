package interfacee;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PlayerNameDialog {
    private static final String DARK_COLOR = "#2E2E2E";
    private static final String LIGHT_COLOR = "#F5F5F5";
    private static final String BROWN_COLOR = "#8B6B4A";
    private static final String ACCENT_COLOR = "#A78A7F";

    private String player1Name;
    private String player2Name;

    public boolean show(Stage parentStage) {
        //Crée une fenêtre modale qui bloque l'interaction avec la fenêtre parente
        Stage dialog = new Stage(StageStyle.UTILITY);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(parentStage);
        dialog.setTitle("Préparez-vous pour le duel !");
        //Utilise un GridPane pour organiser les éléments de façon structurée
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setPadding(new Insets(25));
        grid.setStyle("-fx-background-color: " + LIGHT_COLOR + ";");

        // Titre excitant
        Label title = new Label("ENTREZ VOS NOMS");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        title.setTextFill(Color.web(BROWN_COLOR));
        title.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);");
        grid.add(title, 0, 0, 2, 1);

        // Joueur 1 (Noir)
        Label lblPlayer1 = new Label("Maître des Pions Noirs:");
        lblPlayer1.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        lblPlayer1.setTextFill(Color.web(DARK_COLOR));

        TextField txtPlayer1 = new TextField();
        txtPlayer1.setPromptText("Votre nom légendaire...");
        txtPlayer1.setStyle("-fx-font-family: 'Georgia'; -fx-background-color: " + LIGHT_COLOR + ";");
        txtPlayer1.setMaxWidth(200);

        // Joueur 2 (Blanc)
        Label lblPlayer2 = new Label("Seigneur des Pions Blancs:");
        lblPlayer2.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        lblPlayer2.setTextFill(Color.web(DARK_COLOR));

        TextField txtPlayer2 = new TextField();
        txtPlayer2.setPromptText("Votre nom glorieux...");
        txtPlayer2.setStyle("-fx-font-family: 'Georgia'; -fx-background-color: " + LIGHT_COLOR + ";");
        txtPlayer2.setMaxWidth(200);

        // Bouton stylisé
        Button btnStart = new Button("LANCER LE DUEL !");
        btnStart.setStyle("-fx-background-color: " + BROWN_COLOR + "; " +
                "-fx-text-fill: white; " +
                "-fx-font-family: 'Georgia'; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 8px 20px; " +
                "-fx-background-radius: 3px; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 1);");

        btnStart.setOnMouseEntered(e -> btnStart.setStyle("-fx-background-color: " + ACCENT_COLOR + "; " +
                "-fx-text-fill: white;"));
        btnStart.setOnMouseExited(e -> btnStart.setStyle("-fx-background-color: " + BROWN_COLOR + "; " +
                "-fx-text-fill: white;"));


        //Récupère les noms saisis ou utilise des valeurs par défaut
        //Stocke les noms dans les variables player1Name et player2Name
        btnStart.setOnAction(e -> {
            this.player1Name = txtPlayer1.getText().isEmpty() ? "Maître des Noirs" : txtPlayer1.getText();
            this.player2Name = txtPlayer2.getText().isEmpty() ? "Seigneur des Blancs" : txtPlayer2.getText();
            dialog.close();
        });

        grid.add(lblPlayer1, 0, 1);
        grid.add(txtPlayer1, 1, 1);
        grid.add(lblPlayer2, 0, 2);
        grid.add(txtPlayer2, 1, 2);
        grid.add(btnStart, 0, 3, 2, 1);
        GridPane.setHalignment(btnStart, Pos.CENTER.getHpos());

        Scene scene = new Scene(grid, 420, 250);
        scene.setFill(Color.web(LIGHT_COLOR));
        dialog.setScene(scene);

        // Message de bienvenue dans la console
        System.out.println("Le duel légendaire va commencer...");

        dialog.showAndWait();

        return player1Name != null && player2Name != null;
    }


    //Permettent de récupérer les noms après fermeture de la boîte de dialogue
    public String getPlayer1Name() {
        return player1Name;
    }
    public String getPlayer2Name() {
        return player2Name;
    }
}
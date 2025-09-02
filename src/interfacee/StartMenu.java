package interfacee;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import composants.HumanPlayer;

//Configure la fenêtre principale avec titre, sous-titre et boutons
//Utilise un layout VBox pour organiser les éléments verticalement
public class StartMenu {
    private static final String Noir = "#2E2E2E";      // Noir profond
    private static final String Blanc = "#F5F5F5";     // Blanc cassé
    private static final String Marron = "#8B6B4A";     // Marron chaud


    //affichage
    public void show(Stage stage) {
        // Titre principal élégant
        Label title = new Label("DAMES CLASSIQUES");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 42));
        title.setTextFill(Color.web(Marron));
        title.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 2, 0, 0, 1);");

        // Sous-titre discret
        Label subtitle = new Label("L'art du jeu traditionnel");
        subtitle.setFont(Font.font("Georgia", 16));
        subtitle.setTextFill(Color.web(Noir).darker());

        // Boutons stables
        Button btnStart = createMenuButton("NOUVELLE PARTIE", Marron);
        Button btnRules = createMenuButton("RÈGLES", Marron);
        Button btnQuit = createMenuButton("QUITTER", Marron);

        // Ouvre une boîte de dialogue pour les noms puis lance le jeu
        btnStart.setOnAction(e -> {
            PlayerNameDialog nameDialog = new PlayerNameDialog();
            if (nameDialog.show(stage)) {
                CheckersWindow gameWindow = new CheckersWindow(
                        new HumanPlayer(nameDialog.getPlayer1Name()),
                        new HumanPlayer(nameDialog.getPlayer2Name())
                );
                gameWindow.show();
                stage.close();
            }
        });
        //Affiche une fenêtre modale avec les règles
        btnRules.setOnAction(e -> showRulesWindow());
        //Ferme l'application
        btnQuit.setOnAction(e -> stage.close());

        // Organisation visuelle
        VBox buttonBox = new VBox(12, btnStart, btnRules, btnQuit);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));

        VBox content = new VBox(8, title, subtitle, buttonBox);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(40));

        StackPane root = new StackPane(content);
        root.setStyle("-fx-background-color: " + Blanc + ";");

        Scene scene = new Scene(root, 800, 600);
        scene.setFill(Color.web(Blanc));

        stage.setTitle("Jeu de Dames - Édition Classique");
        stage.setScene(scene);
        stage.show();
    }


    //Crée des boutons cohérents avec le style de l'application
    //Ajoute des effets visuels au survol
    private Button createMenuButton(String text, String color) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + color + "; " +
                "-fx-text-fill: " + Blanc + "; " +
                "-fx-font-family: 'Georgia'; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: bold; " +
                "-fx-padding: 12px 30px; " +
                "-fx-background-radius: 3px; " +
                "-fx-cursor: hand; " +
                "-fx-min-width: 160px; " +
                "-fx-min-height: 40px;");

        // Effet de survol simple (changement de couleur seulement)
        button.setOnMouseEntered(e -> button.setStyle(
                "-fx-background-color: derive(" + color + ", -10%); " +
                        button.getStyle().substring(button.getStyle().indexOf("-fx-text-fill:"))
        ));

        button.setOnMouseExited(e -> button.setStyle(
                "-fx-background-color: " + color + "; " +
                        button.getStyle().substring(button.getStyle().indexOf("-fx-text-fill:"))
        ));

        return button;
    }
    //Crée une fenêtre modale indépendante
    //Présente les règles dans un format lisible
    //Bouton de fermeture simple
    private void showRulesWindow() {
        Stage rulesStage = new Stage(StageStyle.UTILITY);
        rulesStage.setTitle("Règles du Jeu");
        rulesStage.initModality(Modality.APPLICATION_MODAL);

        Label title = new Label("LES RÈGLES DES DAMES");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        title.setTextFill(Color.web(Marron));

        TextFlow rulesContent = new TextFlow();
        rulesContent.setPadding(new Insets(15));
        rulesContent.setLineSpacing(8);

        String[] rules = {
                "1. Plateau 10x10 cases, joué sur les cases sombres",
                "2. 20 pions par joueur, disposés sur 4 rangées",
                "3. Déplacement diagonal avant uniquement",
                "4. Prise par saut obligatoire si possible",
                "5. Promotion en dame en dernière rangée",
                "6. Les dames se déplacent sur plusieurs cases",
                "7. Victoire par capture ou blocage complet"
        };

        for (String rule : rules) {
            Text ruleText = new Text(rule + "\n");
            ruleText.setFont(Font.font("Georgia", 14));
            ruleText.setStyle("-fx-fill: " + Noir + ";");
            rulesContent.getChildren().add(ruleText);
        }


        Button closeButton = new Button("J'AI COMPRIS");
        closeButton.setStyle("-fx-background-color: " + Marron + "; " +
                "-fx-text-fill: " + Blanc + "; " +
                "-fx-font-family: 'Georgia'; " +
                "-fx-min-width: 120px;");

        closeButton.setOnAction(e -> rulesStage.close());

        VBox layout = new VBox(15, title, rulesContent, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: " + Blanc + ";");

        Scene scene = new Scene(layout, 500, 450);
        rulesStage.setScene(scene);
        rulesStage.setResizable(false);
        rulesStage.showAndWait();
    }
}
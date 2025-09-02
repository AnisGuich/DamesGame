package interfacee; //RENOMMER INTERFACE
import javafx.application.Application; // la classe de base pour toutes les applications JavaFX.
import javafx.stage.Stage; //la fenêtre principale de l'application.

public class Main extends Application {
	public static void main(String[] args) {
		launch(args); //initialise JavaFX (standard)
	}
	// démarrer l'interface graphique et d'afficher le menu principal.
	@Override
	public void start(Stage primaryStage) { //la méthode principale de l'application JavaFX.
		StartMenu menu = new StartMenu();
		menu.show(primaryStage);
	}
}
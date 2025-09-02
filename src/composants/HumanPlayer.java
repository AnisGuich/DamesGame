package composants;
/*Représente un joueur humain avec un nom personnalisable. */
public class HumanPlayer extends Player {
	private String name;

	// Constructeur avec nom
	public HumanPlayer(String name) {
		this.name = name;
	}

	// par defaut 
	public HumanPlayer() {
		this("Joueur Humain");
	}

	@Override
	public boolean isHuman() {
		return true;
	}

	@Override
	/*L'action du joueur humain est gérée ailleurs */
	public void updateGame(Game game) { }

	@Override
	/*dans le debeugage juste*/ 
	public String toString() {
		return name;
	}
}
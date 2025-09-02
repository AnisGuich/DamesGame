package composants;
/*classe abstraite pour les joueurs , implementer par les classes HumanPlayer et ComputerPlayer selon les choix du joueur*/
public abstract class Player {
	/*Indique si le joueur est humain ou Computer  */
	public abstract boolean isHuman();
	/*Met à jour l'état du jeu en fonction des actions du joueur.
	 * Pour un humain : Ne fait rien (l'interaction se fait via l'interface graphique).
	 * Pour une IA : Calcule et joue automatiquement un coup via findBestMove().
	 */
	public abstract void updateGame(Game game);
}
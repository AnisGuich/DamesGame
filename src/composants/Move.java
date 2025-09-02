package composants;

/*gere les deplacement entre les deux cases */
public class Move {

	/* point de depart et d'arrivé */
	private Point start;
	private Point end;
	private double weight;

	/* initialiser les points de depart et d'arrive */
	public Move(Point start, Point end) {
		this.start = start;
		this.end = end;
		this.weight = 0;
	}

	/* getters et setters */
	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {this.start = start;}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	/* pour afficher les mouvements lors de debbugage */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[start=" + start +", end=" + end + ", weight=" + weight + "]";
	}
}
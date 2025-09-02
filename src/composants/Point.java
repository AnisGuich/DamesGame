package composants;
/*Représenter une position sur le plateau de jeu ai lieu d'utiliser des classe predefinis de JavaFX , 
Stocker les coordonnées x (colonne) et y (ligne) d'une case .
Faciliter les calculs de déplacement, de capture, et de validation des mouvements.*/

public class Point {
    private int x;
    private int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    /*Verifier si deux points sont égaux Vérifier si un mouvement est valide (ex: start.equals(end) → invalide).
    Éviter les doublons dans les listes de positions.*/
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Point point = (Point) obj;
        return x == point.x && y == point.y;
    }
    /* juste pour voir le comportement de la classe */
    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}
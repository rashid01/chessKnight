
public class Pos {
    public int x;
    public int y;
    public int depth;

    public int fromX;
    public int fromY;
    // ways to position
    int ways;
    // ways from position
    int waysFrom;

    Pos(int x, int y, int depth, int fromX, int fromY) {
        this.x = x;
        this.y = y;
        this.depth = depth;
        this.fromX = fromX;
        this.fromY = fromY;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pos other = (Pos) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
    }

    public String toString() {
        return "(" + this.x + " " + this.y + " " + " " + this.depth + " " + " " + this.fromX + " " + this.fromY + ")";
    }

}


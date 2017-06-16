package graph;

import java.util.HashMap;

public class Coordinates2D {

    private Integer x, y;
    private HashMap<Coordinates2D, Integer> distanceTable = new HashMap<>();

    /** ------------------------ */

    public Coordinates2D() {
        this(0,0);
    }

    public Coordinates2D(Integer x, Integer y) {
        this.x = x;
        this.y = y;

        //System.out.printf("[C2d]: %d / %d\n", this.x, this.y);
    }

    /** ------------------------ */

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    /** ------------------------ */

    public Coordinates2D midTo(Coordinates2D other) {
        int tx = (int)((other.x + this.x)/2),
                ty = (int)((other.y + this.y)/2);
        return new Coordinates2D(tx, ty);
    }

    public Integer distance(Coordinates2D other) {
        // Dynamic programming!
        if(this.distanceTable.containsKey(other)) {
            return this.distanceTable.get(other);
        }

        int tx = (int)(Math.pow(other.x - this.x, 2)),
                ty = (int)(Math.pow(other.y - this.y, 2)),
                distance = (int)(Math.sqrt(tx+ty));
        this.distanceTable.put(other, distance);
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Coordinates2D that = (Coordinates2D) o;

        if (x != null ? !x.equals(that.x) : that.x != null) return false;
        return y != null ? y.equals(that.y) : that.y == null;
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}

package battleship;

enum Status {
    AFLOAT,
    SANK;
}

abstract class Unit {
    String name;
    Status status;
    String[][] area;
    String damaged = "damaged";
    int length;

    public Unit(String name, int length, Status status, String[][] area) {
        this.name = name;
        this.length = length;
        this.status = status;
        this.area = area;
    }

    public void setArea(int i, int j) {
        for (int x = 0; x < this.area.length; x++) {
            for (int y = 0; y < this.area[0].length; y++) {
                if (this.area[x][y] == null) {
                    this.area[x][y] = String.valueOf(i);
                    this.area[x][y + 1] = String.valueOf(j);
                    return;
                }
            }
        }
    }

    public Status takeDamage(int i, int j) {
        this.area[i][j] = damaged;
        this.area[i][j + 1] = damaged;

        for (int a = 0; a < this.area.length; a++) {
            for (int b = 0; b < this.area[0].length; b++) {
                if (this.area[a][b].equals(damaged)) {
                    status = Status.SANK;
                } else {
                    return Status.AFLOAT;
                }
            }
        }
        return Status.SANK;
    }
}

class AircraftCarrier extends Unit {
    public AircraftCarrier(String name, int length, Status status, String[][] area) {
        super(name, length, status, area);
    }

}

class Battleship extends Unit {
    public Battleship(String name, int length, Status status, String[][] area) {
        super(name, length, status, area);
    }

}

class Submarine extends Unit {
    public Submarine(String name, int length, Status status, String[][] area) {
        super(name, length, status, area);
    }

}

class Cruiser extends Unit {
    public Cruiser(String name, int length, Status status, String[][] area) {
        super(name, length, status, area);
    }

}

class Destroyer extends Unit {
    public Destroyer(String name, int length, Status status, String[][] area) {
        super(name, length, status, area);
    }

}
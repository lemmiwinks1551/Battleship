package battleship;

class Player {
    Field fields;
    Unit[] ships;
    int number;

    public Player(Field fields, Unit[] ships, int number) {
        this.fields = fields;
        this.ships = ships;
        this.number = number;
    }
}

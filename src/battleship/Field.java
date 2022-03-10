package battleship;

import java.util.Objects;

public class Field {
    private final static int length = 11;
    private final static int weight = 3;
    private final static String fogOfWar = "~";
    private final static String cellWithYourShip = "O";
    private final static String shipWasHit = "X";
    private final static String miss = "M";
    private final static String missMessage = "You missed!";
    private final static String hitMessage = "You hit a ship!";
    private final static String sankMessage = "You sank a ship!";
    private final static String endGameMessage = "You sank the last ship. You won. Congratulations!";
    String[][] shipsField = new String[length][length];
    String[][] shootsField = new String[length][length];

    public void fillField(Field field) {
        // заполнить поля стартовыми значениями
        char letter = 'A';
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                if (x == 0 && y == 0) {
                    field.shipsField[x][y] = " ";
                    field.shootsField[x][y] = " ";
                } else if (x == 0) {
                    field.shipsField[x][y] = String.valueOf(y);
                    field.shootsField[x][y] = String.valueOf(y);
                } else if (y == 0) {
                    field.shipsField[x][y] = String.valueOf(letter);
                    field.shootsField[x][y] = String.valueOf(letter);
                    letter++;
                } else {
                    field.shipsField[x][y] = fogOfWar;
                    field.shootsField[x][y] = fogOfWar;
                }
            }
        }
    }

    public static void printShipsField(Player player) {
        for (String[] strings : player.fields.shipsField) {
            for (int j = 0; j < length; j++) {
                System.out.print(strings[j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printShootsField(Player player) {
        for (String[] strings : player.fields.shootsField) {
            for (int j = 0; j < length; j++) {
                System.out.print(strings[j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void placeShip(int i, int j, Plane plane, Unit ship, Player player) {
        // Проверить, свободны ли ячейки
        // Если ошибок не обнаружено - нарисовать корабль
        if (!checkArea(i, j, ship, plane, player)) {
            printShip(i, j, plane, ship, player);
        }
    }

    private static boolean checkArea(int i, int j, Unit ship, Plane plane, Player player) {
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                switch (plane) {
                    case HORIZONTAL:
                        if ((x >= i && x < i + weight) && (y >= j && y <= j + ship.length + 1)) {
                            if (Objects.equals(player.fields.shipsField[x][y], cellWithYourShip)) {
                                coordinatesError(ship, player);
                                return true;
                            }
                        }
                        break;
                    case VERTICAL:
                        if ((x >= i && x <= i + ship.length + 1) && (y >= j && y < j + weight)) {
                            if (Objects.equals(player.fields.shipsField[x][y], cellWithYourShip)) {
                                coordinatesError(ship, player);
                                return true;
                            }
                        }
                        break;
                }
            }
        }
        return false;
    }

    private static void coordinatesError(Unit ship, Player player) {
        System.out.println("Error! You placed it too close to another one. Try again:\n");
        Main.checkDiagonalError(player, ship);
    }

    private static void printShip(int i, int j, Plane plane, Unit ship, Player player) {
        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                switch (plane) {
                    case HORIZONTAL:
                        if (x == i + 1 && y >= j + 1 && y <= j + ship.length) {
                            player.fields.shipsField[x][y] = cellWithYourShip;
                            ship.setArea(x, y);
                        }
                        break;
                    case VERTICAL:
                        if (y == j + 1 && x >= i + 1 && x <= i + ship.length) {
                            player.fields.shipsField[x][y] = cellWithYourShip;
                            ship.setArea(x, y);
                        }
                        break;
                }
            }
        }
    }

    public static Player[] placeShoot(Player[] players, Player player, int i, int j) {
        String sign;
        int oppositePlayer = 0;

        for (Player p : players) {
            if (p.number != player.number) {
                oppositePlayer = p.number - 1;
            }
        }

        for (int x = 0; x < length; x++) {
            for (int y = 0; y < length; y++) {
                if (x == i + 1 && y == j + 1) {
                    sign = players[oppositePlayer].fields.shipsField[x][y];
                    switch (sign) {
                        case fogOfWar, miss -> {
                            players[oppositePlayer].fields.shipsField[x][y] = miss;
                            player.fields.shootsField[x][y] = miss;
                            System.out.println(missMessage);
                            return players;
                        }
                        case cellWithYourShip -> {
                            players[oppositePlayer].fields.shipsField[x][y] = shipWasHit;
                            player.fields.shootsField[x][y] = shipWasHit;
                            if (!damageShip(players, x, y, oppositePlayer)) {
                                System.out.println(hitMessage);
                            }
                            return players;
                        }
                        case shipWasHit -> {
                            players[oppositePlayer].fields.shipsField[x][y] = shipWasHit;
                            player.fields.shootsField[x][y] = shipWasHit;
                            return players;
                        }
                    }
                }
            }
        }
        return players;
    }

    private static boolean damageShip(Player[] players, int x, int y, int oppositePlayer) {
        for (Unit ship : players[oppositePlayer].ships) {
            for (int i = 0; i < ship.area.length; i++) {
                for (int j = 0; j < ship.area[0].length - 1; j++) {
                    if (Objects.equals(ship.area[i][j], String.valueOf(x)) && Objects.equals(ship.area[i][j + 1], String.valueOf(y))) {
                        if (ship.takeDamage(i, j) == Status.SANK) {
                            if (Main.shipsAfloat(players[oppositePlayer]) != players[oppositePlayer].ships.length) {
                                System.out.println(sankMessage);
                                return true;
                            } else {
                                System.out.println(endGameMessage);
                                System.exit(0);
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

}

package battleship;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

enum Plane {
    HORIZONTAL,
    VERTICAL;
}

public class Main {
    public final static Scanner scanner = new Scanner(System.in);
    public static Plane plane;

    private final static Character[] fieldRows = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'};
    private final static int[] fieldColumns = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};


    private static String letterA;
    private static String letterB;
    private static int numA;
    private static int numB;

    public static void main(String[] args) {

        AircraftCarrier aircraftCarrierP1 = new AircraftCarrier("Aircraft Carrier", 5, Status.AFLOAT, new String[5][2]);
        Battleship battleshipP1 = new Battleship("Battleship", 4, Status.AFLOAT, new String[4][2]);
        Submarine submarineP1 = new Submarine("Submarine", 3, Status.AFLOAT, new String[3][2]);
        Cruiser cruiserP1 = new Cruiser("Cruiser", 3, Status.AFLOAT, new String[3][2]);
        Destroyer destroyerP1 = new Destroyer("Destroyer", 2, Status.AFLOAT, new String[2][2]);

        AircraftCarrier aircraftCarrierP2 = new AircraftCarrier("Aircraft Carrier", 5, Status.AFLOAT, new String[5][2]);
        Battleship battleshipP2 = new Battleship("Battleship", 4, Status.AFLOAT, new String[4][2]);
        Submarine submarineP2 = new Submarine("Submarine", 3, Status.AFLOAT, new String[3][2]);
        Cruiser cruiserP2 = new Cruiser("Cruiser", 3, Status.AFLOAT, new String[3][2]);
        Destroyer destroyerP2 = new Destroyer("Destroyer", 2, Status.AFLOAT, new String[2][2]);

        Unit[] shipsP1 = {aircraftCarrierP1, battleshipP1, submarineP1, cruiserP1, destroyerP1};
        Unit[] shipsP2 = {aircraftCarrierP2, battleshipP2, submarineP2, cruiserP2, destroyerP2};

        Field fieldsP1 = new Field();
        Field fieldsP2 = new Field();

        Field[] fields = {fieldsP1, fieldsP2};

        Player player1 = new Player(fieldsP1, shipsP1, 1);
        Player player2 = new Player(fieldsP2, shipsP2, 2);
        Player[] players = {player1, player2};

        // создать поля для игроков
        for (Player player : players) {
            for (Field field : fields) {
                player.fields.fillField(field);
            }
        }

        // расставить корабли игроков
        for (Player player : players) {
            System.out.printf("Player %d, place your ships on the game field\n", player.number);
            Field.printShipsField(player);
            placeShips(player);
            promptEnterKey();
        }

        // начать игру
        while (true) {
            for (Player player : players) {
                Field.printShootsField(player);
                System.out.println("---------------------");
                Field.printShipsField(player);
                System.out.printf("Player %d, it's your turn:\n", player.number);
                shootInput(players, player);
                promptEnterKey();
            }
        }
    }

    private static void placeShips(Player player) {
        for (Unit ship : player.ships) {
            System.out.printf("Enter the coordinates of the %s (%d cells):\n", ship.name, ship.length);
            checkDiagonalError(player, ship);
            Field.printShipsField(player);
        }
    }

    public static void checkDiagonalError(Player player, Unit ship) {
        while (true) {
            input();
            if (!letterA.equals(letterB) && numA != numB) {
                System.out.println("Error! Wrong ship location! Try again:\n");
            } else {
                break;
            }
        }
        checkLength(player, plane, ship);
    }

    private static void input() {
        String firstCoordinates = scanner.next();
        String secondCoordinates = scanner.next();

        letterA = firstCoordinates.substring(0, 1);
        letterB = secondCoordinates.substring(0, 1);
        numA = Integer.parseInt(firstCoordinates.substring(1));
        numB = Integer.parseInt(secondCoordinates.substring(1));

        if (letterA.equals(letterB)) {
            plane = Plane.HORIZONTAL;
        } else {
            plane = Plane.VERTICAL;
        }
    }

    public static void checkLength(@NotNull Player player, Plane plane, Unit ship) {
        switch (plane) {
            case VERTICAL:
                if (Math.abs(letterA.charAt(0) - letterB.charAt(0)) != ship.length - 1) {
                    System.out.printf("Error! Wrong length of the %s! Try again:\n", ship.name);
                    checkDiagonalError(player, ship);
                    return;
                }
                break;
            case HORIZONTAL:
                if (Math.abs(numA - numB) != ship.length - 1) {
                    System.out.printf("Error! Wrong length of the %s! Try again:\n", ship.name);
                    checkDiagonalError(player, ship);
                    return;
                }
                break;
        }
        convertCoordinates(ship, player);

    }

    public static void convertCoordinates(Unit ship, Player player) {
        // convert start cell
        char startLetter = 'L';
        int StartNum = 0;

        if (plane == Plane.HORIZONTAL) {
            startLetter = letterA.charAt(0);
            StartNum = Math.min(numA, numB);
        } else if (plane == Plane.VERTICAL) {
            startLetter = letterA.charAt(0) < letterB.charAt(0) ? letterA.charAt(0) : letterB.charAt(0);
            StartNum = numA;
        }

        int i = Arrays.binarySearch(fieldRows, startLetter);
        int j = Arrays.binarySearch(fieldColumns, StartNum);

        Field.placeShip(i, j, plane, ship, player);
    }

    public static void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("...");
    }

    private static void shootInput(Player[] players, Player player) {
        String coordinate = scanner.next();
        String shootLetter;
        int shootNum;

        shootLetter = coordinate.substring(0, 1);
        shootNum = Integer.parseInt(coordinate.substring(1));

        if (shootLetter.charAt(0) > fieldRows[fieldRows.length - 1]) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            shootInput(players, player);
        }

        if (shootNum > fieldColumns[fieldColumns.length - 1]) {
            System.out.println("Error! You entered the wrong coordinates! Try again:");
            shootInput(players, player);
        }

        int i = Arrays.binarySearch(fieldRows, shootLetter.charAt(0));
        int j = Arrays.binarySearch(fieldColumns, shootNum);

        players = Field.placeShoot(players, player, i, j);
    }

    public static int shipsAfloat(Player player) {
        int swankedShips = 0;
        for (Unit ship : player.ships) {
            if (!ship.status.equals(Status.AFLOAT)) {
                swankedShips++;
            }
        }
        return swankedShips;
    }
}








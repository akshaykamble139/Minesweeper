package minesweeper;

import java.util.*;

public class Main {

    public static int NUMBER_OF_MINES = 10;
    public static final int ROWS = 9;
    public static final int COLUMNS = 9;

    public static final String NUMBERS = " |123456789|";
    public static final String TABLE = "-|---------|";

    public static final ArrayList<String> COMMANDS = new ArrayList<>(Arrays.asList("free", "mine"));

    public static char[][] MINEFIELD_SPACE = new char[ROWS][COLUMNS];
    public static char[][] USER_SPACE = new char[ROWS][COLUMNS];

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("How many mines do you want on the field?");
        NUMBER_OF_MINES = scanner.nextInt();
        if (!(0 <= NUMBER_OF_MINES && NUMBER_OF_MINES <= ROWS*COLUMNS)) {
            NUMBER_OF_MINES = 10;
        }

        setMines();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                USER_SPACE[i][j] = '.';
            }
        }

        printField(USER_SPACE);

        int x,y;

        boolean firstFree = true, gameStopped = false;
        while (!checkMarkedCells()) {
            try {
                System.out.println("Set/unset mines marks or claim a cell as free:");
                x = Integer.parseInt(scanner.next());
                y = Integer.parseInt(scanner.next());
                String command = scanner.next();

                if (x >= 1 && x <= COLUMNS && y >= 1 && y <= ROWS && COMMANDS.contains(command)) {
                    if (command.equals("free")) {
                        if (!firstFree && MINEFIELD_SPACE[y - 1][x - 1] == 'X') {
                            gameStopped = true;
                            break;
                        }
                        else if (MINEFIELD_SPACE[y - 1][x - 1] == 'X') {
                            while (MINEFIELD_SPACE[y - 1][x - 1] == 'X') {
                                setMines();
                            }
                            freeCell(y-1,x-1);
                        }
                        else if (MINEFIELD_SPACE[y - 1][x - 1] == '.') {
                            freeCell(y-1,x-1);
                        }
                        else if ('0' <= MINEFIELD_SPACE[y - 1][x - 1] && MINEFIELD_SPACE[y - 1][x - 1] <= '9') {
                            USER_SPACE[y - 1][x - 1] = MINEFIELD_SPACE[y - 1][x - 1];
                        }
                        firstFree = false;
                    }
                    else {
                        if (USER_SPACE[y - 1][x - 1] == '*') {
                            USER_SPACE[y - 1][x - 1] = '.';
                        }
                        else if (USER_SPACE[y - 1][x - 1] == '.') {
                            USER_SPACE[y - 1][x - 1] = '*';
                        }
                    }
                    printField(USER_SPACE);
                }
                else {
                    System.out.println("Enter proper command!");
                }
            } catch (Exception e) {
                System.out.println("Enter proper values!");
            }
        }

        if (gameStopped) {
            System.out.println(NUMBERS);
            System.out.println(TABLE);
            for (int i = 0; i < ROWS; i++) {
                System.out.print((i+1) + "|");
                for (int j = 0; j < COLUMNS; j++) {
                    if (MINEFIELD_SPACE[i][j] == 'X') {
                        System.out.print('X');
                    }
                    else {
                        System.out.print(USER_SPACE[i][j]);
                    }
                }
                System.out.println("|");
            }
            System.out.println(TABLE);
            System.out.println("You stepped on a mine and failed!");
        }
        else {
            System.out.println("Congratulations! You found all the mines!");
        }
        scanner.close();
    }

    public static boolean checkMarkedCells() {

        boolean answer = false;

        int count = 0;

        boolean stopLoop = false;
        for (int i = 0; i < ROWS; i++) {
            if (stopLoop) {
                break;
            }
            for (int j = 0; j < COLUMNS; j++) {
                if (USER_SPACE[i][j] == '*') {
                    if (MINEFIELD_SPACE[i][j] == 'X') {
                        count++;
                    }
                    else {
                        stopLoop = true;
                        break;
                    }
                }
            }
        }
        if (count == NUMBER_OF_MINES) {
            return true;
        }

        count = 0;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (USER_SPACE[i][j] == '.') {
                    if (MINEFIELD_SPACE[i][j] == 'X') {
                        count++;
                    }
                    else {
                        return false;
                    }
                }
            }
        }
        if (count == NUMBER_OF_MINES) {
            return true;
        }
        return answer;
    }

    public static void freeCell(int row, int col) {
        if (0 <= row && row < ROWS && 0 <= col && col < COLUMNS && (USER_SPACE[row][col] == '.' || USER_SPACE[row][col] == '*')) {
            boolean hasMines = MINEFIELD_SPACE[row][col] != 'X' && MINEFIELD_SPACE[row][col] != '.';

            if (!hasMines) {
                USER_SPACE[row][col] = '/';

                for (int i = row-1; i < row+2; i++) {
                    for (int j = col-1; j < col+2; j++) {
                        if (!(i == row && j == col)) {
                            if (0 <= i && i < ROWS && 0 <= j && j < COLUMNS) {
                                if (USER_SPACE[i][j] == '.' || USER_SPACE[i][j] == '*') {
                                    freeCell(i, j);
                                }
                            }
                        }
                    }
                }
            }
            else {
                USER_SPACE[row][col] = MINEFIELD_SPACE[row][col];
            }
        }
    }

    private static void printField(char[][] space) {
        System.out.println(NUMBERS);
        System.out.println(TABLE);
        for (int i = 0; i < ROWS; i++) {
            System.out.print((i+1) + "|");
            for (int j = 0; j < COLUMNS; j++) {
                System.out.print(space[i][j]);
            }
            System.out.println("|");
        }
        System.out.println(TABLE);
    }

    private static void setMines() {
        Random random = new Random();
        int row,col, count = 0;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                MINEFIELD_SPACE[i][j] = '.';
            }
        }

        while (count < NUMBER_OF_MINES) {
            row = random.nextInt(ROWS);
            col = random.nextInt(COLUMNS);
            if (MINEFIELD_SPACE[row][col] == '.') {
                count++;
                MINEFIELD_SPACE[row][col] = 'X';
            }
        }

        int curr = 0;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (MINEFIELD_SPACE[i][j] != 'X') {
                    curr = 0;

                    if (i>0) {
                        if (j>0 && MINEFIELD_SPACE[i-1][j-1] == 'X') {
                            curr++;
                        }
                        if (j<COLUMNS-1 && MINEFIELD_SPACE[i-1][j+1] == 'X') {
                            curr++;
                        }
                        if (MINEFIELD_SPACE[i-1][j] == 'X') {
                            curr++;
                        }
                    }
                    if (i<ROWS-1) {
                        if (j>0 && MINEFIELD_SPACE[i+1][j-1] == 'X') {
                            curr++;
                        }
                        if (j<COLUMNS-1 && MINEFIELD_SPACE[i+1][j+1] == 'X') {
                            curr++;
                        }
                        if (MINEFIELD_SPACE[i+1][j] == 'X') {
                            curr++;
                        }
                    }
                    if (j>0 && MINEFIELD_SPACE[i][j-1] == 'X') {
                        curr++;
                    }
                    if (j<COLUMNS-1 && MINEFIELD_SPACE[i][j+1] == 'X') {
                        curr++;
                    }

                    if (curr > 0) {
                        MINEFIELD_SPACE[i][j] = (char) ('0' + curr);
                    }
                    else {
                        MINEFIELD_SPACE[i][j] = '.';
                    }
                }
            }
        }
    }
}

import java.util.Scanner;
import java.io.File;

/**
 * @author: ______Tse Yuk Ling (22233091)_________
 * <p>
 * For the instruction of the assignment please refer to the assignment
 * GitHub.
 * <p>
 * Plagiarism is a serious offense and can be easily detected. Please
 * don't share your code to your classmate even if they are threatening
 * you with your friendship. If they don't have the ability to work on
 * something that can compile, they would not be able to change your
 * code to a state that we can't detect the act of plagiarism. For the
 * first commit of plagiarism, regardless you shared your code or
 * copied code from others, you will receive 0 with an addition of 5
 * mark penalty. If you commit plagiarism twice, your case will be
 * presented in the exam board and you will receive a F directly.
 * <p>
 * Terms about generative AI:
 * You are not allowed to use any generative AI in this assignment.
 * The reason is straight forward. If you use generative AI, you are
 * unable to practice your coding skills. We would like you to get
 * familiar with the syntax and the logic of the Java programming.
 * We will examine your code using detection software as well as
 * inspecting your code with our eyes. Using generative AI tool
 * may fail your assignment.
 * <p>
 * If you cannot work out the logic of the assignment, simply contact
 * us on Discord. The teaching team is more the eager to provide
 * you help. We can extend your submission due if it is really
 * necessary. Just please, don't give up.
 */
public class Sokoban {
    /**
     * The following constants are variables that you can use in your code.
     * Use them whenever possible. Try to avoid writing something like:
     * if (input == 'W') ...
     * instead
     * if (input == UP) ...
     */
    public static final char UP = 'W';
    public static final char DOWN = 'S';
    public static final char LEFT = 'A';
    public static final char RIGHT = 'D';
    public static final char PLAYER = 'o';
    public static final char BOX = '@';
    public static final char WALL = '#';
    public static final char GOAL = '.';
    public static final char BOXONGOAL = '%';

    /**
     * Finished. You are not allowed to touch this method.
     * The main method.
     */
    public static void main(String[] args) {
        new Sokoban().runApp();
    }


    /**
     * All coding of this method has been finished.
     * You are not supposed to add or change any code in this method.
     * However, you are required to add comments after every // to explain the code below.
     */
    public void runApp() {

        String mapfile = "map1.txt"; //change this to test other maps
        char[][] map = readmap(mapfile); //define the map as "map1.txt"
        char[][] oldMap = readmap(mapfile); //define the oldMap as "map1.txt"

        if (map == null) { //if there is no map
            System.out.println("Map file not found");
            return;
        }
        int[] start = findPlayer(map); //define start as the return int[] of the findPlayer method
        if (start.length == 0) { //if no player found
            System.out.println("Player not found");
            return;
        }
        int row = start[0];
        int col = start[1];
        while (!gameOver(map)) { //it will loop the following while loop until the game is over
            printMap(map);
            System.out.println("\nPlease enter a move (WASD): ");
            char input = readValidInput(); //to get player's input
            if (input == 'q')  //to check if input is quitting the game
                break;
            if (input == 'r') {  //to check if input is restarting the game
                map = readmap(mapfile);
                row = start[0];    //update the row and col to the initial state
                col = start[1];
                continue;
            }
            if (input == 'h') { //to check if input is requesting for help
                printHelp();
            }
            if (!isValid(map, row, col, input)) //to check if the move of player is valid
                continue;
            movePlayer(map, row, col, input); //move player

            fixMap(map, oldMap);  //copies back the original map so when restarting the game brings it back to the initial state

            int[] newPos = findPlayer(map); //define newPos as the return int[] of the findPlayer method
            row = newPos[0]; //update the row and col
            col = newPos[1];

        }
        System.out.println("Bye!");
    }

    /**
     * Print the Help menu.
     * TODO:
     * <p>
     * Inspect the code in runApp() and find out the function of each characters.
     * The first one has been done for you.
     */
    public void printHelp() {
        System.out.println("Sokoban Help:");
        System.out.println("Move up: W");
        System.out.println("Move down: S");
        System.out.println("Move left: A");
        System.out.println("Move right: D");
        System.out.println("Print help: h");
        System.out.println("Restart the game: r");
        System.out.println("Quit the game: q");
    }

    /**
     * Reading a valid input from the user.
     * <p>
     * TODO
     * <p>
     * <p>
     * This method will return a character that the user has entered. However, if a user enter an invalid character (e.g. 'x'),
     * the method should keep prompting the user until a valid character is entered. Noted, there are all together 7 valid characters
     * which you need to figure out yourself.
     */
    public char readValidInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            char input = scanner.next().charAt(0);
            if (input == UP || input == DOWN || input == LEFT || input == RIGHT ||
                    input == 'r' || input == 'h' || input == 'q') {
                return input;
            }
            System.out.println("Invalid input. Please try again.");
        }
    }


    /**
     * Mysterious method.
     * <p>
     * TODO
     * <p>
     * We know this method is to "fix" the map. But we don't know how it does and why it is needed.
     * You need to figure out the function of this method and implement it accordingly.
     * <p>
     * You are given an additional demo program that does not implement this method.
     * You can run them to see the difference between the two demo programs.
     */
    public void fixMap(char[][] map, char[][] oldMap) {
        //This method copies back the original map so when
        //restarting the game brings it back to the initial state
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == ' ' && oldMap[i][j] == GOAL) {
                    map[i][j] = GOAL;
                }
                if (map[i][j] == ' ' && oldMap[i][j] == BOXONGOAL) {
                    map[i][j] = GOAL;
                }
            }
        }
    }

    /**
     * To move a box in a map.
     * <p>
     * TODO
     * <p>
     * This method will move a box in the map. The box will be moved to the direction specified by the parameter "direction".
     * You must call this method somewhere in movePlayer() method.
     * <p>
     * After this method, a box should be moved to the new position from the coordinate [row, col] according to the direction.
     * For example, if [row, col] is [2, 5] and the direction is 'S', the box should be moved to [3, 5].
     * <p>
     * If a box is moved to a goal, the box should be marked as BOXONGOAL.
     * If a box is moved to a non-goal, the box should be marked as BOX.
     * You should set the original position of the box to ' ' in this method.
     * <p>
     * Note, you may always assume that this method is called when the box can be moved to the direction.
     * During grading, we will never call this method when the box cannot be moved to the direction.
     */
    public void moveBox(char[][] map, int row, int col, char direction) {
        if (direction == DOWN) {
            if (map[row + 1][col] == GOAL) {
                map[row + 1][col] = BOXONGOAL;
            } else {
                map[row + 1][col] = BOX;
            }
            map[row][col] = ' ';
        } else if (direction == LEFT) {
            if (map[row][col - 1] == GOAL) {
                map[row][col - 1] = BOXONGOAL;
            } else {
                map[row][col - 1] = BOX;
            }
            map[row][col] = ' ';
        } else if (direction == RIGHT) {
            if (map[row][col + 1] == GOAL) {
                map[row][col + 1] = BOXONGOAL;
            } else {
                map[row][col + 1] = BOX;
            }
            map[row][col] = ' ';
        } else if (direction == UP) {
            if (map[row - 1][col] == GOAL) {
                map[row - 1][col] = BOXONGOAL;
            } else {
                map[row - 1][col] = BOX;
            }
            map[row][col] = ' ';
        }

    }

    /**
     * To move the player in the map.
     * <p>
     * TODO
     * <p>
     * This method will move the player in the map. The player will be moved to the direction specified by the parameter "direction".
     * <p>
     * After this method, the player should be moved to the new position from the coordinate [row, col] according to the direction.
     * At the same time, the original position of the player should be set to ' '.
     * <p>
     * During the move of the player, it is also possible that a box is also moved.
     * <p>
     * Note, you may always assume that this method is called when the player can be moved to the direction.
     * During grading, we will never call this method when the player cannot be moved to the direction.
     */
    public void movePlayer(char[][] map, int row, int col, char direction) {
        if (direction == DOWN) {
            if (map[row + 1][col] == BOX || map[row + 1][col] == BOXONGOAL) {
                moveBox(map, row + 1, col, direction);
            }
            map[row][col] = ' ';
            map[row + 1][col] = PLAYER;

        }

        if (direction == LEFT) {
            if (map[row][col - 1] == BOX || map[row][col - 1] == BOXONGOAL) {
                moveBox(map, row, col - 1, direction);
            }
            map[row][col] = ' ';
            map[row][col - 1] = PLAYER;
        }

        if (direction == RIGHT) {
            if (map[row][col + 1] == BOX || map[row][col + 1] == BOXONGOAL) {
                moveBox(map, row, col + 1, direction);
            }
            map[row][col] = ' ';
            map[row][col + 1] = PLAYER;
        }

        if (direction == UP) {
            if (map[row - 1][col] == BOX || map[row - 1][col] == BOXONGOAL) {
                moveBox(map, row - 1, col, direction);
            }
            map[row][col] = ' ';
            map[row - 1][col] = PLAYER;
        }

    }

    /**
     * To check if the game is over.
     * <p>
     * TODO
     * <p>
     * This method should return true if the game is over, false otherwise.
     * The condition for game over is that there is no goal left in the map that is not covered by a box.
     * <p>
     * According to this definition, if the number of goal is actually more than the number of boxes,
     * the game will never end even through all boxes are placed on the goals.
     */
    public boolean gameOver(char[][] map) {

        int goal = 0;
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == GOAL) {
                    goal++;
                }

            }
        }
        return (goal == 0);
    }

    /**
     * To count the number of rows in a file.
     * <p>
     * TODO
     * <p>
     * This method should return the number of rows in the file which filename is stated in the argument.
     * If the file is not found, it should return -1.
     */
    public int numberOfRows(String fileName) {
        File inputFile = new File(fileName);
        int rows = 0;
        if (inputFile.exists()) {
            try {
                Scanner scanner = new Scanner(inputFile);
                while (scanner.hasNextLine()) {
                    scanner.nextLine();
                    rows++;
                }
            } catch (Exception e) {
                return -1;
            }
        }
        return rows;
    }

    /**
     * To read a map from a file.
     * <p>
     * TODO
     * <p>
     * This method should return a 2D array of characters which represents the map.
     * This 2D array should be read from the file which filename is stated in the argument.
     * If the file is not found, it should return null.
     * <p>
     * The number of columns in each row may be different. However, there is no restriction on
     * the number of columns that is declared in the array. You can declare the number of columns
     * in your array as you wish, as long as it is enough to store the map.
     * <p>
     * That is, if the map is as follow,
     * ####
     * #.@o#
     * #  #
     * ###
     * your array may be declared as
     * char[][] map = {{'#', '#', '#', '#'},
     *                 {'#', '.', '@', 'o', '#'},
     *                 {'#', ' ', ' ', '#'},
     *                 {'#', '#', '#'} };
     * or something like
     * char[][] map = {{'#', '#', '#', '#', ' ', ' ', ' '},
     *                 {'#', '.', '@', 'o', '#', ' ', ' '},
     *                 {'#', ' ', ' ', '#', ' ', ' ', ' '},
     *                 {'#', '#', '#', ' ', ' ', ' ', ' '} };
     */
    public char[][] readmap(String fileName) {
        File inputFile = new File(fileName);
        int rows = numberOfRows(fileName);
        char[][] map = new char[rows][17];

        try {
            Scanner scanner = new Scanner(inputFile);


            for (int i = 0; i < map.length; i++) {
                String line = scanner.nextLine();
                for (int j = 0; j < map[i].length; j++) {

                    if (j >= line.length()) {
                        map[i][j] = ' ';
                    } else {
                        map[i][j] = line.charAt(j);
                    }
                }
            }

            return map;

        } catch (Exception e) {
            return null;
        }

    }

    /**
     * To find the coordinate of player in the map.
     * <p>
     * TODO
     * <p>
     * This method should return a 2D array that stores the [row, col] of the player in the map.
     * For example, if the map is as follow,
     * ####
     * #.@o#
     * #  #
     * ###
     * this method should return {1, 3}.
     * <p>
     * In case there is no player in the map, this method should return null.
     */
    public int[] findPlayer(char[][] map) {
        int[] abc = new int[2];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == PLAYER) {
                    abc[0] = i;
                    abc[1] = j;
                }
            }
        }
        return abc;
    }

    /**
     * To check if a move is valid.
     * <p>
     * TODO
     * <p>
     * This method should return true if the move is valid, false otherwise.
     * The parameter "map" represents the map.
     * The parameter "row" and "col" indicates where the player is.
     * The parameter "direction" indicates the direction of the move.
     * At the end of the method, this method should not change any content of the map.
     * <p>
     * The physics of the game is as follow:
     * 1. The player can only move to a position that is not occupied by a wall or a box.
     * 2. If the player is moving to a position that is occupied by a box, the box can only be moved to a position that is not occupied by a wall or a box.
     * <p>
     * Thus, in the following condition, the player can move to the right
     * o #   <-- there is a space
     * o@ #  <-- there is a space right to the box.
     * In the following condition, the player cannot move to the right
     * o#    <-- there is a wall
     * o@#   <-- there is a wall right to the box.
     * o@@ # <-- there is a box right to the box.
     */
    public boolean isValid(char[][] map, int row, int col, char direction) {
        if (direction == UP && map[row - 1][col] == WALL)
            return false;
        else if (direction == DOWN && map[row + 1][col] == WALL)
            return false;
        else if (direction == LEFT && map[row][col - 1] == WALL)
            return false;
        else if (direction == RIGHT && map[row][col + 1] == WALL)
            return false;

        else if (direction == UP && (map[row - 1][col] == BOX || map[row - 1][col] == BOXONGOAL) && map[row - 2][col] == WALL)
            return false;
        else if (direction == DOWN && (map[row + 1][col] == BOX || map[row + 1][col] == BOXONGOAL) && map[row + 2][col] == WALL)
            return false;
        else if (direction == LEFT && (map[row][col - 1] == BOX || map[row][col - 1] == BOXONGOAL) && map[row][col - 2] == WALL)
            return false;
        else if (direction == RIGHT && (map[row][col + 1] == BOX || map[row][col + 1] == BOXONGOAL) && map[row][col + 2] == WALL)
            return false;

        else if (direction == UP && map[row - 1][col] == BOX && map[row - 2][col] == BOX)
            return false;
        else if (direction == DOWN && map[row + 1][col] == BOX && map[row + 2][col] == BOX)
            return false;
        else if (direction == LEFT && map[row][col - 1] == BOX && map[row][col - 2] == BOX)
            return false;
        else if (direction == RIGHT && map[row][col + 1] == BOX && map[row][col + 2] == BOX)
            return false;

        else if (direction == UP && map[row - 1][col] == BOXONGOAL && map[row - 2][col] == BOX)
            return false;
        else if (direction == DOWN && map[row + 1][col] == BOXONGOAL && map[row + 2][col] == BOX)
            return false;
        else if (direction == LEFT && map[row][col - 1] == BOXONGOAL && map[row][col - 2] == BOX)
            return false;
        else if (direction == RIGHT && map[row][col + 1] == BOXONGOAL && map[row][col + 2] == BOX)
            return false;

        else if (direction == UP && map[row - 1][col] == BOX && map[row - 2][col] == BOXONGOAL)
            return false;
        else if (direction == DOWN && map[row + 1][col] == BOX && map[row + 2][col] == BOXONGOAL)
            return false;
        else if (direction == LEFT && map[row][col - 1] == BOX && map[row][col - 2] == BOXONGOAL)
            return false;
        else if (direction == RIGHT && map[row][col + 1] == BOX && map[row][col + 2] == BOXONGOAL)
            return false;

        else
            return true;
    }

    /**
     * To print the map.
     * <p>
     * TODO
     * <p>
     * This method should print the map in the console.
     * At the top row, it should print a space followed by the last digit of the column indexes.
     * At the leftmost column, it should print the last two digits of row indexes, aligning to the left.
     */
    public void printMap(char[][] map) {
        System.out.print(" ");
        for (int j = 0; j < map[0].length; j++) {
            System.out.print(j % 10);
        }
        System.out.println();

        for (int i = 0; i < map.length; i++) {
            System.out.print(i % 100);
            for (int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
            System.out.println();
        }
    }

}
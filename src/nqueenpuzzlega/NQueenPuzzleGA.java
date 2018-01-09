/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nqueenpuzzlega;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author spiros
 */
public class NQueenPuzzleGA {

    private static final int INIT_CHROMOSOMES = 10000;
    private static final int INIT_BOARD_SIZE = 8;
    private static final int FITNESS_UTOPIA = 100;
    private static final int FITNESS_MINIMUM = FITNESS_UTOPIA - ((INIT_BOARD_SIZE * INIT_BOARD_SIZE) / 2 + INIT_BOARD_SIZE);
    private static final int GENERATIONS_LIMIT = 1000;

    public static void pr(int[][] obj) {
        for (int[] is : obj) {
            for (int i : is) {
                System.out.print(i);
                System.out.print(" ");
            }
            System.out.println("");
        }
        for (int i = 0; i < INIT_BOARD_SIZE; i++) {
            System.out.print("==");
        }
        System.out.println("");
    }

    public static void println(Object obj) {
        System.out.println(obj);

    }

    public static int sumBoard(int[][] numbers) {
        int sum[] = new int[INIT_BOARD_SIZE];
        int ttl = 0;
        for (int i = 0; i < numbers.length; i++) {
            int total = 0;
            for (int j = 0; j < numbers[0].length; j++) {
                total += numbers[i][j];
            }
            sum[i] = total;
        }

        for (int i : sum) {
            ttl += i;
        }
        return ttl;
    }

    public static int measureFitness(int[][] board, boolean debug) {
        int fitness = FITNESS_UTOPIA;

        for (int row = 0; row < INIT_BOARD_SIZE; row++) {

            for (int col = 0; col < INIT_BOARD_SIZE; col++) {

                if (board[row][col] == 1) {
                    //System.out.print("Q"+row+""+col+"");
                    if (debug) {
                        System.out.print("[Q]");
                    }

                    //CHECK SAME ROW
                    int sumrow = 0;
                    for (int i : board[row]) {
                        sumrow += i;
                    }
                    if (sumrow > 1) {
                        if (debug) {
                            System.out.print("R");
                        }
                        fitness = fitness - 1;
                    }

                    //CHECK SAME COLUMN
                    int sumcol = 0;
                    for (int i = 0; i < INIT_BOARD_SIZE; i++) {
                        sumcol += board[i][col];
                        //System.out.print(row+"."+i);
                    }
                    if (sumcol > 1) {
                        if (debug) {
                            System.out.print("C");
                        }
                        fitness = fitness - 1;
                    }

                    //CHECK DIAGONALLY
                    //UP RIGHT
                    int sumrowcol = 0;
                    int i = row;
                    int j = col;
                    while (i < INIT_BOARD_SIZE - 1 && j < INIT_BOARD_SIZE - 1) {
                        i++;
                        j++;
                        sumrowcol += board[i][j];
                    }

                    //DOWN LEFT
                    i = row;
                    j = col;
                    while (i > 0 && j > 0) {
                        i--;
                        j--;
                        sumrowcol += board[i][j];
                    }

                    //DOWN RIGHT
                    i = row;
                    j = col;
                    while (i < INIT_BOARD_SIZE - 1 && j > 0) {
                        j--;
                        i++;
                        sumrowcol += board[i][j];
                    }

                    //UP RIGHT
                    i = row;
                    j = col;
                    while (i > 0 && j < INIT_BOARD_SIZE - 1) {
                        j++;
                        i--;
                        sumrowcol += board[i][j];
                    }

                    if (sumrowcol >= 1) {
                        if (debug) {
                            System.out.print("D");
                        }
                        fitness = fitness - 1;
                    }

                    if (debug) {
                        System.out.print("");
                    }
                } else {
                    if (debug) {
                        System.out.print("[ ]");
                    }
                }

            }
            if (debug) {
                System.out.println("");
            }
        }

        if (debug) {
            //System.out.println("Fitness: " + fitness);
        }
        return fitness;
    }

    public static int measureFitness(String DNA, boolean debug) {
        int[][] board = new int[INIT_BOARD_SIZE][INIT_BOARD_SIZE];
        int[] dnaInt = new int[INIT_BOARD_SIZE];
        String[] dna = DNA.split("(?!^)");
        for (int i = 0; i < dna.length; i++) {
            int j = Integer.valueOf(dna[i]);
            board[i][j] = 1;
        }
        if (debug) {
            System.out.println("DNA: " + DNA);
        }
        return measureFitness(board, debug);
    }

    public static Object[] getNextGeneration(String[] DNA) {
        ArrayList<String> ELLIGIPLE_PARENTS = new ArrayList<String>();
        int fitness = 0;
        for (int i = 0; i < DNA.length; i++) {
            fitness = measureFitness(DNA[i], false);
            if (fitness == FITNESS_UTOPIA) {
                //println(DNA[i]);
                measureFitness(DNA[i], true);
                //System.exit(0);
            }
            if (fitness > FITNESS_MINIMUM) {
                ELLIGIPLE_PARENTS.add(DNA[i]);
            }
        }
        String[] d = new String[ELLIGIPLE_PARENTS.size()];

        for (int i = 0; i < ELLIGIPLE_PARENTS.size(); i++) {
            d[i] = ELLIGIPLE_PARENTS.get(i);
        }

        return d;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Random rnd = new Random();

        String[] DNA = new String[INIT_CHROMOSOMES];
        int[] FITNESS = new int[INIT_CHROMOSOMES];
        for (int i = 0; i < INIT_CHROMOSOMES; i++) {
            for (int j = 0; j < INIT_BOARD_SIZE; j++) {
                if (DNA[i] != null) {
                    DNA[i] = DNA[i] + String.valueOf(rnd.nextInt(INIT_BOARD_SIZE));
                } else {
                    DNA[i] = String.valueOf(rnd.nextInt(INIT_BOARD_SIZE));
                }
            }
        }

        int fitness = 0;
        int totalboards = 0;
        Set<String> UNIQUE_BOARDS = new HashSet<String>();
        for (int generations = 0; generations < GENERATIONS_LIMIT; generations++) {
            //println("Generation " + generations + " of " + GENERATIONS_LIMIT);
            String[] ELLIGIPLE_PARENTS = (String[]) getNextGeneration(DNA);

            int nextgenerationsize = ELLIGIPLE_PARENTS.length;
            int combo = 0;
            if (nextgenerationsize % 2 == 0) {
                combo = nextgenerationsize;
            } else {
                combo = nextgenerationsize - 1;
            }

            DNA = null;
            DNA = new String[combo];
            for (int i = 0; i < combo; i++) {
                int combineat = rnd.nextInt(INIT_BOARD_SIZE);
                String CHILD = ELLIGIPLE_PARENTS[i].substring(0, combineat) + ELLIGIPLE_PARENTS[combo - i - 1].substring(combineat, INIT_BOARD_SIZE);

                String mutationstring = String.valueOf(rnd.nextInt(INIT_BOARD_SIZE));
                int mutationposition = rnd.nextInt(INIT_BOARD_SIZE);
                if (mutationposition == 0) {
                    mutationposition = 1;
                }

                //println(ELLIGIPLE_PARENTS[i] + " - " + ELLIGIPLE_PARENTS[(combo - i - 1)] + " -> [" + combineat + "] -> " + CHILD);
                CHILD = CHILD.substring(0, mutationposition - 1) + mutationstring + CHILD.substring(mutationposition, INIT_BOARD_SIZE);
                fitness = measureFitness(CHILD, false);
                DNA[i] = CHILD;
                if (fitness == FITNESS_UTOPIA) {

                    if (!UNIQUE_BOARDS.contains(DNA[i])) {
                        totalboards++;
                        UNIQUE_BOARDS.add(DNA[i]);
                    }
                }
                //println("" + generations + ";" + fitness);
            }

        }
        println("");
        println("Total boards found: " + totalboards);
        println("Total unique boards found: " + UNIQUE_BOARDS.size());
        println("Unique DNA list follows...");
        for (Iterator<String> it = UNIQUE_BOARDS.iterator(); it.hasNext();) {
            println(it.next());
        }
        /*
         for (Map.Entry<String, Integer> entry : ELLIGIPLE_PARENTS.entrySet()) {
         String key = entry.getKey();
         Integer value = entry.getValue();
         System.out.println(key + " - " + value);
         }*/
        System.exit(0);
    }

}

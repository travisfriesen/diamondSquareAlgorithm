/*
Code written May 2024 by Travis Friesen
*/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class DiamondSquareGeneration {

    public static void main(String[] args) {
        initDSG();
    }

    public static void initDSG(){
        Scanner input = new Scanner(System.in);
        int power;
        int max;
        int rMax;
        System.out.println("Welcome to the Diamond Square Generation program, warning this might suck idk");
        System.out.print("Enter a power of 2 for the size of the board:");
        power = input.nextInt();
        System.out.print("Enter the max height (1-inputted value):");
        max = input.nextInt();
        System.out.print("Enter the random range (-inputted value - inputted value)(should be equal or higher than the power):");
        rMax = input.nextInt();
        System.out.println("Created board, and outputted to file.");
        int[][] data = diamondSquareGeneration(power, max, rMax);
        outputToCSV(data);
    }

    //debug instance
    public static void initDSG(int power, int max, int rMax){
        int[][] data = diamondSquareGeneration(power, max, rMax);
        outputToCSV(data);
    }

    public static int[][] diamondSquareGeneration(int power, int max, int rMax) {
        int size = (int) Math.pow(2, power) + 1;
        int[][] data = genArray(size);

        genCorners(data, max);
        runLoop(data, rMax, size);
        return data;
    }

    public static void runLoop(int[][] data, int rMax, int size) {
        if (!checkCompleted(data)) {
            squareStep(data, size, rMax);
            diamondStep(data, size, rMax);
            if (rMax > 1) {
                runLoop(data, rMax - 1, size / 2);
            } else {
                runLoop(data, rMax, size);
            }
        }

    }

    public static boolean checkCompleted(int[][] data) {
        boolean isCompleted = true;
        for (int i = 0; i < data.length && isCompleted; i++) {
            for (int j = 0; j < data.length && isCompleted; j++) {
                if (data[i][j] == 0) {
                    isCompleted = false;
                    break;
                }
            }
        }
        return isCompleted;
    }

    public static void squareStep(int[][] data, int cellSize, int randomMax) {
        for (int x = 0; x < data.length-1; x+=cellSize) {
            for (int y = 0; y < data.length-1; y+=cellSize) {
                generateXSlot(data, x, y, cellSize, randomMax);
            }
        }
    }

    public static void diamondStep(int[][] data, int cellSize, int randomMax) {
        int multiplier = cellSize / 2;
        for (int x = 0; x < data.length-1; x+=cellSize) {
            for (int y = 0; y < data.length-1; y+=cellSize) {
                generateDiamondSlot(data, 0, y+multiplier, cellSize, randomMax);
                generateDiamondSlot(data, x+multiplier, 0, cellSize, randomMax);
                generateDiamondSlot(data, x+(multiplier*2), y+multiplier, cellSize, randomMax);
                generateDiamondSlot(data, x+multiplier, y+(multiplier*2), cellSize, randomMax);
            }
        }
    }

    public static void generateDiamondSlot(int[][] data, int x, int y, int multiplier, int randomRange) {
        multiplier /= 2;
        Random random = new Random();
        int rng = random.nextInt((randomRange*2)+1) - randomRange;
        int value = 0;
        int count = 0;

        if (data[x][y] == 0) {

            if (x - multiplier < data.length && x - multiplier >= 0 && y < data.length && data[x - multiplier][y] != 0) {
                value += data[x - multiplier][y];
                count++;
            }
            if (x + multiplier < data.length && y < data.length && data[x + multiplier][y] != 0) {
                value += data[x + multiplier][y];
                count++;
            }
            if (x < data.length && y - multiplier < data.length && y - multiplier >= 0 && data[x][y - multiplier] != 0) {
                value += data[x][y - multiplier];
                count++;
            }
            if (x < data.length && y + multiplier < data.length && x >= 0 && data[x][y + multiplier] != 0) {
                value += data[x][y + multiplier];
                count++;
            }
            if (count > 0) {
                value /= count;
            }
            value += rng;
            if (value <= 0){
                value = 1;
            }
            data[x][y] = value;
        }
    }

    public static void generateXSlot(int[][] data, int x, int y, int multiplier, int randomRange) {
        multiplier /= 2;
        x += multiplier;
        y += multiplier;
        Random random = new Random();
        int rng = random.nextInt((randomRange*2)+1) - randomRange;
        int value = 0;
        int count = 0;

        if (x-multiplier < data.length && y+multiplier < data.length && data[x - multiplier][y + multiplier] != 0) {
            value += data[x-multiplier][y + multiplier];
            count++;
        }
        if (x+multiplier < data.length && y+multiplier < data.length && data[x+multiplier][y+multiplier] != 0) {
            value += data[x+multiplier][y+multiplier];
            count++;
        }
        if (x-multiplier < data.length && y-multiplier < data.length && y-multiplier >= 0 && data[x-multiplier][y-multiplier] != 0) {
            value += data[x-multiplier][y-multiplier];
            count++;
        }
        if (x-multiplier < data.length && y+multiplier < data.length && x-multiplier >= 0 && data[x-multiplier][y+multiplier] != 0) {
            value += data[x+multiplier][y-multiplier];
            count++;
        }
        if (count > 0) {
            value /= count;
        }
        value += rng;

        if (value <= 0){
            value = 1;
        }
        data[x][y] = value;
    }

    public static void genCorners(int[][] data, int max) {
        data[0][0] = genInt(max);
        data[0][data.length-1] = genInt(max);
        data[data.length-1][0] = genInt(max);
        data[data.length-1][data.length-1] = genInt(max);
    }

    public static int genInt(int max) {
        Random random = new Random();
        return random.nextInt(max)+1;
    }

    public static int[][] genArray(int size) {
        int[][] data = new int[size][size];

        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data.length; j++) {
                data[i][j] = 0;
            }
        }
        return data;
    }

    public static void outputToCSV(int[][] data) {
        try {
            File file = new File("output.csv");
            FileWriter writer = new FileWriter(file, false);
            String output = "";
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data.length; j++) {
                    if (j+1 == data.length) {
                        output += data[i][j];
                    } else {
                        output += data[i][j] + ", ";
                    }
                }
                writer.write(output);
                output = "\n";
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Exception" + e);
        }
    }
}

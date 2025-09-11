package com;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;



public class FortuneFileReader {
    public final static Path path = Paths.get("fortune-br.txt");
	private int NUM_FORTUNES = 0;
    private boolean debug;

    public FortuneFileReader(boolean debug) {
        this.debug = debug;
    }

    public int countFortunes() throws FileNotFoundException {

        int lineCount = 0;

        InputStream is = new BufferedInputStream(new FileInputStream(
                path.toString()));
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                is))) {

            String line = br.readLine();
            while (line != null) {

                if (line.equals("%"))
                    lineCount++;

                line = br.readLine();

            }// fim while

            if (debug) System.out.println(lineCount);
        } catch (IOException e) {
            if (debug) System.out.println("SHOW: Excecao na leitura do arquivo.");
        }
        return lineCount;
    }

    public void parser(HashMap<Integer, String> hm)
            throws FileNotFoundException {

        InputStream is = new BufferedInputStream(new FileInputStream(
                path.toString()));
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                is))) {

            int lineCount = 0;
            String line = br.readLine();
            while (line != null) {
                if (line.equals("%"))
                    lineCount++;

                line = br.readLine();
                StringBuffer fortune = new StringBuffer();
                while (line != null && !line.equals("%")) {
                    fortune.append(line + "\n");
                    line = br.readLine();
                }

                if (fortune.length() > 0) {
                    hm.put(lineCount, fortune.toString());
                    if (debug) {
                        System.out.println(fortune.toString());
                        System.out.println(lineCount);
                    }
                }
            }// fim while

        } catch (IOException e) {
            if (debug) System.out.println("SHOW: Excecao na leitura do arquivo.");
        }
    }

    public String read(HashMap<Integer, String> hm)
            throws FileNotFoundException {

        if (hm == null || hm.isEmpty()) {
            String result = "No fortunes available.";
            return result;
        }

        SecureRandom random = new SecureRandom();
        int randKey = random.nextInt(hm.size()) + 1;
        String fortune = hm.get(randKey);

        if (fortune != null) {
            String result = "Random Fortune:\n" + fortune + "\n Key found: " + randKey;
            return result;
        } else {
            System.out.println("No fortune at: " + randKey);
            return "No fortune found at key: " + randKey;
        }
    }

    public void write(HashMap<Integer, String> hm, StringBuilder newFortune) throws FileNotFoundException {
        int newKey = hm.size() + 1;
        hm.put(newKey, newFortune.toString());
        System.out.println("---------------------------------\n\tNew fortune:\n" + newFortune + "\nKey assigned: " + newKey);

        Path newPath = Paths.get("fortune-br-new.txt");
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(newPath.toString()))) {
            for (String fortune : hm.values()) {
                bw.write("%\n");
                bw.write(fortune);
                bw.write("%\n");
            }
        } catch (IOException e) {
            System.out.println("SHOW: Excecao ao escrever no novo arquivo.");
        }
        System.out.println("New fortune added and all fortunes written to: " + newPath);
    }
}

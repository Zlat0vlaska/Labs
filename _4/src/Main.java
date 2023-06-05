import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        PrintWriter writer;
        File file = new File("main.html");
        String buffer, subString, charToRemove = "=\"";
        Scanner scanner;
        Pattern p = Pattern.compile("\\w{1,}\\s*=\\s*\"\\w{1,}\"");
        Matcher m ;
        ArrayList<String> atributes = new ArrayList<>();

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        try {
            writer = new PrintWriter("Data.txt","UTF-8");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        while(scanner.hasNextLine()) {
            buffer = scanner.nextLine();
            m = p.matcher(buffer);
            while (m.find())
            {
                subString = m.group();
                writer.println(subString);
                subString = subString.replaceAll("\\s*=\\s*\"[\\w]*\"","");
                atributes.add(subString);
            }
        }

        writer.close();

        Collections.sort(atributes);
        System.out.println(atributes);
        Status(atributes);

    }
    /**
     * Данный метод выводит статистики использования атрибутов
     * @param atributes - AreyList типа string в котором хранятся все атрибут
     */

    private static void Status(ArrayList<String> atributes)
    {
        int size, newSize;
        while (atributes.size()> 0)
        {
            size = atributes.size();
            System.out.print("Количество атрибутов:"+atributes.get(0)+" = ");
            atributes.removeAll(Collections.singleton(atributes.get(0)));
            newSize = atributes.size();
            System.out.println(size - newSize);
            size = newSize;
        }
    }
}

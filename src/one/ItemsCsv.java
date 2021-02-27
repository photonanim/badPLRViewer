package one;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ItemsCsv {
    public static Map<Integer, String> itemsById = new HashMap<Integer, String>();
    public static Map<String, Integer> idByItems = new HashMap<String, Integer>();
    static {
        new BufferedReader(new InputStreamReader(ItemsCsv.class.getClassLoader().getResourceAsStream("items.csv"))).lines().forEach(line -> {
            String[] splitten = line.split(",");
            int id = Integer.parseInt(splitten[0]);
            String name = splitten[1];
            itemsById.put(id, name);
            idByItems.put(name, id);
        });
    }
}
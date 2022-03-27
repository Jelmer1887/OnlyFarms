package nl.tue.onlyfarms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * This class contains a load of Strings and values that can be used for debugging
 * and placeholder purposes
 * It also contains a few methods to get a value from this class
 * */
public class DebugVals {
    private static final Random generator = new Random();
    public enum Type {
        USER,
        PRODUCT,
        STORE
    }

    public static String getRandom(Type type) {
        if (type == Type.USER) {
            return userNames.get(generator.nextInt(userNames.size()));
        } else if (type == Type.PRODUCT) {
            return productNames.get(generator.nextInt(productNames.size()));
        } else if (type == Type.STORE) {
            return storeNames.get(generator.nextInt(storeNames.size()));
        }
        throw new IllegalArgumentException("not a valid type entered");
    }

    public static List<String> userNames = Arrays.asList("Henk",
            "Jos",
            "James",
            "Robert",
            "Mary",
            "Patricia",
            "Jennifer",
            "William",
            "Michael",
            "Susan",
            "Karen",
            "David",
            "Thomas",
            "Lisa",
            "Nancy"
    );

    public static List<String> productNames = Arrays.asList(
            "Apple",
            "Pear",
            "Banana",
            "Kiwi",
            "Jackfruit",
            "Salak",
            "Durian",
            "Dragonfruit",
            "Grapes",
            "Blueberries",
            "Bacon",
            "Steak",
            "Coffee",
            "Pepper",
            "Salt"
    );

    public static List<String> storeNames = Arrays.asList(
            "BlueFarm",
            "Jack&Jakes",
            "Peterson' Farm",
            "Drued Farms",
            "Twin-pines & Co",
            "Lonely-pine & Co",
            "Tu/e",
            "Greenhouse productions"
    );
}

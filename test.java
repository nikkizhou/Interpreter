import java.util.ArrayList;
import java.util.HashMap;
public class test {
  public static void main(String[] args) {
    HashMap<Integer, String> hash_map = new HashMap<Integer, String>();

    
    hash_map.put(10, "Geeks");
    hash_map.put(15, "4");
    hash_map.put(20, "Geeks");
    hash_map.put(25, "Welcomes");
    hash_map.put(30, "You");

    // Displaying the HashMap
    System.out.println("Initial Mappings are: " + hash_map);

    // Displaying the size of the map
    System.out.println("The size of the map is " + hash_map.size());
    
  }
  
}

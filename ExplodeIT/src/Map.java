import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Map {
    private String name;
    private String color;
    private int id;
    private List<Block> blocks;

    public Map(int id, String name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.blocks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public int getId() {
        return id;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    //Fetch maps from the database
    public static List<Map> fetchMaps() {
        List<Map> maps = new ArrayList<>();
        try (Connection connection = DbConfig.connect()) {
            if (connection != null) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT MapID, name, color FROM map");
                while (resultSet.next()) {
                    int id = resultSet.getInt("MapID");
                    String name = resultSet.getString("name");
                    String color = resultSet.getString("color");
                    maps.add(new Map(id, name, color));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maps;
    }

    //Fetch blocks for the map
    public void loadBlocks() {
        try (Connection connection = DbConfig.connect()) {
            if (connection != null) {
                String query = "SELECT b.color, b.type, mb.positionX, mb.positionY " +
                        "FROM mapblock mb " +
                        "JOIN block b ON mb.BlockID = b.BlockID " +
                        "WHERE mb.MapID = " + this.id;
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    String color = resultSet.getString("color");
                    String type = resultSet.getString("type");
                    int positionX = resultSet.getInt("positionX");
                    int positionY = resultSet.getInt("positionY");

                    Block block = createBlockFromResultSet(type, color);
                    if (block != null) {
                        blocks.add(block);
                        block.setPosition(positionX, positionY);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Helper to create a Block instance from the type
    private Block createBlockFromResultSet(String type, String color) {
        switch (type) {
            case "LUCKY":
                return new LuckyBlock(color, true, 50, new ArrayList<>()); //Example lootProbability
            case "DESTRUCTIBLE":
                return new DestructibleBlock(color);
            case "INDESTRUCTIBLE":
                return new IndestructibleBlock(color);
            default:
                return null;
        }
    }
}

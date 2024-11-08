import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class MapExample {
    public static void main(String[] args) {
        /*// 创建一个 HashMap 实例
        Map<String, Integer> map = new HashMap<>();

        // 向 Map 中添加键值对
        map.put("Alice", 30);
        map.put("Bob", 25);
        map.put("Charlie", 35);

        // 打印 Map 内容
        System.out.println("Map: " + map);

        // 获取元素
        int age = map.get("Alice");
        System.out.println("Alice's age: " + age);

        // 检查是否包含键或值
        boolean hasBob = map.containsKey("Bob");
        boolean hasAge30 = map.containsValue(30);
        System.out.println("Map contains key 'Bob': " + hasBob);
        System.out.println("Map contains value 30: " + hasAge30);

        // 移除元素
        map.remove("Charlie");
        System.out.println("Map after removing 'Charlie': " + map);

        // 遍历 Map - 使用 entrySet
        System.out.println("Traversing using entrySet:");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }

        // 遍历 Map - 使用 keySet
        System.out.println("Traversing using keySet:");
        for (String key : map.keySet()) {
            System.out.println("Key: " + key + ", Value: " + map.get(key));
        }

        // 遍历 Map - 使用 values
        System.out.println("Traversing using values:");
        for (Integer value : map.values()) {
            System.out.println("Value: " + value);
        }*/
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Glue Example");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // 创建一个水平排列的 Box 容器
            Box horizontalBox = Box.createHorizontalBox();

            // 添加几个按钮到 Box 容器中
            horizontalBox.add(new JButton("Left"));
            horizontalBox.add(Box.createHorizontalGlue()); // 添加 Glue，填充剩余空间
            horizontalBox.add(new JButton("Right"));

            // 将 Box 容器添加到 JFrame 中
            frame.getContentPane().add(horizontalBox);

            // 设置 JFrame 大小并显示
            frame.setSize(300, 200);
            frame.setVisible(true);
        });
    }
}

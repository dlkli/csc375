import javax.swing.*;
import java.awt.*;

public class DrawFactory extends JPanel {

    private int size, height;
    private FactoryFloor factoryFloor;

    JFrame frame;

    public DrawFactory(int size, int height, FactoryFloor factoryFloor, JFrame frame) {
        this.size = size;
        this.height = height;
        this.factoryFloor = factoryFloor;
        this.frame = frame;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600,600);
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (factoryFloor.floor[x][y] != null) {
                    setBackground(Color.white);
                    graphics.setColor(factoryFloor.floor[x][y].color);
                    graphics.fillRect((x * height), (y * height), 600 / 6, height);
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect((x * height), (y * height), 600 / 6, height);
                }
            }
        }
    }
}

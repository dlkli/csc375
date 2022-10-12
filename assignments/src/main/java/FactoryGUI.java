import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FactoryGUI {
    JPanel jPanel = new JPanel();
    JFrame jFrame = new JFrame();

    JButton generateButton = new JButton("Generate Factory");

    JLabel factorySizeLabel = new JLabel("Size of Factory");
    JLabel numStationsLabel = new JLabel("Number of Stations");
    JLabel numThreadsLabel = new JLabel("Number of Threads");

    JTextField factorySizeField = new JTextField("");
    JTextField numStationsField = new JTextField("");
    JTextField numThreadsField = new JTextField("");

    GridLayout gridLayout = new GridLayout(4, 2, 5, 5);

    public FactoryGUI() {
        addComponentsToPane();
    }

    public void addComponentsToPane() {
        jPanel.setLayout(gridLayout);

        jPanel.add(factorySizeLabel);
        jPanel.add(factorySizeField);

        jPanel.add(numThreadsLabel);
        jPanel.add(numThreadsField);

        jPanel.add(numStationsLabel);
        jPanel.add(numStationsField);

        jPanel.add(generateButton);

        jFrame.add(jPanel, BorderLayout.CENTER);
        jFrame.setSize(600, 600);
        jFrame.setLayout(gridLayout);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jPanel.setVisible(false);

                GenerateFactory factory = new GenerateFactory(Integer.parseInt(factorySizeField.getText()),
                        Integer.parseInt(numStationsField.getText()),
                        Integer.parseInt(numThreadsField.getText()),
                        jFrame);
                factory.run();
            }
        });
    }

}
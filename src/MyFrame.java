import javax.swing.*;

public class MyFrame extends JFrame {

    private final JPanel drawPanel;

    public MyFrame(JPanel drawPanel){
        this.drawPanel = drawPanel;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Wave Function Collapse");
        setLocationRelativeTo(null);

        add(this.drawPanel);

        setVisible(true);

        pack();
    }

}

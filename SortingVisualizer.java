import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SortingVisualizer {
    private JPanel sortingPanel; // Custom panel for visualization
    private int[] data = {4, 2, 7, 1, 9, 5, 9, 2, 6};
    private int sortingSpeed = 100; // Array to be sorted

    private JPanel controlPanel; // Control panel for buttons
    private boolean isSorting = false; // To track if sorting is in progress
    private Map<Integer, Color> colorMap = new HashMap<>(); // Map for number-color associations

    public SortingVisualizer() {
        // Create a JFrame (main window)
        JFrame frame = new JFrame("Sorting Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle desktopBounds = ge.getMaximumWindowBounds();
        int screenWidth = desktopBounds.width;
        int screenHeight = desktopBounds.height;

        // Set the JFrame size to half of the desktop dimensions
        int windowWidth = screenWidth / 2;
        int windowHeight = screenHeight / 2;
        frame.setSize(windowWidth, windowHeight);
        frame.setLayout(new BorderLayout());

        // Create a panel for the heading and buttons, and add it to the left
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        frame.add(leftPanel, BorderLayout.LINE_START);

        // Heading Panel
        JPanel headingPanel = new JPanel();
        headingPanel.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));
        JLabel headingLabel = new JLabel("Sorting Visualizer");
        headingLabel.setFont(new Font("Times Roman", Font.BOLD, 40)); // Adjust font and size
        headingPanel.add(headingLabel);
        

        // Update the top insets to add padding from the top
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Updated top insets to 40px, other insets remain the same

        leftPanel.add(headingPanel, BorderLayout.NORTH);

        // Button Panel
        controlPanel = new JPanel(new GridBagLayout()); // Initialize control panel

        // Create GridBagConstraints for proper alignment
        gbc.anchor = GridBagConstraints.WEST;

        // Create four buttons
        JButton button1 = createButton("Button 1");
        JButton button2 = createButton("Button 2");
        JButton button3 = createButton("Button 3");
        JButton button4 = createButton("Button 4");

        // Add buttons to the control panel with proper alignment and padding
        gbc.gridx = 0;
        gbc.gridy = 0;
        controlPanel.add(button1, gbc);
        gbc.gridx = 1;
        controlPanel.add(button2, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        controlPanel.add(button3, gbc);
        gbc.gridx = 1;
        controlPanel.add(button4, gbc);

        leftPanel.add(controlPanel, BorderLayout.CENTER);

        sortingPanel = new SortingPanel();
        frame.add(sortingPanel, BorderLayout.CENTER);

        // Create a speed control panel
        JPanel speedControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton speedButton = createButton("Change Speed");

        speedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Action to perform when the speed button is clicked
                showSpeedControlDialog(speedButton);
            }
        });

        speedControlPanel.add(speedButton);
        frame.add(speedControlPanel, BorderLayout.SOUTH);

        // Make the frame fit its components
        frame.pack();
        // Center the frame on the desktop
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 75)); // Set a fixed button size

        // Create a custom border with rounded corners and thicker line
        button.setBorder(new LineBorder(Color.BLACK, 3, true)); // You can adjust the line thickness

        if (text.equals("Button 1")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isSorting) {
                        // Disable the button during sorting
                        button.setEnabled(false);

                        // Start the sorting process in a new thread
                        Thread sortingThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                isSorting = true;
                                BubbleSort.sort(data, sortingPanel, sortingSpeed); // Pass sortingSpeed
                                isSorting = false;
                                // Re-enable the button after sorting
                                button.setEnabled(true);
                            }
                        });
                        sortingThread.start();
                    }
                }
            });
        } else {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Button '" + text + "' was clicked!");
                }
            });
        }

        return button;
    }

    private void showSpeedControlDialog(Component parentComponent) {
        // Create a custom speed control dialog
        JDialog dialog = new JDialog();
        dialog.setTitle("Speed Control");

        // Create and add components to the dialog (radio buttons for speed options)
        JPanel dialogPanel = new JPanel();
        dialogPanel.add(new JLabel("Select Speed:"));

        JRadioButton radio2x = new JRadioButton("2x");
        JRadioButton radio4x = new JRadioButton("4x");

        // Create a button group to ensure that only one radio button is selected at a time
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radio2x);
        buttonGroup.add(radio4x);

        dialogPanel.add(radio2x);
        dialogPanel.add(radio4x);

        // Add action listeners to the radio buttons
        radio2x.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortingSpeed = 100; // Set sorting speed to the default value
            }
        });

        radio4x.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortingSpeed = 25; // Set sorting speed to 1/4th of the default value for faster sorting
            }
        });

        // Add the panel to the dialog
        dialog.add(dialogPanel);

        // Position the dialog above the side of the parent component
        dialog.pack();
        dialog.setLocation(parentComponent.getLocationOnScreen().x, parentComponent.getLocationOnScreen().y - dialog.getHeight());

        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SortingVisualizer();
            }
        });
    }

    public static class BubbleSort {
        public static void sort(int[] arr, JPanel panel, int sortingSpeed) {
            int n = arr.length;
            boolean swapped;
            for (int i = 0; i < n - 1; i++) {
                swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        // Swap arr[j] and arr[j+1]
                        int temp = arr[j];
                        arr[j] = arr[j + 1];
                        arr[j + 1] = temp;

                        // Update the display in the sorting panel
                        if (panel instanceof SortingPanel) {
                            ((SortingPanel) panel).setArray(arr);
                        }

                        // Sleep for a short period to visualize the sorting process
                        try {
                            Thread.sleep(sortingSpeed); // Use sortingSpeed
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        swapped = true;
                    }
                }

                if (!swapped) {
                    // If no two elements were swapped in this pass, the array is already sorted
                    break;
                }
            }
        }
    }

    // Define the custom JPanel for visualization
    private class SortingPanel extends JPanel {
        private int[] arrayToDisplay;

        public void setArray(int[] arr) {
            this.arrayToDisplay = arr;
            repaint(); // Trigger a repaint to update the display
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Calculate the padding for the top, left, right, and bottom
            int paddingTop = 20;
            int paddingLeft = 40;
            int paddingRight = 20;
            int paddingBottom = 20;

            // Define the rectangular area to draw the background, considering padding
            int rectX = paddingLeft;
            int rectY = paddingTop;
            int rectWidth = getWidth() - paddingLeft - paddingRight;
            int rectHeight = getHeight() - paddingTop - paddingBottom;

            // Draw a white background inside the rectangular box
            g.setColor(Color.WHITE);
            g.fillRect(rectX, rectY, rectWidth, rectHeight);

            if (arrayToDisplay != null) {
                int maxBarHeight = rectHeight - 40; // Adjust for padding
                int fontSize = 12;

                int numBars = arrayToDisplay.length;
                int barWidth = rectWidth / numBars;

                for (int i = 0; i < numBars; i++) {
                    int barHeight = arrayToDisplay[i] * maxBarHeight / Arrays.stream(arrayToDisplay).max().getAsInt();
                    int x = rectX + i * barWidth;
                    int y = rectY + (maxBarHeight - barHeight);

                    // Assign a unique color to each number
                    Color barColor = getColorForNumber(arrayToDisplay[i]);

                    // Draw the black outline of the bar
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, barWidth, barHeight);

                    // Fill the bar with its color
                    g.setColor(barColor);
                    g.fillRect(x, y, barWidth, barHeight);

                    // Draw the array number as a label
                    g.setColor(Color.BLACK);
                    g.setFont(new Font("Arial", Font.PLAIN, fontSize));
                    String text = String.valueOf(arrayToDisplay[i]);
                    FontMetrics metrics = g.getFontMetrics();
                    int labelX = x + (barWidth - metrics.stringWidth(text)) / 2;
                    int labelY = rectY + maxBarHeight + 15; // Adjust for position below the bar
                    g.drawString(text, labelX, labelY);
                }
            }
        }

        private Color getColorForNumber(int number) {
            // Check if a color is already assigned for the number
            if (colorMap.containsKey(number)) {
                return colorMap.get(number);
            } else {
                // Generate a random color for the number
                Color randomColor = new Color((int) (Math.random() * 0x1000000));
                colorMap.put(number, randomColor);
                return randomColor;
            }
        }
    }
}

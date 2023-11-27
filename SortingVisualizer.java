import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class SortingVisualizer {
    private SortingPanel sortingPanel;
    private int[] data = {4, 2, 7, 1, 9, 5, 9, 2, 6, 7, 5, 4, 3, 2};
    private int sortingSpeed = 400;
    private boolean isPaused = false;
    private JButton inputArrayButton;
    private JLabel algorithmLabel;
    private JButton floatingButton; // Declare the floating button
    private JFrame newWindowFrame;
    private JPanel controlPanel;
    private JTextArea algorithmStepsTextArea; 
    private boolean isSorting = false;
    private Map<Integer, Color> colorMap = new HashMap<>();
    private JLabel complexityLabel;

    public SortingVisualizer() {
        // Create a JFrame (main window)
        JFrame frame = new JFrame("Sorting Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle desktopBounds = ge.getMaximumWindowBounds();
        int screenWidth = desktopBounds.width;
        int screenHeight = desktopBounds.height;

        int windowWidth = screenWidth / 2;
        int windowHeight = screenHeight / 2;
        frame.setSize(windowWidth, windowHeight);
        frame.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        frame.add(leftPanel, BorderLayout.LINE_START);

        JPanel headingPanel = new JPanel();
        headingPanel.setBorder(BorderFactory.createEmptyBorder(60, 0, 0, 0));
        JLabel headingLabel = new JLabel("Sorting Visualizer");
        headingLabel.setFont(new Font("Times Roman", Font.BOLD, 40));
        headingPanel.add(headingLabel);

        leftPanel.add(headingPanel, BorderLayout.NORTH);

        controlPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(40, 10, 10, 10);

        JButton button1 = createButton("Bubble Sort");
        JButton button2 = createButton("Insertion Sort");
        JButton button3 = createButton("Merge Sort");
        JButton button4 = createButton("Quick Sort");

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

        JButton resetButton = createButton("Reset");
        gbc.gridx = 1;
        gbc.gridy = 2;
        controlPanel.add(resetButton, gbc);

        JButton pauseButton = createButton("Pause");
        gbc.gridx = 0;
        gbc.gridy = 2;
        controlPanel.add(pauseButton, gbc);
        inputArrayButton = createButton("Input Array");
        gbc.gridx = 0;
        gbc.gridy = 3;
        controlPanel.add(inputArrayButton, gbc);
        JButton openwindowButton = createButton("Algorithm Steps");
        gbc.gridx = 1;
        gbc.gridy = 3;
        controlPanel.add(openwindowButton, gbc);

        leftPanel.add(controlPanel, BorderLayout.CENTER);

        sortingPanel = new SortingPanel();
        frame.add(sortingPanel, BorderLayout.CENTER);

        JPanel speedControlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton speedButton = createButton("Change Speed");
        speedControlPanel.add(speedButton);
        frame.add(speedControlPanel, BorderLayout.NORTH);
        inputArrayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!isSorting) {
                    String input = JOptionPane.showInputDialog(frame, "Enter an array of integers (comma-separated):");
                    if (input != null) {
                        try {
                            String[] inputValues = input.split(",");
                            int[] newArray = new int[inputValues.length];
                            for (int i = 0; i < inputValues.length; i++) {
                                newArray[i] = Integer.parseInt(inputValues[i].trim());
                            }
                            data = newArray;
                            sortingPanel.setArray(data);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid array of integers.");
                        }
                    }
                }
            }
        });
        

        complexityLabel = new JLabel("Time Complexity: N/A | Space Complexity: N/A");
        complexityLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        complexityLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 10)); // Adjust top padding
        frame.add(complexityLabel, BorderLayout.EAST);

        algorithmLabel = new JLabel("Selected Algorithm: None", SwingConstants.CENTER);
        algorithmLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        Font largerFont = algorithmLabel.getFont().deriveFont(Font.PLAIN, 20);
        algorithmLabel.setFont(largerFont);
        frame.add(algorithmLabel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        algorithmStepsTextArea = new JTextArea(20, 30);
        algorithmStepsTextArea.setEditable(false);
    }


    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 75));
        button.setBorder(new LineBorder(Color.BLACK, 3, true));

        if (text.equals("Bubble Sort")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isSorting) {
                        button.setEnabled(false);
                        algorithmLabel.setText("Selected Algorithm: Bubble Sort");

                        Thread sortingThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                isSorting = true;
                                BubbleSort bubbleSort = new BubbleSort();
                                long startTime = System.nanoTime();

                                bubbleSort.sort(data, sortingPanel, sortingSpeed);

                                long endTime = System.nanoTime();

                                isSorting = false;
                                button.setEnabled(true);

                                long timeComplexity = endTime - startTime;
                                long spaceComplexity = data.length * 4;

                                complexityLabel.setText("Time Complexity: " + timeComplexity + " ns | Space Complexity: " + spaceComplexity + " bytes");
                            }
                        });
                        sortingThread.start();
                    }
                }
            });
        } else if (text.equals("Insertion Sort")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isSorting) {
                        button.setEnabled(false);
                        algorithmLabel.setText("Selected Algorithm: Insertion Sort");
                        Thread sortingThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                isSorting = true;
                                InsertionSort insertionSort = new InsertionSort();
                                long startTime = System.nanoTime();
                                insertionSort.sort(data, sortingPanel, sortingSpeed);
                                long endTime = System.nanoTime();
                                isSorting = false;
                                button.setEnabled(true);
                                long timeComplexity = endTime - startTime;
                                long spaceComplexity = data.length * 4;
                                complexityLabel.setText("Time Complexity: " + timeComplexity + " ns | Space Complexity: " + spaceComplexity + " bytes");
                            }
                        });
                        sortingThread.start();
                    }
                }
            });
        } else if (text.equals("Merge Sort")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isSorting) {
                        button.setEnabled(false);
                        algorithmLabel.setText("Selected Algorithm: Merge Sort");
                        Thread sortingThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                isSorting = true;
                                MergeSort mergeSort = new MergeSort();
                                long startTime = System.nanoTime();
                                mergeSort.sort(data, sortingPanel, sortingSpeed);
                                long endTime = System.nanoTime();
                                isSorting = false;
                                button.setEnabled(true);
                                long timeComplexity = endTime - startTime;
                                long spaceComplexity = data.length * 4;
                                complexityLabel.setText("Time Complexity: " + timeComplexity + " ns | Space Complexity: " + spaceComplexity + " bytes");
                            }
                        });
                        sortingThread.start();
                    }
                }
            });
        } else if (text.equals("Quick Sort")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isSorting) {
                        button.setEnabled(false);
                        algorithmLabel.setText("Selected Algorithm: Quick Sort");
                        Thread sortingThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                isSorting = true;
                                QuickSort quickSort = new QuickSort();
                                long startTime = System.nanoTime();
                                quickSort.sort(data, sortingPanel, sortingSpeed);
                                long endTime = System.nanoTime();
                                isSorting = false;
                                button.setEnabled(true);
                                long timeComplexity = endTime - startTime;
                                long spaceComplexity = data.length * 4;
                                complexityLabel.setText("Time Complexity: " + timeComplexity + " ns | Space Complexity: " + spaceComplexity + " bytes");
                            }
                        });
                        sortingThread.start();
                    }
                }
            });
        } else if (text.equals("Reset")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!isSorting) {
                        data = new int[]{4, 2, 7, 1, 9, 5, 9, 2, 6, 7, 5, 4, 3, 2};
                        colorMap.clear();
                        sortingPanel.setArray(data);
                        complexityLabel.setText("Time Complexity: N/A | Space Complexity: N/A");
                    }
                }
            });
        } else if (text.equals("Pause")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    isPaused = !isPaused;
                }
            });
        } else if (text.equals("Change Speed")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showSpeedControlDialog(button);
                }
            });
        }
        else if (text.equals("Algorithm Steps")) {
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Call the method to open a new window
                    openNewWindow();
                }
            });
        }
        return button;
    }
    // Add this method to create and display a new window
    private void openNewWindow() {
        // Create a new JFrame for the additional window
        newWindowFrame = new JFrame("New Window");
        newWindowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a new panel for displaying algorithm steps
        JPanel algorithmPanel = new JPanel();
        algorithmPanel.add(new JLabel("Algorithm Steps:"));
        JScrollPane scrollPane = new JScrollPane(algorithmStepsTextArea);
        algorithmPanel.add(scrollPane);

        newWindowFrame.add(algorithmPanel);
        newWindowFrame.setSize(400, 300);
        newWindowFrame.setLocationRelativeTo(null);
        newWindowFrame.setVisible(true);
    }
    private void updateAlgorithmSteps(String step) {
        SwingUtilities.invokeLater(() -> algorithmStepsTextArea.append(step + "\n"));
    }

    private void showSpeedControlDialog(Component parentComponent) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Speed Control");

        JPanel dialogPanel = new JPanel();
        dialogPanel.add(new JLabel("Select Speed:"));

        JRadioButton radio2x = new JRadioButton("2x");
        JRadioButton radio4x = new JRadioButton("4x");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radio2x);
        buttonGroup.add(radio4x);

        dialogPanel.add(radio2x);
        dialogPanel.add(radio4x);

        radio2x.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortingSpeed = 100;
            }
        });

        radio4x.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortingSpeed = 25;
            }
        });

        dialog.add(dialogPanel);

        dialog.pack();
        dialog.setLocation(parentComponent.getLocationOnScreen().x, parentComponent.getLocationOnScreen().y + parentComponent.getHeight());

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

    
//sorting Algorithms /////////////////////////////////////////////////////////////
public class BubbleSort {
    public void sort(int[] arr, SortingPanel panel, int sortingSpeed) {
        int n = arr.length;
        boolean swapped;
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;

                    panel.setArray(arr);
                    sleep(sortingSpeed);

                    // Update the algorithm steps
                    updateAlgorithmSteps("Comparing elements: " + arr[j] + " and " + arr[j + 1]);

                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }
    }

    private void sleep(int sortingSpeed) {
        try {
            while (isPaused) {
                Thread.sleep(100);
            }
            Thread.sleep(sortingSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
public class InsertionSort {
    public void sort(int[] arr, SortingPanel panel, int sortingSpeed) {
        int n = arr.length;
        for (int i = 1; i < n; ++i) {
            int key = arr[i];
            int j = i - 1;

            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j = j - 1;

                panel.setArray(arr);
                sleep(sortingSpeed);

                // Update the algorithm steps
                updateAlgorithmSteps("Moving element " + arr[j + 1] + " to the right");
            }
            arr[j + 1] = key;

            panel.setArray(arr);
            sleep(sortingSpeed);

            // Update the algorithm steps
            updateAlgorithmSteps("Placing element " + key + " in the correct position");
        }
    }

    private void sleep(int sortingSpeed) {
        try {
            while (isPaused) {
                Thread.sleep(100);
            }
            Thread.sleep(sortingSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class MergeSort {
    public void sort(int[] arr, SortingPanel panel, int sortingSpeed) {
        mergeSort(arr, 0, arr.length - 1, panel, sortingSpeed);
    }

    private void mergeSort(int[] arr, int l, int r, SortingPanel panel, int sortingSpeed) {
        if (l < r) {
            int m = (l + r) / 2;

            mergeSort(arr, l, m, panel, sortingSpeed);
            mergeSort(arr, m + 1, r, panel, sortingSpeed);

            merge(arr, l, m, r, panel, sortingSpeed);
        }
    }

    private void merge(int[] arr, int l, int m, int r, SortingPanel panel, int sortingSpeed) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int[] left = new int[n1];
        int[] right = new int[n2];

        for (int i = 0; i < n1; ++i) {
            left[i] = arr[l + i];
        }
        for (int j = 0; j < n2; ++j) {
            right[j] = arr[m + 1 + j];
        }
        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (left[i] <= right[j]) {
                arr[k] = left[i];
                i++;
            } else {
                arr[k] = right[j];
                j++;
            }
            k++;

            panel.setArray(arr);
            sleep(sortingSpeed);

            // Update the algorithm steps
            updateAlgorithmSteps("Merging elements from left and right");
        }
        while (i < n1) {
            arr[k] = left[i];
            i++;
            k++;

            panel.setArray(arr);
            sleep(sortingSpeed);

            // Update the algorithm steps
            updateAlgorithmSteps("Adding remaining elements from left");
        }
        while (j < n2) {
            arr[k] = right[j];
            j++;
            k++;

            panel.setArray(arr);
            sleep(sortingSpeed);

            // Update the algorithm steps
            updateAlgorithmSteps("Adding remaining elements from right");
        }
    }

    private void sleep(int sortingSpeed) {
        try {
            while (isPaused) {
                Thread.sleep(100);
            }
            Thread.sleep(sortingSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class QuickSort {
    public void sort(int[] arr, SortingPanel panel, int sortingSpeed) {
        quickSort(arr, 0, arr.length - 1, panel, sortingSpeed);
    }

    private void quickSort(int[] arr, int low, int high, SortingPanel panel, int sortingSpeed) {
        if (low < high) {
            int pi = partition(arr, low, high, panel, sortingSpeed);

            quickSort(arr, low, pi - 1, panel, sortingSpeed);
            quickSort(arr, pi + 1, high, panel, sortingSpeed);
        }
    }

    private int partition(int[] arr, int low, int high, SortingPanel panel, int sortingSpeed) {
        int pivot = arr[high];
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            if (arr[j] < pivot) {
                i++;

                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;

                panel.setArray(arr);
                sleep(sortingSpeed);

                // Update the algorithm steps
                updateAlgorithmSteps("Swapping elements: " + arr[i] + " and " + arr[j]);
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        panel.setArray(arr);
        sleep(sortingSpeed);

        // Update the algorithm steps
        updateAlgorithmSteps("Placing pivot " + pivot + " in the correct position");

        return i + 1;
    }

    private void sleep(int sortingSpeed) {
        try {
            while (isPaused) {
                Thread.sleep(100);
            }
            Thread.sleep(sortingSpeed);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            int fontSize = 18;

            int numBars = arrayToDisplay.length;
            int barWidth = rectWidth / numBars;

            for (int i = 0; i < numBars; i++) {
                int barHeight = arrayToDisplay[i] * maxBarHeight / Arrays.stream(arrayToDisplay).max().getAsInt();
                int x = rectX + i * barWidth;
                int y = rectY + (maxBarHeight - barHeight);
            
                // Assign a unique color to each number
                Color barColor = getColorForNumber(arrayToDisplay[i]);
            
                // Fill the bar with its color
                g.setColor(barColor);
                g.fillRect(x, y, barWidth, barHeight);
            
                // Draw the black outline of the bar
                g.setColor(Color.BLACK);
                g.drawRect(x, y, barWidth, barHeight);
            
                // Draw the array number as a label at the top of the bar
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, fontSize));
                String text = String.valueOf(arrayToDisplay[i]);
                FontMetrics metrics = g.getFontMetrics();
                int labelX = x + (barWidth - metrics.stringWidth(text)) / 2;
                int labelY = y - 5; // Adjust for position above the bar
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
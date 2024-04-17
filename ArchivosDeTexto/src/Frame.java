import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.Font;
import java.awt.Color;

public class Frame extends JFrame {
    private JMenuItem openFileMenuItem;
    private JMenuItem separateMenuItem; // Nuevo item de menú para Separar
    private JTextArea textAreaProcesado;
    private JTextField tFCadena;
    private JTextArea textAMessage; 

    public Frame() {
        initializeComponents();
    }

    private void initializeComponents() {
        setTitle("File Chooser Example");
        setSize(429, 431);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setForeground(Color.GRAY);
        
        // Menú Archivo
        JMenu fileMenu = new JMenu("File");
        openFileMenuItem = new JMenuItem("Open File");
        openFileMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFileMenuItemActionPerformed(e);
            }
        });
        fileMenu.add(openFileMenuItem);
        menuBar.add(fileMenu);
        
        // Menú Procesar
        JMenu processMenu = new JMenu("Procesar");
        separateMenuItem = new JMenuItem("Separar");
        separateMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                separate();
            }
        });
        processMenu.add(separateMenuItem);
        menuBar.add(processMenu);

        setJMenuBar(menuBar);
        
        getContentPane().setLayout(null);
        
        // JTextArea para mostrar el contenido del archivo
        textAreaProcesado = new JTextArea();
        textAreaProcesado.setEditable(false);
        textAreaProcesado.setLineWrap(true);
        textAreaProcesado.setWrapStyleWord(true);
        textAreaProcesado.setColumns(30);
        JScrollPane scrollPane = new JScrollPane(textAreaProcesado);
        scrollPane.setBounds(0, 0, 400, 256);
        getContentPane().add(scrollPane);

        // JTextField para ingresar texto manualmente
        tFCadena = new JTextField();
        tFCadena.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        tFCadena.setText("");
        scrollPane.setColumnHeaderView(tFCadena);

        // JTextArea para mostrar los resultados
        textAMessage = new JTextArea();
        textAMessage.setEditable(false);
        textAMessage.setBounds(getBounds());
        JScrollPane errorScrollPane = new JScrollPane(textAMessage); 
        errorScrollPane.setBounds(10, 281, 380, 69); 
        getContentPane().add(errorScrollPane);

        // Etiqueta para mostrar un mensaje
        JLabel lblNewLabel = new JLabel("Message:");
        lblNewLabel.setBounds(10, 259, 61, 16);
        getContentPane().add(lblNewLabel);
    }

    // Método para separar identificadores de variables y números
    private void separate() {
        // Obtener el texto del JTextArea
        String inputText = textAreaProcesado.getText(); 

        // Expresiones regulares para identificar variables, números y caracteres especiales
        String variablePattern = "[a-zA-Z_]\\w*"; // Variables, inician con letra o guion bajo, seguidas de letras, números o guion bajo
        String numberPattern = "\\d+"; // Números
        String specialCharacterPattern = "[^a-zA-Z0-9\\s]"; // Caracteres especiales (excluye letras, números y espacios)

        // Patrones para las expresiones regulares
        Pattern variableRegex = Pattern.compile(variablePattern);
        Pattern numberRegex = Pattern.compile(numberPattern);
        Pattern specialCharacterRegex = Pattern.compile(specialCharacterPattern);

        // Listas para almacenar variables, números y caracteres especiales
        List<String> variables = new ArrayList<>();
        List<String> numbers = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();

        // Buscar coincidencias de variables en el texto de entrada
        Matcher variableMatcher = variableRegex.matcher(inputText);
        while (variableMatcher.find()) {
            String variable = variableMatcher.group();
            variables.add(variable);
        }

        // Buscar coincidencias de números en el texto de entrada
        Matcher numberMatcher = numberRegex.matcher(inputText);
        while (numberMatcher.find()) {
            String number = numberMatcher.group();
            numbers.add(number);
        }

        // Buscar coincidencias de caracteres especiales en el texto de entrada
        Matcher specialCharacterMatcher = specialCharacterRegex.matcher(inputText);
        while (specialCharacterMatcher.find()) {
            String specialChar = specialCharacterMatcher.group();
            specialCharacters.add(specialChar);
        }

        // Mostrar los resultados en el textAMessage
        textAMessage.setText("Variables:\n");
        for (String variable : variables) {
            textAMessage.append(variable + "\n");
        }

        textAMessage.append("\nNúmeros:\n");
        for (String number : numbers) {
            textAMessage.append(number + "\n");
        }

        textAMessage.append("\nCaracteres especiales:\n");
        for (String specialChar : specialCharacters) {
            textAMessage.append(specialChar + "\n");
        }
    }
    // Método para abrir un archivo
    private void openFileMenuItemActionPerformed(ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String fileName = selectedFile.getName();
            String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
            
            // Verificar si la extensión del archivo es compatible con texto
            if ("txt".equalsIgnoreCase(fileExtension) || "rtf".equalsIgnoreCase(fileExtension)) {
                try {
                    FileReader reader = new FileReader(selectedFile);
                    BufferedReader bufferedReader = new BufferedReader(reader);

                    // Leer el contenido del archivo y mostrarlo en el JTextArea
                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line).append("\n");
                    }
                    bufferedReader.close();
                    reader.close();

                    textAreaProcesado.setText(content.toString());
                    tFCadena.setText(selectedFile.getName()); // Mostrar el nombre del archivo en el JTextField
                    
                    textAMessage.setText(""); // Limpiar el mensaje de error
                } catch (IOException ex) {
                    // Mostrar el error en el textAMessage
                    textAMessage.setText("Text Error: " + ex.getMessage());
                }
            } else {
                textAMessage.setText("This isn't a valid archive .");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Frame frame = new Frame();
                frame.setVisible(true);
            }
        });
    }
}
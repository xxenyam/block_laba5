import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Клас Block представляє блок
class Block {
    private final String data;
    public Block(String data) {
        this.data = data;
    }
    public String getData() {
        return data;
    }
}

// Клас Node представляє вузол
class Node {
    private final int id;
    private final List<Boolean> validations = new ArrayList<>();
    private boolean blockAdded = false;

    public Node(int id) {
        this.id = id;
    }

    // Метод для отримання id вузла
    public int getId() {
        return id;
    }

    // Метод для отримання блоку для валідації
    public void receiveBlock(Block block) {
        boolean isValid = validateBlock(block);
        System.out.println("Вузол " + id + " отримав блок для валідації: " + (isValid ? "Дійсний" : "Недійсний"));
        sendValidationResult(isValid);
    }

    // Метод для валідації блоку (випадковий результат)
    private boolean validateBlock(Block block) {
        return new Random().nextBoolean();
    }

    // Метод для надсилання результату валідації
    private void sendValidationResult(boolean isValid) {
        validations.add(isValid);
    }

    // Метод для перевірки, чи є консенсус
    public boolean hasConsensus() {
        long validCount = validations.stream().filter(Boolean::booleanValue).count();
        return validCount > validations.size() * 2 / 3;
    }

    // Метод для додавання блоку до блокчейну, якщо є консенсус
    public void addBlock(Block block) {
        if (hasConsensus()) {
            blockAdded = true;
        } else {
            blockAdded = false;
        }
    }

    // Метод для перевірки, чи додано блок
    public boolean isBlockAdded() {
        return blockAdded;
    }
}

public class BFT {

    // Метод для симуляції консенсусу серед вузлів
    public static long simulateConsensus(int numNodes) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            nodes.add(new Node(i)); // Створення вузлів
        }

        Block block = new Block("Дані транзакції"); // Створення блоку з даними транзакції
        long startTime = System.currentTimeMillis(); // Запис часу початку виконання

        // Відправка блоку всім вузлам для валідації
        for (Node node : nodes) {
            node.receiveBlock(block);
            try {
                Thread.sleep(100);  // Імітація затримки між відправкою блоків
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Перевірка консенсусу і додавання блоку до блокчейну
        for (Node node : nodes) {
            node.addBlock(block);
            // Виведення результату лише для додавання блоку
            System.out.println("Вузол " + node.getId() + (node.isBlockAdded() ? " додав блок до блокчейну." : " не додав блок: немає консенсусу."));
        }

        long endTime = System.currentTimeMillis(); // Запис часу завершення виконання
        System.out.println("Час виконання: " + (endTime - startTime) + " мс");
        return endTime - startTime; // Повернення часу виконання
    }

    // Метод для створення графіку виконання
    public static void createChart(int[] participants, long[] times) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Заповнення даних для графіка
        for (int i = 0; i < participants.length; i++) {
            dataset.addValue(times[i], "Час виконання (мс)", Integer.toString(participants[i]));
        }

        // Створення лінійного графіка
        JFreeChart chart = ChartFactory.createLineChart(
                "Залежність часу виконання протоколу від кількості учасників",
                "Кількість учасників",
                "Час (мс)",
                dataset,
                PlotOrientation.VERTICAL, // Ось Y вертикальна
                true, // Легенда
                true, // Підказки
                false // Збереження в файл
        );

        // Відображення графіка в новому вікні
        JFrame frame = new JFrame("Графік");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new ChartPanel(chart)); // Додавання панелі для графіка
        frame.pack();
        frame.setVisible(true); // Відображення вікна
    }

    // Головний метод програми
    public static void main(String[] args) {
        int[] participants = {5, 15, 30}; // Кількість учасників для тестування
        long[] times = new long[participants.length]; // Массив для зберігання часу виконання

        System.out.println("Запуск симуляції BFT-протоколу...");
        // Симуляція BFT для різної кількості учасників
        for (int i = 0; i < participants.length; i++) {
            System.out.println("Кількість учасників: " + participants[i]);
            times[i] = simulateConsensus(participants[i]); // Виконання симуляції
        }

        // Виведення результатів
        System.out.println("Час виконання симуляцій:");
        for (int i = 0; i < participants.length; i++) {
            System.out.println("Кількість учасників: " + participants[i] + ", Час виконання: " + times[i] + " мс");
        }

        // Створення графіка для результатів
        createChart(participants, times);
    }
}








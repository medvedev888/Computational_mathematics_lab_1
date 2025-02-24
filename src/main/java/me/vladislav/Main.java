package me.vladislav;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double[][] A;
        double[] b;
        double epsilon;

        System.out.println("Введите:");
        System.out.println("\t1 - Ввести матрицу с консоли");
        System.out.println("\t2 - Ввести матрицу с файла");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Для захвата оставшейся новой строки после выбора
        if (choice == 1) {
            System.out.println("Вводим матрицу с консоли");
            System.out.print("Введите размерность матрицы: ");
            int n = scanner.nextInt();
            A = new double[n][n];
            b = new double[n];

            System.out.println("Введите строки матрицы (каждая строка через пробел):");
            scanner.nextLine(); // Для захвата оставшейся новой строки после ввода размера матрицы
            for (int i = 0; i < n; i++) {
                // Вводим строку матрицы
                String row = scanner.nextLine();
                String[] values = row.split(" "); // Разделяем строку на части по пробелу

                // Заполняем строку матрицы и вектор b
                for (int j = 0; j < n; j++) {
                    A[i][j] = Double.parseDouble(values[j]);
                }
                b[i] = Double.parseDouble(values[n]); // Последний элемент для вектора b
            }

            System.out.print("Введите точность: ");
            epsilon = scanner.nextDouble();

        } else if (choice == 2) {
            System.out.print("Введите имя файла: ");
            String filename = scanner.next();
            try {
                Scanner fileScanner = new Scanner(new File(filename));
                int n = fileScanner.nextInt(); // Считываем размерность
                fileScanner.nextLine(); // Считываем оставшийся перевод строки

                A = new double[n][n];
                b = new double[n];

                for (int i = 0; i < n; i++) {
                    String row = fileScanner.nextLine().trim(); // Читаем строку, убираем лишние пробелы
                    String[] values = row.split("\\s+"); // Разделяем по пробелам или табуляции

                    for (int j = 0; j < n; j++) {
                        A[i][j] = Double.parseDouble(values[j]);
                    }
                    b[i] = Double.parseDouble(values[n]); // Последний элемент для вектора b
                }

                epsilon = Double.parseDouble(fileScanner.nextLine().trim()); // Считываем epsilon
                fileScanner.close();
            } catch (FileNotFoundException e) {
                System.out.println("Ошибка: файл не найден.");
                return;
            } catch (Exception e) {
                System.out.println("Ошибка чтения файла: " + e.getMessage());
                return;
            }
        } else {
            System.out.println("Ошибка: неверный выбор.");
            return;
        }

        // Вывод введенной матрицы
        System.out.println("Матрица:");
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[i].length; j++) {
                System.out.printf("%.2f ", A[i][j]);
            }
            System.out.printf("| %.2f%n", b[i]);
        }

        // Решаем систему
        SimpleIterationSolver solver = new SimpleIterationSolver(A, b, epsilon);
        double[] solution = solver.solve();

        // Вывод результата
        System.out.println("\nРешение системы:");
        for (int i = 0; i < solution.length; i++) {
            System.out.printf("[%d] = %.6f%n", i + 1, solution[i]);
        }

        // Вычисление вектора невязки
        double[] residual = solver.computeResidual();
        System.out.println("Вектор невязки:");
        for (int i = 0; i < residual.length; i++) {
            System.out.printf("[%d] = %.12f%n", i + 1, residual[i]);
        }

        scanner.close();
    }
}

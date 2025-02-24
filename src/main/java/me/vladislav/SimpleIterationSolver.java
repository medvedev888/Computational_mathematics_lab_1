package me.vladislav;

import java.util.Arrays;

public class SimpleIterationSolver {
    private final int n; // Размерность матрицы
    private final double[][] A; // Коэффициенты матрицы
    private final double[] b; // Вектор правой части
    private final double epsilon; // Точность
    private final double[] x; // Вектор решения

    public SimpleIterationSolver(double[][] A, double[] b, double epsilon) {
        this.n = A.length;
        this.A = A;
        this.b = b;
        this.epsilon = epsilon;
        this.x = new double[n]; // Начальное приближение (нулевой вектор)
    }

    public double[] solve() {
        // Проверка на диагональное преобладание
        if (!checkDiagonalDominance()) {
            System.out.println("Внимание: Матрица не обладает диагональным преобладанием. Выполняется перестановка строк.");
            applyRowSwaps();
        }

        double[] prevX = new double[n];
        int iteration = 0;

        // Вывод header-а
        StringBuilder header = new StringBuilder("№");
        for (int i = 0; i < n; i++) {
            header.append(" |x").append(i + 1).append("        ");
        }
        for (int i = 0; i < n; i++) {
            header.append(" | eps").append(i + 1).append("        ");
        }
        System.out.println(header);

        while (true) {
            System.arraycopy(x, 0, prevX, 0, n);

            for (int i = 0; i < n; i++) {
                double sum = 0;
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        sum += A[i][j] * prevX[j];
                    }
                }
                x[i] = (b[i] - sum) / A[i][i];
            }

            double[] eps = new double[n];
            for (int i = 0; i < n; i++) {
                eps[i] = Math.abs(x[i] - prevX[i]);
            }

            printIteration(iteration, x, eps);
            iteration++;

            if (Arrays.stream(eps).max().orElse(0) < epsilon) {
                break;
            }
        }

        return x;
    }

    private void printIteration(int iteration, double[] x, double[] eps) {
        System.out.printf("%-3d |", iteration);
        for (double xi : x) {
            System.out.printf("%10.6f   |", xi);
        }
        for (double e : eps) {
            System.out.printf("%10.6f   |", e);
        }
        System.out.println();
    }

    // Метод вычисления невязки
    public double[] computeResidual() {
        double[] residual = new double[n];
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                sum += A[i][j] * x[j];
            }
            residual[i] = sum - b[i];
        }
        return residual;
    }

    // Метод проверки диагонального преобладания
    public boolean checkDiagonalDominance() {
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    sum += Math.abs(A[i][j]);
                }
            }
            if (Math.abs(A[i][i]) <= sum) {
                return false;
            }
        }
        return true;
    }

    // Метод перестановки строк для обеспечения диагонального преобладания
    private void applyRowSwaps() {
        for (int i = 0; i < n; i++) {
            // Ищем строку с максимальным элементом в i-ом столбце
            int maxRow = i;
            for (int j = i + 1; j < n; j++) {
                if (Math.abs(A[j][i]) > Math.abs(A[maxRow][i])) {
                    maxRow = j;
                }
            }

            // Если максимальный элемент не на текущей строке, меняем строки местами
            if (maxRow != i) {
                double[] tempRow = A[i];
                A[i] = A[maxRow];
                A[maxRow] = tempRow;

                double temp = b[i];
                b[i] = b[maxRow];
                b[maxRow] = temp;
            }
        }
    }
}

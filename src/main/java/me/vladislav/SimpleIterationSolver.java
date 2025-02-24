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
        double[] prevX = new double[n];
        int iteration = 0;

        System.out.println("№  |x1         |x2         |x3      |  eps1        | eps2        | eps3       |");

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
}

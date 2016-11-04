/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs331_project1;

/**
 * Project1 for my CS 331 class. The purpose is to measure the runtimes of
 * several matrix multiplication algorithms.
 *
 * @author Gabriel Ortega-Gingrich
 */
public class Project1 {

   public static void performMultiplication(int nTimes, int mSize) {
      SqrMatrix mat1, mat2;
      int i;
      long time;

      mat1 = new SqrMatrix(mSize);
      mat2 = new SqrMatrix(mSize);

      System.out.println("\n");

      // strassen
      for (i = 0; i < nTimes; i++) {
         time = System.nanoTime();
         mat1.strassenMult(mat2);
         time = System.nanoTime() - time;
         System.out.println("Strassen: n = " + mSize + "     \ttime = "
                 + ((double) (time) / 1000000000));
      }

      // classic
      for (i = 0; i < nTimes; i++) {
         time = System.nanoTime();
         mat1.classicMult(mat2);
         time = System.nanoTime() - time;
         System.out.println("Classic: n = " + mSize + "     \ttime = "
                 + ((double) (time) / 1000000000));
      }

      // divide and conquer
      for (i = 0; i < nTimes; i++) {
         time = System.nanoTime();
         mat1.dandcMult(mat2);
         time = System.nanoTime() - time;
         System.out.println("DandC: n = " + mSize + "     \ttime = "
                 + ((double) (time) / 1000000000));
      }
   }

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args) {
        // TODO code application logic here

      for (int i = 2; i <= 4096; i *= 2) {
         performMultiplication(1, i);
      }
   }

}

class SqrMatrix {

   int[][] values;

   private final static int[][] zero = new int[4096][4096];

   /**
    * creates nxn matrix
    *
    * @param n
    */
   SqrMatrix(int n) {
      values = new int[n][n];
   }

   public int[][] classicMult(SqrMatrix mat) {
      int[][] result = new int[values.length][values.length];
      int sum, i, j, k;
      int n = values.length;

      for (i = 0; i < n; i++) {
         for (j = 0; j < n; j++) {
            sum = 0;

            for (k = 0; k < n; k++) {
               sum += values[i][k] * mat.values[k][j];
            }

            result[i][j] = sum;
         }
      }

      return result;
   }

   public int[][] dandcMult(SqrMatrix mat) {
      int[][] result = new int[values.length][values.length];

      dAndC(values.length, values, 0, 0, mat.values, 0, 0, result, 0, 0);

      return result;
   }

   public void dAndC(int n, int[][] in1, int a1, int b1, int[][] in2, int a2,
           int b2, int[][] out, int a3, int b3) {
      if (n == 2) {
         out[a3][b3] = in1[a1][b1] * in2[a2][b2] + in1[a1][b1 + 1]
                 * in2[a2 + 1][b2];
         out[a3][b3 + 1] = in1[a1][b1] * in2[a2][b2 + 1] + in1[a1][b1 + 1]
                 * in2[a2 + 1][b2 + 1];
         out[a3 + 1][b3] = in1[a1 + 1][b1] * in2[a2][b2] + in1[a1 + 1][b1 + 1]
                 * in2[a2 + 1][b2];
         out[a3 + 1][b3 + 1] = in1[a1 + 1][b1] * in2[a2][b2 + 1]
                 + in1[a1 + 1][b1 + 1] * in2[a2 + 1][b2 + 1];
      } else {
         n /= 2;
         int[][] temp1 = new int[n][n];
         int[][] temp2 = new int[n][n];

         dAndC(n, in1, a1, b1, in2, a2, b2, temp1, 0, 0);
         dAndC(n, in1, a1, b1 + n, in2, a2 + n, b2, temp2, 0, 0);
         add(n, temp1, 0, 0, temp2, 0, 0, out, a3, b3);

         dAndC(n, in1, a1, b1, in2, a2, b2 + n, temp1, 0, 0);
         dAndC(n, in1, a1, b1 + n, in2, a2 + n, b2 + n, temp2, 0, 0);
         add(n, temp1, 0, 0, temp2, 0, 0, out, a3, b3 + n);

         dAndC(n, in1, a1 + n, b1, in2, a2, b2, temp1, 0, 0);
         dAndC(n, in1, a1 + n, b1 + n, in2, a2 + n, b2, temp2, 0, 0);
         add(n, temp1, 0, 0, temp2, 0, 0, out, a3 + n, b3);

         dAndC(n, in1, a1 + n, b1, in2, a2, b2 + n, temp1, 0, 0);
         dAndC(n, in1, a1 + n, b1 + n, in2, a2 + n, b2 + n, temp2, 0, 0);
         add(n, temp1, 0, 0, temp2, 0, 0, out, a3 + n, b3 + n);
      }
   }

   public int[][] strassenMult(SqrMatrix mat) {
      int[][] result = new int[values.length][values.length];
      strassen(values.length, 0, 0, this.values, mat.values, result);

      return result;
   }

   private void strassen(int n, int a, int b, int[][] in1, int[][] in2,
           int[][] out) {
      int[][] p, q, r, s, t, u, v; // used in def of strassen's method
      int[][] tempA, tempB;

      if (n == 2) {
         out[a][b] = in1[a][b] * in2[a][b] + in1[a][b + 1] * in2[a + 1][b];
         out[a][b + 1] = in1[a][b] * in2[a][b + 1] + in1[a][b + 1]
                 * in2[a + 1][b + 1];
         out[a + 1][b] = in1[a + 1][b] * in2[a][b] + in1[a + 1][b + 1] 
                 * in2[a + 1][b + 1];
         out[a + 1][b + 1] = in1[a + 1][b] * in2[a][b + 1] + in1[a + 1][b + 1]
                 * in2[a + 1][b + 1];
      } else {
         n /= 2;

         p = new int[n][n];
         q = new int[n][n];
         r = new int[n][n];
         s = new int[n][n];
         t = new int[n][n];
         u = new int[n][n];
         v = new int[n][n];
         tempA = new int[n][n];
         tempB = new int[n][n];

         // first recurssive call
         add(n, in1, a, b, in1, a + n, b + n, tempA, 0, 0);
         add(n, in2, a, b, in2, a + n, b + n, tempB, 0, 0);
         strassen(n, 0, 0, tempA, tempB, p);

         // second recurssive call
         add(n, in1, a + n, b, in1, a + n, b + n, tempA, 0, 0);
         strassen(n, 0, 0, tempA, in2, q);

         // third recurssive call
         sub(n, in2, a, b + n, in2, a + n, b + n, tempA, 0, 0);
         strassen(n, 0, 0, in1, tempA, r);

         // fourth recurssive call
         sub(n, in2, a + n, b + n, in2, a, b, tempA, 0, 0);
         strassen(n, 0, 0, in1, tempA, s);

         // fifth recurssive call
         add(n, in1, a, b, in1, a, b + n, tempA, 0, 0);
         add(n, in2, a + n, b + n, zero, 0, 0, tempB, 0, 0);
         strassen(n, 0, 0, tempA, tempB, t);

         // sixth recurssive call
         sub(n, in1, a + n, b, in1, a, b, tempA, 0, 0);
         add(n, in2, a, b, in2, a, b + n, tempB, 0, 0);
         strassen(n, 0, 0, tempA, tempB, u);

         // seventh recurssive call
         sub(n, in1, a, b + n, in1, a + n, b + n, tempA, 0, 0);
         add(n, in2, a + n, b, in2, a + n, b + n, tempB, 0, 0);
         strassen(n, 0, 0, tempA, tempB, v);

         // set C11
         add(n, p, 0, 0, s, 0, 0, zero, 0, 0);
         add(n, zero, 0, 0, v, 0, 0, zero, 0, 0);
         sub(n, zero, 0, 0, t, 0, 0, out, 0, 0);

         // set c12
         add(n, r, 0, 0, t, 0, 0, out, 0, n);

         // set c21
         add(n, q, 0, 0, s, 0, 0, out, n, 0);

         // set c22
         add(n, p, 0, 0, r, 0, 0, zero, n, n);
         sub(n, zero, n, n, q, 0, 0, zero, n, n);
         add(n, u, 0, 0, zero, n, n, out, n, n);
      }
   }

   private void add(int n, int[][] in1, int a1, int b1, int[][] in2, int a2,
           int b2, int[][] out, int a, int b) {
      int i, j;

      for (i = 0; i < n; i++) {
         for (j = 0; j < n; j++) {
            out[a + i][b + j] = in1[a1 + i][b1 + j] + in2[a2 + i][b2 + j];
         }
      }
   }

   private void sub(int n, int[][] in1, int a1, int b1, int[][] in2, int a2,
           int b2, int[][] out, int a, int b) {
      int i, j;

      for (i = 0; i < n; i++) {
         for (j = 0; j < n; j++) {
            out[a + i][b + j] = in1[a1 + i][b1 + j] - in2[a2 + i][b2 + j];
         }
      }
   }
}

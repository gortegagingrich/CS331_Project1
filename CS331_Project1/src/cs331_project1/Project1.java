/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs331_project1;

import java.util.Random;

/**
 *
 * @author Gabriel
 */
public class Project1 {

    public static void performMultiplication(int nTimes, int mSize) {
	SqrMatrix mat1, mat2;
        int i;

	for (i = 0; i < nTimes; i++) {
	    mat1 = new SqrMatrix(mSize);
	    mat2 = new SqrMatrix(mSize);

	    printResult(mat1.classicMult(mat2));
            System.out.println();
            printResult(mat1.strassenMult(mat2));
            System.out.println("\n");
	}
    }

    public static void printResult(int[][] result) {
	for (int[] list: result) {
	    System.out.print("[\t");
	    
	    for (int i: list) {
		System.out.print(i + "\t");
	    }

	    System.out.println("\t]");
	}
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        performMultiplication(2,2);
    }
    
}

class SqrMatrix {
    int[][] values;
    Random rand;
    
    /**
     * creates nxn matrix
     * @param n 
     */
    SqrMatrix(int n) {
        values = new int[n][n];
        rand = new Random();
        
        fillValues();
    }
    
    private void fillValues() {
        for (int[] row: values) {
            for (int i = 0; i < row.length; i++) {
                row[i] = rand.nextInt();
            }
        }
    }
    
    public String toString() {
        String out = "";
        
        for (int[] row: values) {
            out += "[\t";
            for (int val: row) {
                out += Integer.toString(val) + "\t";
            }
            
            out += "]\n";
        }
        
        return out;
    }
    
    public int[][] classicMult(SqrMatrix mat) {
        int[][] result = new int[values.length][values.length];
        int sum, i, j, k;
        
        for (i = 0; i < values.length; i++) {
            for (j = 0; j < values.length; j++) {
                sum = 0;
                
                for (k = 0; k < values.length; k++) {
                    sum += values[i][k] * mat.values[k][j];
                }
                
                result[i][j] = sum;
            }
        }
        
        return result;
    }

    public int[][] dandcMult(SqrMatrix mat) {
	int[][] result = new int[values.length][values.length];

	return result;
    }

    public int[][] strassenMult(SqrMatrix mat) {
	int[][] result = new int[values.length][values.length];
        strassen(2,0,0,this.values,mat.values,result);

	return result;
    }
    
    private void strassen(int n, int a, int b, int[][] in1, int[][] in2, int[][] out) {
        int[][] p, q, r, s, t, u, v; // used in def of strassen's method
        
        if (n == 2) {
            out[a][b] = in1[a][b] * in2[a][b] + in1[a][b+1] * in2[a+1][b];
            out[a][b+1] = in1[a][b] * in2[a][b+1] + in1[a][b+1] * in2[a+1][b+1];
            out[a+1][b] = in1[a+1][b] * in2[a][b] + in1[a+1][b+1] * in2[a+1][b+1];
            out[a+1][b+1] = in1[a+1][b] * in2[a][b+1] + in1[a+1][b+1] * in2[a+1][b+1];
        } else {
            n /= 2;
            
            int[][] temp1 = new int[n][n];
            int[][] temp2 = new int[n][n];
            
            p = new int[n][n];
            q = new int[n][n];
            r = new int[n][n];
            s = new int[n][n];
            t = new int[n][n];
            u = new int[n][n];
            v = new int[n][n];
            
            add(n, in1, a, b, in1, a+n, b+n, temp1, 0, 0);
            add(n, in2, a, b, in2, a+n, b+n, temp2, 0, 0);
            strassen(n, 0, 0, temp1, temp2, p);
            
            add(n, in1, a + n, b, in1, a+n, b+n, temp1, 0, 0);
            strassen(n, 0, 0, temp1, in2, q);
            
            sub(n, in2, a, b+n, in2, a+n, b+n, temp1, 0, 0);
            strassen(n,0,0,in1,temp1,r);
            
            sub(n, in2, a+n, b+n, in2, a, b, temp1, 0, 0);
            strassen(n,0,0,in1,temp1,s);
        }
    }
    
    private void add(int n, int[][] in1, int a1, int b1, int[][] in2, int a2, int b2, int[][] out, int a, int b) {
        int i, j;
        
        for (i = 0; i < n; i++) {
            for(j = 0; j < n; j++) {
                out[a+i][b+j] = in1[a1+i][b1+j] + in2[a2+i][b2+j];
            }
        }
    }
    private void sub(int n, int[][] in1, int a1, int b1, int[][] in2, int a2, int b2, int[][] out, int a, int b) {
        int i, j;
        
        for (i = 0; i < n; i++) {
            for(j = 0; j < n; j++) {
                out[a+i][b+j] = in1[a1+i][b1+j] - in2[a2+i][b2+j];
            }
        }
    }
}

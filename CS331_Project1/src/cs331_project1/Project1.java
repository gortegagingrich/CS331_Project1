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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        SqrMatrix mat1 = new SqrMatrix(3);
        SqrMatrix mat2 = new SqrMatrix(3);
        
        int[][] result = mat1.classicMult(mat2);
        
        System.out.println(mat1 + "\n" + mat2);
        
        for (int[] list: result) {
            for (int i: list) {
                System.out.print(i + "\t");
            }
            System.out.println();
        }
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
        int sum;
        
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values.length; j++) {
                sum = 0;
                
                for (int k = 0; k < values.length; k++) {
                    sum += values[i][k] * mat.values[k][j];
                }
                
                result[i][j] = sum;
            }
        }
        
        return result;
    }
}

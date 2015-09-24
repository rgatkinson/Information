package com.qualcomm.robotcore.util;

import android.util.Log;
import java.lang.reflect.Array;

public class MatrixD
{
    protected int mCols;
    protected double[][] mData;
    protected int mRows;
    
    public MatrixD(final int n, final int n2) {
        this((double[][])Array.newInstance(Double.TYPE, n, n2));
    }
    
    public MatrixD(final double[] array, final int n, final int n2) {
        this(n, n2);
        if (array == null) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with null array");
        }
        if (array.length != n * n2) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with rows/cols not matching init data");
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                this.mData[i][j] = array[j + n2 * i];
            }
        }
    }
    
    public MatrixD(final float[] array, final int n, final int n2) {
        this(n, n2);
        if (array == null) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with null array");
        }
        if (array.length != n * n2) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with rows/cols not matching init data");
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                this.mData[i][j] = array[j + n2 * i];
            }
        }
    }
    
    public MatrixD(final double[][] mData) {
        int i = 0;
        this.mData = mData;
        if (this.mData == null) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with null array");
        }
        this.mRows = this.mData.length;
        if (this.mRows <= 0) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with 0 rows");
        }
        this.mCols = this.mData[0].length;
        while (i < this.mRows) {
            if (this.mData[i].length != this.mCols) {
                throw new IllegalArgumentException("Attempted to initialize MatrixF with rows of unequal length");
            }
            ++i;
        }
    }
    
    public static void test() {
        Log.e("MatrixD", "Hello2 matrix");
        final MatrixD matrixD = new MatrixD(new double[][] { { 1.0, 0.0, -2.0 }, { 0.0, 3.0, -1.0 } });
        Log.e("MatrixD", "Hello3 matrix");
        Log.e("MatrixD", "A = \n" + matrixD.toString());
        final MatrixD matrixD2 = new MatrixD(new double[][] { { 0.0, 3.0 }, { -2.0, -1.0 }, { 0.0, 4.0 } });
        Log.e("MatrixD", "B = \n" + matrixD2.toString());
        Log.e("MatrixD", "A transpose = " + matrixD.transpose().toString());
        Log.e("MatrixD", "B transpose = " + matrixD2.transpose().toString());
        Log.e("MatrixD", "AB = \n" + matrixD.times(matrixD2).toString());
        final MatrixD times = matrixD2.times(matrixD);
        Log.e("MatrixD", "BA = \n" + times.toString());
        Log.e("MatrixD", "BA*2 = " + times.times(2.0).toString());
        Log.e("MatrixD", "BA submatrix 3,2,0,1 = " + times.submatrix(3, 2, 0, 1));
        Log.e("MatrixD", "BA submatrix 2,1,1,2 = " + times.submatrix(2, 1, 1, 2));
    }
    
    public MatrixD add(final double n) {
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, this.numRows(), this.numCols());
        final int numRows = this.numRows();
        final int numCols = this.numCols();
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                array[i][j] = n + this.data()[i][j];
            }
        }
        return new MatrixD(array);
    }
    
    public MatrixD add(final MatrixD matrixD) {
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, this.numRows(), this.numCols());
        final int numRows = this.numRows();
        final int numCols = this.numCols();
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                array[i][j] = this.data()[i][j] + matrixD.data()[i][j];
            }
        }
        return new MatrixD(array);
    }
    
    public double[][] data() {
        return this.mData;
    }
    
    public double length() {
        if (this.numRows() != 1 && this.numCols() != 1) {
            throw new IndexOutOfBoundsException("Not a 1D matrix ( " + this.numRows() + ", " + this.numCols() + " )");
        }
        double n = 0.0;
        for (int i = 0; i < this.numRows(); ++i) {
            double n2;
            for (int j = 0; j < this.numCols(); ++j, n = n2) {
                n2 = n + this.mData[i][j] * this.mData[i][j];
            }
        }
        return Math.sqrt(n);
    }
    
    public int numCols() {
        return this.mCols;
    }
    
    public int numRows() {
        return this.mRows;
    }
    
    public boolean setSubmatrix(final MatrixD matrixD, final int n, final int n2, final int n3, final int n4) {
        if (matrixD == null) {
            throw new IllegalArgumentException("Input data to setSubMatrix null");
        }
        if (n > this.numRows() || n2 > this.numCols()) {
            throw new IllegalArgumentException("Attempted to get submatrix with size larger than original");
        }
        if (n3 + n > this.numRows() || n4 + n2 > this.numCols()) {
            throw new IllegalArgumentException("Attempted to access out of bounds data with row or col offset out of range");
        }
        if (n > matrixD.numRows() || n2 > matrixD.numCols()) {
            throw new IllegalArgumentException("Input matrix small for setSubMatrix");
        }
        if (n3 + n > matrixD.numRows() || n4 + n2 > this.numCols()) {
            throw new IllegalArgumentException("Input matrix Attempted to access out of bounds data with row or col offset out of range");
        }
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                this.data()[n3 + i][n4 + j] = matrixD.data()[i][j];
            }
        }
        return true;
    }
    
    public MatrixD submatrix(final int n, final int n2, final int n3, final int n4) {
        if (n > this.numRows() || n2 > this.numCols()) {
            throw new IllegalArgumentException("Attempted to get submatrix with size larger than original");
        }
        if (n3 + n > this.numRows() || n4 + n2 > this.numCols()) {
            throw new IllegalArgumentException("Attempted to access out of bounds data with row or col offset out of range");
        }
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, n, n2);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                array[i][j] = this.data()[n3 + i][n4 + j];
            }
        }
        return new MatrixD(array);
    }
    
    public MatrixD subtract(final double n) {
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, this.numRows(), this.numCols());
        final int numRows = this.numRows();
        final int numCols = this.numCols();
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                array[i][j] = this.data()[i][j] - n;
            }
        }
        return new MatrixD(array);
    }
    
    public MatrixD subtract(final MatrixD matrixD) {
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, this.numRows(), this.numCols());
        final int numRows = this.numRows();
        final int numCols = this.numCols();
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols; ++j) {
                array[i][j] = this.data()[i][j] - matrixD.data()[i][j];
            }
        }
        return new MatrixD(array);
    }
    
    public MatrixD times(final double n) {
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, this.numRows(), this.numCols());
        for (int i = 0; i < this.numRows(); ++i) {
            for (int j = 0; j < this.numCols(); ++j) {
                array[i][j] = n * this.data()[i][j];
            }
        }
        return new MatrixD(array);
    }
    
    public MatrixD times(final MatrixD matrixD) {
        if (this.numCols() != matrixD.numRows()) {
            throw new IllegalArgumentException("Attempted to multiply matrices of invalid dimensions (AB) where A is " + this.numRows() + "x" + this.numCols() + ", B is " + matrixD.numRows() + "x" + matrixD.numCols());
        }
        final int numCols = this.numCols();
        final int numRows = this.numRows();
        final int numCols2 = matrixD.numCols();
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, numRows, numCols2);
        for (int i = 0; i < numRows; ++i) {
            for (int j = 0; j < numCols2; ++j) {
                for (int k = 0; k < numCols; ++k) {
                    final double[] array2 = array[i];
                    array2[j] += this.data()[i][k] * matrixD.data()[k][j];
                }
            }
        }
        return new MatrixD(array);
    }
    
    @Override
    public String toString() {
        String s = new String();
        for (int i = 0; i < this.numRows(); ++i) {
            String s2 = new String();
            for (int j = 0; j < this.numCols(); ++j) {
                s2 += String.format("%.4f", this.data()[i][j]);
                if (j < -1 + this.numCols()) {
                    s2 += ", ";
                }
            }
            s += s2;
            if (i < -1 + this.numRows()) {
                s += "\n";
            }
        }
        return s + "\n";
    }
    
    public MatrixD transpose() {
        final int mRows = this.mRows;
        final int mCols = this.mCols;
        final double[][] array = (double[][])Array.newInstance(Double.TYPE, mCols, mRows);
        for (int i = 0; i < mCols; ++i) {
            for (int j = 0; j < mRows; ++j) {
                array[i][j] = this.mData[j][i];
            }
        }
        return new MatrixD(array);
    }
}

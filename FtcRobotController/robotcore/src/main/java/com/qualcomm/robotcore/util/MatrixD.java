package com.qualcomm.robotcore.util;

import android.util.Log;

import java.lang.reflect.Array;

public class MatrixD {
    protected int mCols;
    protected double[][] mData;
    protected int mRows;

    public MatrixD(int var1, int var2) {
        this((double[][]) Array.newInstance(Double.TYPE, var1, var2));
    }

    public MatrixD(double[] var1, int var2, int var3) {
        this(var2, var3);
        if (var1 == null) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with null array");
        } else if (var1.length != var2 * var3) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with rows/cols not matching init data");
        } else {
            for (int var4 = 0; var4 < var2; ++var4) {
                for (int var5 = 0; var5 < var3; ++var5) {
                    this.mData[var4][var5] = var1[var5 + var3 * var4];
                }
            }

        }
    }

    public MatrixD(float[] var1, int var2, int var3) {
        this(var2, var3);
        if (var1 == null) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with null array");
        } else if (var1.length != var2 * var3) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with rows/cols not matching init data");
        } else {
            for (int var4 = 0; var4 < var2; ++var4) {
                for (int var5 = 0; var5 < var3; ++var5) {
                    this.mData[var4][var5] = (double) var1[var5 + var3 * var4];
                }
            }

        }
    }

    public MatrixD(double[][] var1) {
        super();
        int var2 = 0;

        this.mData = var1;
        if (this.mData == null) {
            throw new IllegalArgumentException("Attempted to initialize MatrixF with null array");
        } else {
            this.mRows = this.mData.length;
            if (this.mRows <= 0) {
                throw new IllegalArgumentException("Attempted to initialize MatrixF with 0 rows");
            } else {
                for (this.mCols = this.mData[0].length; var2 < this.mRows; ++var2) {
                    if (this.mData[var2].length != this.mCols) {
                        throw new IllegalArgumentException("Attempted to initialize MatrixF with rows of unequal length");
                    }
                }

            }
        }
    }

    public static void test() {
        Log.e("MatrixD", "Hello2 matrix");
        MatrixD var1 = new MatrixD(new double[][]{{1.0D, 0.0D, -2.0D}, {0.0D, 3.0D, -1.0D}});
        Log.e("MatrixD", "Hello3 matrix");
        Log.e("MatrixD", "A = \n" + var1.toString());
        MatrixD var4 = new MatrixD(new double[][]{{0.0D, 3.0D}, {-2.0D, -1.0D}, {0.0D, 4.0D}});
        Log.e("MatrixD", "B = \n" + var4.toString());
        MatrixD var6 = var1.transpose();
        Log.e("MatrixD", "A transpose = " + var6.toString());
        MatrixD var8 = var4.transpose();
        Log.e("MatrixD", "B transpose = " + var8.toString());
        MatrixD var10 = var1.times(var4);
        Log.e("MatrixD", "AB = \n" + var10.toString());
        MatrixD var12 = var4.times(var1);
        Log.e("MatrixD", "BA = \n" + var12.toString());
        MatrixD var14 = var12.times(2.0D);
        Log.e("MatrixD", "BA*2 = " + var14.toString());
        MatrixD var16 = var12.submatrix(3, 2, 0, 1);
        Log.e("MatrixD", "BA submatrix 3,2,0,1 = " + var16);
        MatrixD var18 = var12.submatrix(2, 1, 1, 2);
        Log.e("MatrixD", "BA submatrix 2,1,1,2 = " + var18);
    }

    public MatrixD add(double var1) {
        int[] var3 = new int[]{this.numRows(), this.numCols()};
        double[][] var4 = (double[][]) Array.newInstance(Double.TYPE, var3);
        int var5 = this.numRows();
        int var6 = this.numCols();

        for (int var7 = 0; var7 < var5; ++var7) {
            for (int var8 = 0; var8 < var6; ++var8) {
                var4[var7][var8] = var1 + this.data()[var7][var8];
            }
        }

        return new MatrixD(var4);
    }

    public MatrixD add(MatrixD var1) {
        int[] var2 = new int[]{this.numRows(), this.numCols()};
        double[][] var3 = (double[][]) Array.newInstance(Double.TYPE, var2);
        int var4 = this.numRows();
        int var5 = this.numCols();

        for (int var6 = 0; var6 < var4; ++var6) {
            for (int var7 = 0; var7 < var5; ++var7) {
                var3[var6][var7] = this.data()[var6][var7] + var1.data()[var6][var7];
            }
        }

        return new MatrixD(var3);
    }

    public double[][] data() {
        return this.mData;
    }

    public double length() {
        if (this.numRows() != 1 && this.numCols() != 1) {
            throw new IndexOutOfBoundsException("Not a 1D matrix ( " + this.numRows() + ", " + this.numCols() + " )");
        } else {
            double var1 = 0.0D;

            double var5;
            for (int var3 = 0; var3 < this.numRows(); ++var3) {
                for (int var4 = 0; var4 < this.numCols(); var1 = var5) {
                    var5 = var1 + this.mData[var3][var4] * this.mData[var3][var4];
                    ++var4;
                }
            }

            return Math.sqrt(var1);
        }
    }

    public int numCols() {
        return this.mCols;
    }

    public int numRows() {
        return this.mRows;
    }

    public boolean setSubmatrix(MatrixD var1, int var2, int var3, int var4, int var5) {
        if (var1 == null) {
            throw new IllegalArgumentException("Input data to setSubMatrix null");
        } else if (var2 <= this.numRows() && var3 <= this.numCols()) {
            if (var4 + var2 <= this.numRows() && var5 + var3 <= this.numCols()) {
                if (var2 <= var1.numRows() && var3 <= var1.numCols()) {
                    if (var4 + var2 <= var1.numRows() && var5 + var3 <= this.numCols()) {
                        for (int var6 = 0; var6 < var2; ++var6) {
                            for (int var7 = 0; var7 < var3; ++var7) {
                                this.data()[var4 + var6][var5 + var7] = var1.data()[var6][var7];
                            }
                        }

                        return true;
                    } else {
                        throw new IllegalArgumentException("Input matrix Attempted to access out of bounds data with row or col offset out of range");
                    }
                } else {
                    throw new IllegalArgumentException("Input matrix small for setSubMatrix");
                }
            } else {
                throw new IllegalArgumentException("Attempted to access out of bounds data with row or col offset out of range");
            }
        } else {
            throw new IllegalArgumentException("Attempted to get submatrix with size larger than original");
        }
    }

    public MatrixD submatrix(int var1, int var2, int var3, int var4) {
        if (var1 <= this.numRows() && var2 <= this.numCols()) {
            if (var3 + var1 <= this.numRows() && var4 + var2 <= this.numCols()) {
                int[] var5 = new int[]{var1, var2};
                double[][] var6 = (double[][]) Array.newInstance(Double.TYPE, var5);

                for (int var7 = 0; var7 < var1; ++var7) {
                    for (int var8 = 0; var8 < var2; ++var8) {
                        var6[var7][var8] = this.data()[var3 + var7][var4 + var8];
                    }
                }

                return new MatrixD(var6);
            } else {
                throw new IllegalArgumentException("Attempted to access out of bounds data with row or col offset out of range");
            }
        } else {
            throw new IllegalArgumentException("Attempted to get submatrix with size larger than original");
        }
    }

    public MatrixD subtract(double var1) {
        int[] var3 = new int[]{this.numRows(), this.numCols()};
        double[][] var4 = (double[][]) Array.newInstance(Double.TYPE, var3);
        int var5 = this.numRows();
        int var6 = this.numCols();

        for (int var7 = 0; var7 < var5; ++var7) {
            for (int var8 = 0; var8 < var6; ++var8) {
                var4[var7][var8] = this.data()[var7][var8] - var1;
            }
        }

        return new MatrixD(var4);
    }

    public MatrixD subtract(MatrixD var1) {
        int[] var2 = new int[]{this.numRows(), this.numCols()};
        double[][] var3 = (double[][]) Array.newInstance(Double.TYPE, var2);
        int var4 = this.numRows();
        int var5 = this.numCols();

        for (int var6 = 0; var6 < var4; ++var6) {
            for (int var7 = 0; var7 < var5; ++var7) {
                var3[var6][var7] = this.data()[var6][var7] - var1.data()[var6][var7];
            }
        }

        return new MatrixD(var3);
    }

    public MatrixD times(double var1) {
        int[] var3 = new int[]{this.numRows(), this.numCols()};
        double[][] var4 = (double[][]) Array.newInstance(Double.TYPE, var3);

        for (int var5 = 0; var5 < this.numRows(); ++var5) {
            for (int var6 = 0; var6 < this.numCols(); ++var6) {
                var4[var5][var6] = var1 * this.data()[var5][var6];
            }
        }

        return new MatrixD(var4);
    }

    public MatrixD times(MatrixD var1) {
        if (this.numCols() != var1.numRows()) {
            throw new IllegalArgumentException("Attempted to multiply matrices of invalid dimensions (AB) where A is " + this.numRows() + "x" + this.numCols() + ", B is " + var1.numRows() + "x" + var1.numCols());
        } else {
            int var2 = this.numCols();
            int var3 = this.numRows();
            int var4 = var1.numCols();
            int[] var5 = new int[]{var3, var4};
            double[][] var6 = (double[][]) Array.newInstance(Double.TYPE, var5);

            for (int var7 = 0; var7 < var3; ++var7) {
                for (int var8 = 0; var8 < var4; ++var8) {
                    for (int var9 = 0; var9 < var2; ++var9) {
                        double[] var10 = var6[var7];
                        var10[var8] += this.data()[var7][var9] * var1.data()[var9][var8];
                    }
                }
            }

            return new MatrixD(var6);
        }
    }

    public String toString() {
        String var1 = new String();

        for (int var2 = 0; var2 < this.numRows(); ++var2) {
            String var3 = new String();

            for (int var4 = 0; var4 < this.numCols(); ++var4) {
                StringBuilder var5 = (new StringBuilder()).append(var3);
                Object[] var6 = new Object[]{Double.valueOf(this.data()[var2][var4])};
                var3 = var5.append(String.format("%.4f", var6)).toString();
                if (var4 < -1 + this.numCols()) {
                    var3 = var3 + ", ";
                }
            }

            var1 = var1 + var3;
            if (var2 < -1 + this.numRows()) {
                var1 = var1 + "\n";
            }
        }

        return var1 + "\n";
    }

    public MatrixD transpose() {
        int var1 = this.mRows;
        int var2 = this.mCols;
        int[] var3 = new int[]{var2, var1};
        double[][] var4 = (double[][]) Array.newInstance(Double.TYPE, var3);

        for (int var5 = 0; var5 < var2; ++var5) {
            for (int var6 = 0; var6 < var1; ++var6) {
                var4[var5][var6] = this.mData[var6][var5];
            }
        }

        return new MatrixD(var4);
    }
}

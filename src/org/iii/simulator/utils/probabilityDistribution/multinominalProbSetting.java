package org.iii.simulator.utils.probabilityDistribution;

import org.iii.simulator.utils.probabilityDistribution.distrib.MatrixLib;

public class multinominalProbSetting implements MatrixLib {

    private int gx;
    private int gy;
    private double[][] gval;
    @Override
    public double elementAt(int x, int y) {
        return gval[x][y];
    }

    @Override
    public void setElement(int x, int y, double[] val) {
        gx = val.length;
        gy = y;
        gval = new double[x][y];
        for (int i = 0; i < val.length; i++){
            gval[i][y-1] = val[i];
        }
    }

    @Override
    public String toString() {
        return null;
    }

    @Override
    public int numRows() {
        return gx;
    }

    @Override
    public int numCols() {
        return gy;
    }

    @Override
    public MatrixLib sliceRow(int i) {
        return null;
    }

    @Override
    public MatrixLib sliceRows(int i, int j) {
        return null;
    }

    @Override
    public MatrixLib sliceCol(int i) {
        return null;
    }

    @Override
    public MatrixLib sliceCols(int i, int j) {
        return null;
    }

    @Override
    public MatrixLib subMat(int x1, int y1, int x2, int y2) {
        return null;
    }

    @Override
    public MatrixLib plus(MatrixLib otherMat) {
        return null;
    }

    @Override
    public MatrixLib minus(MatrixLib otherMat) {
        return null;
    }

    @Override
    public MatrixLib timesScale(double scale) {
        return null;
    }

    @Override
    public MatrixLib timesMat(MatrixLib otherMat) {
        return null;
    }

    @Override
    public double det() {
        return 0;
    }

    @Override
    public double trace() {
        return 0;
    }

    @Override
    public MatrixLib abs() {
        return null;
    }

    @Override
    public MatrixLib exp() {
        return null;
    }

    @Override
    public MatrixLib log() {
        return null;
    }

    @Override
    public double logDet() {
        return 0;
    }

    @Override
    public MatrixLib transpose() {
        return null;
    }

    @Override
    public MatrixLib repmat(int rowTimes, int colTimes) {
        return null;
    }

    @Override
    public MatrixLib inverse() {
        return null;
    }

    @Override
    public MatrixLib choleskyFactor() {
        return null;
    }

    @Override
    public MatrixLib columnSum() {
        return null;
    }

    @Override
    public MatrixLib rowSum() {
        return null;
    }

    @Override
    public double matSum() {
        return 0;
    }

    @Override
    public double[] eigenvals() {
        return new double[0];
    }

    @Override
    public boolean isSymmetric() {
        return false;
    }
}

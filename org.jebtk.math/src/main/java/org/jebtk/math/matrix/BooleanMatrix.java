/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jebtk.math.matrix;

import java.util.Arrays;
import java.util.List;

import org.jebtk.core.sys.SysUtils;

/**
 * Matrix for storing integers only.
 *
 * @author Antony Holmes Holmes
 */
public class BooleanMatrix extends IndexMatrix {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member data.
   */
  public final int[] mData;
  
  /**
   * The offsets in the array where each new row begins.
   */
  public final int[] mRowOffsets;

  /**
   * Instantiates a new numerical matrix.
   *
   * @param rows the rows
   * @param columns the columns
   */
  public BooleanMatrix(int rows, int columns) {
    super(rows, columns);

    mRowOffsets = new int[mDim.mRows];
    
    // We use a 1d array to store a 2d matrix for speed.
    mData = new int[mSize / 8 + 1];
  }
  
  private void createOffsets() {
    // Cache the offsets to improve lookup times

    mRowOffsets[0] = 0;

    for (int i = 1; i < mDim.mRows; ++i) {
      // Use only additions
      mRowOffsets[i] = mRowOffsets[i - 1] + mDim.mCols; // i * columns;
    }
  }

  /**
   * Create a new matrix and initialize all cells to a common value.
   *
   * @param rows the rows
   * @param columns the columns
   * @param v the v
   */
  public BooleanMatrix(int rows, int columns, boolean v) {
    this(rows, columns);

    // Set the default value
    update(v);
  }

  /**
   * Clone a matrix.
   *
   * @param m the m
   */
  public BooleanMatrix(Matrix m) {
    this(m.getRows(), m.getCols());

    update(m);
  }

  /**
   * Instantiates a new int matrix.
   *
   * @param m the m
   */
  public BooleanMatrix(IndexRowMatrix m) {
    this(m.getRows(), m.getCols());

    update(m);
  }

  /**
   * Instantiates a new int matrix.
   *
   * @param m the m
   */
  public BooleanMatrix(BooleanMatrix m) {
    this(m.getRows(), m.getCols());

    update(m);
  }

  /**
   * Sets the.
   *
   * @param m the m
   */
  public void set(BooleanMatrix m) {
    update(m);

    fireMatrixChanged();
  }
  
  @Override
  public void update(Matrix m) {
    if (m instanceof BooleanMatrix) {
      update((BooleanMatrix) m);
    } else {
      super.update(m);
    }
  }

  /**
   * Update.
   *
   * @param m the m
   */
  public void update(BooleanMatrix m) {
    SysUtils.arraycopy(m.mData, mData);
  }
  
  @Override
  public int getIndex(int row, int column) {
    return mRowOffsets[row] + column;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.math.matrix.Matrix#copy()
   */
  @Override
  public Matrix copy() {
    return new BooleanMatrix(this);
  }

  @Override
  public Matrix ofSameType(int rows, int cols) {
    return createBooleanMatrix(rows, cols);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.math.matrix.IndexMatrix#get(int)
   */
  @Override
  public Object get(int index) {
    return getInt(index);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.math.matrix.IndexMatrix#getValue(int)
   */
  @Override
  public double getValue(int index) {
    return getInt(index);
  }

  @Override
  public long getLong(int index) {
    return getInt(index);
  }

  @Override
  public int getInt(int index) {
    return getBit(index);
  }
  
  public boolean getBool(int index) {
    return getBit(index) == 1;
  }
  
  /**
   * Returns the 1 bit value representing a boolean in the matrix.
   * 
   * @param index
   * @return
   */
  private int getBit(int index) {
    int bin = index / 8;
    
    int shift = 7 - (index % 8);
    
    return (mData[bin] >> shift) & 1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.math.matrix.Matrix#update(double)
   */
  @Override
  public void update(double v) {
    int i;

    if (isValidMatrixNum(v)) {
      i = (int) v;
    } else {
      i = 0;
    }

    update(i);
  }

  @Override
  public void update(long v) {
    update((int) v);
  }

  @Override
  public void update(int v) {
    Arrays.fill(mData, v);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.math.matrix.IndexMatrix#update(int, double)
   */
  @Override
  public void update(int index, double v) {
    int i;

    if (isValidMatrixNum(v)) {
      i = (int) v;
    } else {
      i = 0;
    }

    mData[index] = i;
  }

  @Override
  public void update(int index, int v) {
    mData[index] = v;
  }

  @Override
  public void update(int index, long v) {
    mData[index] = (int) v;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.math.matrix.IndexMatrix#rowAsDouble(int)
   */
  @Override
  public void rowToIntArray(int row, int[] ret) {
    SysUtils.arraycopy(mData, getIndex(row, 0), ret, mDim.mCols);
  }

  @Override
  public void columnToIntArray(int column, int[] ret) {
    SysUtils.arraycopy(mData, column, mDim.mCols, ret, mDim.mRows);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.math.matrix.IndexMatrix#getText(int)
   */
  @Override
  public String getText(int index) {
    return Integer.toString(mData[index]);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.math.matrix.Matrix#setValueColumn(int, java.util.List)
   */
  @Override
  public void setValueColumn(int column, List<Double> values) {
    int ix = column;

    for (int i = 0; i < mDim.mRows; ++i) {
      mData[ix] = values.get(i).intValue();

      ix += mDim.mCols;
    }
  }

  @Override
  public void setColumn(int column, double[] values) {
    int ix = column;

    for (int i = 0; i < mDim.mRows; ++i) {
      mData[ix] = (int) values[i];

      ix += mDim.mCols;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.math.matrix.Matrix#copyColumn(org.abh.common.math.matrix.
   * DoubleMatrix, int, int)
   */
  @Override
  public void copyColumn(final DoubleMatrix from, int column, int toColumn) {
    // if (from.getRowCount() == 0 || getRowCount() == 0) {
    // return;
    // }

    int i1 = from.getIndex(0, column);
    int i2 = getIndex(0, toColumn);

    int r = Math.min(from.getRows(), getRows());

    for (int i = 0; i < r; ++i) {
      mData[i2] = (int) from.mData[i1];

      i1 += from.mDim.mCols;
      i2 += mDim.mCols;
    }

    fireMatrixChanged();
  }

  /**
   * Specialized instance of column copying for numerical matrices.
   *
   * @param from the from
   * @param column the column
   * @param toColumn the to column
   */
  public void copyColumn(final BooleanMatrix from, int column, int toColumn) {
    // if (from.getRowCount() == 0 || getRowCount() == 0) {
    // return;
    // }

    int i1 = from.getIndex(0, column);
    int i2 = getIndex(0, toColumn);

    int r = Math.min(from.getRows(), getRows());

    for (int i = 0; i < r; ++i) {
      mData[i2] = from.mData[i1];

      i1 += from.mDim.mCols;
      i2 += mDim.mCols;
    }

    fireMatrixChanged();
  }

  /**
   * Copy row.
   *
   * @param from the from
   * @param row the row
   * @param toRow the to row
   */
  public void copyRow(final BooleanMatrix from, int row, int toRow) {

    int c = Math.min(from.getCols(), getCols());

    System.arraycopy(from.mData,
        from.mRowOffsets[row],
        mData,
        mRowOffsets[toRow],
        c);

    fireMatrixChanged();
  }

  @Override
  public void rowApply(CellFunction f, int index) {
    int offset = mRowOffsets[index];

    for (int i = 0; i < mDim.mCols; ++i) {
      mData[offset] = (int) f.f(i, 0, mData[offset]);

      ++offset;
    }

    fireMatrixChanged();
  }

  @Override
  public void colApply(CellFunction f, int index) {
    int offset = index;

    for (int i = 0; i < mDim.mCols; ++i) {
      mData[offset] = (int) f.f(i, 0, mData[offset]);

      offset += mDim.mCols;
    }

    fireMatrixChanged();
  }

  @Override
  public double stat(MatrixStatFunction f) {
    f.init();

    for (int i = 0; i < mData.length; ++i) {
      f.f(i, 0, mData[i]);
    }

    return f.getStat();
  }

  @Override
  public double rowStat(MatrixStatFunction f, int index) {
    f.init();

    int offset = mRowOffsets[index];

    for (int i = 0; i < mDim.mCols; ++i) {
      f.f(i, 0, mData[offset]);

      ++offset;
    }

    return f.getStat();
  }

  @Override
  public double colStat(MatrixStatFunction f, int index) {
    int offset = index;

    for (int i = 0; i < mDim.mCols; ++i) {
      f.f(i, 0, mData[offset]);

      offset += mDim.mCols;
    }

    return f.getStat();
  }

  @Override
  public Matrix multiply(final Matrix m) {
    if (m instanceof BooleanMatrix) {
      return multiply(this, (BooleanMatrix) m);
    } else {
      return super.multiply(m);
    }
  }

  public static Matrix multiply(final BooleanMatrix m1, final BooleanMatrix m2) {
    BooleanMatrix ret = ofSameType(m1);

    int of1 = 0;

    int r = m1.mDim.mRows;
    int c = m1.mDim.mCols;

    for (int i = 0; i < r; ++i) {
      int ix = of1;

      for (int j = 0; i < c; ++j) {
        int ix1 = of1;

        int ix2 = j;

        for (int k = 0; i < c; ++k) {
          ret.mData[ix] += m1.mData[ix1] * m2.mData[ix2];

          ++ix1;
          ix2 += c;
        }

        ++ix;
      }

      of1 += c;
    }

    return ret;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.math.matrix.IndexMatrix#transpose()
   */
  @Override
  public Matrix transpose() {
    return transpose(this);
  }

  public static Matrix transpose(final BooleanMatrix m) {
    BooleanMatrix ret = createBooleanMatrix(m.mDim.mCols, m.mDim.mRows);

    int i2 = 0;
    int c = 0;

    for (int i = 0; i < m.mData.length; ++i) {
      // Each time we end a row, reset i2 back to the next column
      if (i % m.mDim.mCols == 0) {
        i2 = c++;
      }

      ret.mData[i2] = m.mData[i];

      // Skip blocks
      i2 += m.mDim.mRows;
    }

    return ret;
  }

  @Override
  public void toIntArray(int[] ret) {
    SysUtils.arraycopy(mData, ret);
  }

  //
  // Static methods
  //

  /**
   * Returns a new empty matrix the same dimensions as the input matrix.
   *
   * @param m the m
   * @return the int matrix
   */
  public static BooleanMatrix createBooleanMatrix(Matrix m) {
    return createBooleanMatrix(m.getRows(), m.getCols());
  }

  /**
   * Creates the int matrix.
   *
   * @param rows the rows
   * @param cols the cols
   * @return the int matrix
   */
  public static BooleanMatrix createBooleanMatrix(int rows, int cols) {
    return new BooleanMatrix(rows, cols);
  }

  /**
   * Return an empty double matrix of the same dimension as the matrix argument.
   * 
   * @param m
   * @return
   */
  public static BooleanMatrix ofSameType(final BooleanMatrix m) {
    return new BooleanMatrix(m);
  }
}

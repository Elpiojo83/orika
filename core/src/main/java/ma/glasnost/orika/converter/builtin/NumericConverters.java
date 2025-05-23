/*
 * Orika - simpler, better and faster Java bean mapping
 *
 *  Copyright (C) 2011-2019 Orika authors
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package ma.glasnost.orika.converter.builtin;

import java.math.BigDecimal;
import java.math.BigInteger;

import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.metadata.Type;

/**
 * NumericConverters contains a set of common conversions between the "big" value types in the
 * java.math package (BigDecimal and BigInteger) and their related primitive wrapper types.
 *
 * @auhtor elaatifi@gmail.com
 */
public class NumericConverters {

  /**
   * Provides conversion between BigDecimal and Double.<br>
   * <br>
   * <strong>Note:</strong> please consider the typical warnings regarding loss of precision when
   * converting from BigDecimal to Double.
   *
   * @see BigDecimal#doubleValue()
   */
  public static class BigDecimalToDoubleConverter
      extends BuiltinBidirectionalConverter<BigDecimal, Double> {

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Double convertTo(
        BigDecimal source, Type<Double> destinationType, MappingContext context) {
      return source.doubleValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public BigDecimal convertFrom(
        Double source, Type<BigDecimal> destinationType, MappingContext context) {
      return BigDecimal.valueOf(source.doubleValue());
    }
  }

  /**
   * Provides conversion between BigDecimal and Float.<br>
   * <br>
   * <strong>Note:</strong> please consider the typical warnings regarding loss of precision when
   * converting from BigDecimal to Float.
   *
   * @see BigDecimal#floatValue()
   */
  public static class BigDecimalToFloatConverter
      extends BuiltinBidirectionalConverter<BigDecimal, Float> {

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Float convertTo(BigDecimal source, Type<Float> destinationType, MappingContext context) {
      return source.floatValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public BigDecimal convertFrom(
        Float source, Type<BigDecimal> destinationType, MappingContext context) {
      return BigDecimal.valueOf(source.doubleValue());
    }
  }

  /**
   * Provides conversion between BigInteger and Long.
   *
   * @see BigInteger
   */
  public static class BigIntegerToLongConverter
      extends BuiltinBidirectionalConverter<BigInteger, Long> {

    private static final BigInteger MAX_LONG = BigInteger.valueOf(Long.MAX_VALUE);
    private static final BigInteger MIN_LONG = BigInteger.valueOf(Long.MIN_VALUE);
    private final boolean truncate;

    /**
     * Constructs a new BigIntegerToLongConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by java.lang.Long
     */
    public BigIntegerToLongConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Long convertTo(BigInteger source, Type<Long> destinationType, MappingContext context) {
      if (!truncate && (source.compareTo(MAX_LONG) > 0 || source.compareTo(MIN_LONG) < 0)) {
        throw new ArithmeticException(
            "Overflow: " + source + " cannot be represented by " + Long.class.getCanonicalName());
      }
      return source.longValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public BigInteger convertFrom(
        Long source, Type<BigInteger> destinationType, MappingContext context) {
      return BigInteger.valueOf(source.longValue());
    }
  }

  /** Provides conversion between BigInteger and Integer. */
  public static class BigIntegerToIntegerConverter
      extends BuiltinBidirectionalConverter<BigInteger, Integer> {

    private static final BigInteger MAX_INT = BigInteger.valueOf(Integer.MAX_VALUE);
    private static final BigInteger MIN_INT = BigInteger.valueOf(Integer.MIN_VALUE);

    private final boolean truncate;

    /**
     * Constructs a new BigIntegerToIntegerConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by java.lang.Integer
     */
    public BigIntegerToIntegerConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Integer convertTo(
        BigInteger source, Type<Integer> destinationType, MappingContext context) {
      if (!truncate && (source.compareTo(MAX_INT) > 0 || source.compareTo(MIN_INT) < 0)) {
        throw new ArithmeticException(
            "Overflow: "
                + source
                + " cannot be represented by "
                + Integer.class.getCanonicalName());
      }
      return source.intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public BigInteger convertFrom(
        Integer source, Type<BigInteger> destinationType, MappingContext context) {
      return BigInteger.valueOf(source.longValue());
    }
  }

  /** Provides conversion between Integer and Short. */
  public static class IntegerToShortConverter
      extends BuiltinBidirectionalConverter<Integer, Short> {

    private final boolean truncate;

    /**
     * Constructs a new IntegerToShortConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by the smaller of the two types
     */
    public IntegerToShortConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Short convertTo(Integer source, Type<Short> destinationType, MappingContext context) {
      if (!truncate
          && (source.compareTo((int) Short.MAX_VALUE) > 0
              || source.compareTo((int) Short.MIN_VALUE) < 0)) {
        throw new ArithmeticException(
            "Overflow: " + source + " cannot be represented by " + Short.class.getCanonicalName());
      }
      return source.shortValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Integer convertFrom(
        Short source, Type<Integer> destinationType, MappingContext context) {
      return source.intValue();
    }
  }

  /** Provides conversion between Long and Short. */
  public static class LongToShortConverter extends BuiltinBidirectionalConverter<Long, Short> {

    private final boolean truncate;

    /**
     * Constructs a new LongToShortConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by the smaller of the two types
     */
    public LongToShortConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Short convertTo(Long source, Type<Short> destinationType, MappingContext context) {
      if (!truncate
          && (source.compareTo((long) Short.MAX_VALUE) > 0
              || source.compareTo((long) Short.MIN_VALUE) < 0)) {
        throw new ArithmeticException(
            "Overflow: " + source + " cannot be represented by " + Short.class.getCanonicalName());
      }
      return source.shortValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Long convertFrom(Short source, Type<Long> destinationType, MappingContext context) {
      return source.longValue();
    }
  }

  /** Provides conversion between Long and Integer. */
  public static class LongToIntegerConverter extends BuiltinBidirectionalConverter<Long, Integer> {

    private final boolean truncate;

    /**
     * Constructs a new LongToIntegerConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by the smaller of the two types
     */
    public LongToIntegerConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Integer convertTo(Long source, Type<Integer> destinationType, MappingContext context) {
      if (!truncate
          && (source.compareTo((long) Integer.MAX_VALUE) > 0
              || source.compareTo((long) Integer.MIN_VALUE) < 0)) {
        throw new ArithmeticException(
            "Overflow: "
                + source
                + " cannot be represented by "
                + Integer.class.getCanonicalName());
      }
      return source.intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Long convertFrom(Integer source, Type<Long> destinationType, MappingContext context) {
      return source.longValue();
    }
  }

  /** Provides conversion between Long and Integer. */
  public static class DoubleToLongConverter extends BuiltinBidirectionalConverter<Double, Long> {

    private final boolean truncate;

    /**
     * Constructs a new LongToIntegerConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by the smaller of the two types
     */
    public DoubleToLongConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Long convertTo(Double source, Type<Long> destinationType, MappingContext context) {
      if (!truncate
          && (source.compareTo((double) Long.MAX_VALUE) > 0
              || source.compareTo((double) Long.MIN_VALUE) < 0)) {
        throw new ArithmeticException(
            "Overflow: " + source + " cannot be represented by " + Long.class.getCanonicalName());
      }
      return source.longValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Double convertFrom(Long source, Type<Double> destinationType, MappingContext context) {
      return source.doubleValue();
    }
  }

  /** Provides conversion between Integer and Integer. */
  public static class DoubleToIntegerConverter
      extends BuiltinBidirectionalConverter<Double, Integer> {

    private final boolean truncate;

    /**
     * Constructs a new IntegerToIntegerConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by the smaller of the two types
     */
    public DoubleToIntegerConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Integer convertTo(Double source, Type<Integer> destinationType, MappingContext context) {
      if (!truncate
          && (source.compareTo((double) Integer.MAX_VALUE) > 0
              || source.compareTo((double) Integer.MIN_VALUE) < 0)) {
        throw new ArithmeticException(
            "Overflow: "
                + source
                + " cannot be represented by "
                + Integer.class.getCanonicalName());
      }
      return source.intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Double convertFrom(
        Integer source, Type<Double> destinationType, MappingContext context) {
      return source.doubleValue();
    }
  }

  /** Provides conversion between Short and Short. */
  public static class DoubleToShortConverter extends BuiltinBidirectionalConverter<Double, Short> {

    private final boolean truncate;

    /**
     * Constructs a new ShortToShortConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by the smaller of the two types
     */
    public DoubleToShortConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Short convertTo(Double source, Type<Short> destinationType, MappingContext context) {
      if (!truncate
          && (source.compareTo((double) Short.MAX_VALUE) > 0
              || source.compareTo((double) Short.MIN_VALUE) < 0)) {
        throw new ArithmeticException(
            "Overflow: " + source + " cannot be represented by " + Short.class.getCanonicalName());
      }
      return source.shortValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Double convertFrom(Short source, Type<Double> destinationType, MappingContext context) {
      return source.doubleValue();
    }
  }

  // ~

  /** Provides conversion between Long and Integer. */
  public static class FloatToLongConverter extends BuiltinBidirectionalConverter<Float, Long> {

    private final boolean truncate;

    /**
     * Constructs a new LongToIntegerConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by the smaller of the two types
     */
    public FloatToLongConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Long convertTo(Float source, Type<Long> destinationType, MappingContext context) {
      if (!truncate
          && (source.compareTo((float) Long.MAX_VALUE) > 0
              || source.compareTo((float) Long.MIN_VALUE) < 0)) {
        throw new ArithmeticException(
            "Overflow: " + source + " cannot be represented by " + Long.class.getCanonicalName());
      }
      return source.longValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Float convertFrom(Long source, Type<Float> destinationType, MappingContext context) {
      return source.floatValue();
    }
  }

  /** Provides conversion between Integer and Integer. */
  public static class FloatToIntegerConverter
      extends BuiltinBidirectionalConverter<Float, Integer> {

    private final boolean truncate;

    /**
     * Constructs a new IntegerToIntegerConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by the smaller of the two types
     */
    public FloatToIntegerConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Integer convertTo(Float source, Type<Integer> destinationType, MappingContext context) {
      if (!truncate
          && (source.compareTo((float) Integer.MAX_VALUE) > 0
              || source.compareTo((float) Integer.MIN_VALUE) < 0)) {
        throw new ArithmeticException(
            "Overflow: "
                + source
                + " cannot be represented by "
                + Integer.class.getCanonicalName());
      }
      return source.intValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Float convertFrom(Integer source, Type<Float> destinationType, MappingContext context) {
      return source.floatValue();
    }
  }

  /** Provides conversion between Short and Short. */
  public static class FloatToShortConverter extends BuiltinBidirectionalConverter<Float, Short> {

    private final boolean truncate;

    /**
     * Constructs a new ShortToShortConverter, with the configured truncation behavior.
     *
     * @param truncate specifies whether the converter should perform truncation; if false, an
     *     ArithmeticException is thrown for a value which is too large or too small to be
     *     accurately represented by the smaller of the two types
     */
    public FloatToShortConverter(boolean truncate) {
      this.truncate = truncate;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertTo
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Short convertTo(Float source, Type<Short> destinationType, MappingContext context) {
      if (!truncate
          && (source.compareTo((float) Short.MAX_VALUE) > 0
              || source.compareTo((float) Short.MIN_VALUE) < 0)) {
        throw new ArithmeticException(
            "Overflow: " + source + " cannot be represented by " + Short.class.getCanonicalName());
      }
      return source.shortValue();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ma.glasnost.orika.converter.BuiltinBidirectionalConverter#convertFrom
     * (java.lang.Object, ma.glasnost.orika.metadata.Type)
     */
    @Override
    public Float convertFrom(Short source, Type<Float> destinationType, MappingContext context) {
      return source.floatValue();
    }
  }
}

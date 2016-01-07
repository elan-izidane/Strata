/**
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.calc.runner;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.market.ScenarioValuesList;
import com.opengamma.strata.calc.ColumnDefinition;
import com.opengamma.strata.calc.config.Measure;
import com.opengamma.strata.collect.ArgChecker;
import com.opengamma.strata.collect.Messages;
import com.opengamma.strata.collect.result.Result;

/**
 * Results of performing calculations for a set of targets over a set of scenarios.
 */
@BeanDefinition
public final class Results implements ImmutableBean {

  /** The number of rows in the results. */
  @PropertyDefinition
  private final int rowCount;

  /** The number of columns in the results. */
  @PropertyDefinition
  private final int columnCount;

  /** Column indices keyed by column definition. */
  @PropertyDefinition(validate = "notNull")
  private final ImmutableMap<ColumnDefinition<?, ?>, Integer> columnsByDefinition;

  /**
   * Column indices by measure.
   * <p>
   * Measure is not necessarily unique so this data is only useful for the common case where there is only
   * one column for each measure.
   */
  private final ImmutableMap<Measure, Integer> columnsByMeasure;


  /**
   * The results, with results for each target grouped together, ordered by column.
   * <p>
   * For example, given a set of results with two target, t1 and t2, and two columns c1 and c2, the
   * results will be:
   * <pre>
   *   [t1c1, t1c2, t2c1, t2c2]
   * </pre>
   */
  @PropertyDefinition(validate = "notNull")
  private final ImmutableList<Result<?>> items;

  /**
   * Returns a set of results for some calculations.
   * <p>
   * The number of values must be exactly divisible by the column count.
   *
   * @param rowCount  the number of rows in the results
   * @param columnCount  the number of columns in the results
   * @param values  the calculated values
   * @return a set of results for the calculations
   */
  public static Results of(int rowCount, int columnCount, List<? extends Result<?>> values) {
    return new Results(rowCount, columnCount, values);
  }

  // This is hand written to allow the signature to be customised.
  // The type of the items parameter needs to include a wildcard for the list elements.
  // The corresponding field can't have a wildcard because of a limitation of Joda Beans.
  // The generated constructor parameter has the same type as the field, so in order to have
  // different types for the field and parameter the constructor must be hand written.
  @ImmutableConstructor
  private Results(int rowCount, int columnCount, List<? extends Result<?>> items) {
    this.rowCount = ArgChecker.notNegative(rowCount, "rowCount");
    this.columnCount = ArgChecker.notNegative(columnCount, "columnCount");
    this.items = ImmutableList.copyOf(items);

    if (rowCount * columnCount != items.size()) {
      throw new IllegalArgumentException(
          Messages.format(
              "The number of items ({}) must equal the number of rows ({}) multiplied by the number of columns ({})",
              this.items.size(),
              this.rowCount,
              this.columnCount));
    }
  }

  /**
   * Returns the results for a target and column for a set of scenarios.
   *
   * @param rowIndex   the index of the row containing the results for a target
   * @param columnIndex  the index of the column
   * @return the results for the specified row and column for a set of scenarios
   */
  public Result<?> get(int rowIndex, int columnIndex) {
    if (rowIndex < 0 || rowIndex >= rowCount) {
      throw new IllegalArgumentException(invalidRowIndexMessage(rowIndex));
    }
    if (columnIndex < 0 || columnIndex >= columnCount) {
      throw new IllegalArgumentException(invalidColumnIndexMessage(columnIndex));
    }
    int index = (rowIndex * columnCount) + columnIndex;
    return items.get(index);
  }

  @SuppressWarnings("unchecked")
  public <T> Result<T> get(int rowIndex, ColumnDefinition<T, ?> column) {
    if (rowIndex < 0 || rowIndex >= rowCount) {
      throw new IllegalArgumentException(invalidRowIndexMessage(rowIndex));
    }
    Integer columnIndex = columnsByDefinition.get(column);

    if (columnIndex == null) {
      throw new IllegalArgumentException(invalidColumnMessage(column));
    }
    int index = (rowIndex * columnCount) + columnIndex;
    Result<?> result = items.get(index);

    if (result.isFailure()) {
      return Result.failure(result);
    }
    Object value = result.getValue();
    Measure<T, ?> measure = column.getMeasure();

    if (!measure.getType().isInstance(value)) {
      throw new IllegalArgumentException(
          Messages.format(
              "Value type {} is not an instance of the measure type {}",
              value.getClass().getName(),
              measure.getScenarioType().getName()));
    }
    return (Result<T>) result;
  }

  /**
   * Returns the results for a target and column for a set of scenarios.
   *
   * @param rowIndex   the index of the row containing the results for a target
   * @param columnIndex  the index of the column
   * @return the results for the specified row and column for a set of scenarios
   */
  @SuppressWarnings("unchecked")
  public <T extends ScenarioValuesList<?>> Result<T> get(int rowIndex, Measure<?, T> measure) {
    if (rowIndex < 0 || rowIndex >= rowCount) {
      throw new IllegalArgumentException(invalidRowIndexMessage(rowIndex));
    }
    Integer columnIndex = columnsByDefinition.get(column);

    if (columnIndex == null) {
      throw new IllegalArgumentException(invalidColumnMessage(column));
    }
    int index = (rowIndex * columnCount) + columnIndex;
    Result<?> result = items.get(index);

    if (result.isFailure()) {
      return Result.failure(result);
    }
    Object value = result.getValue();

    if (!measure.getType().isInstance(value)) {
      throw new IllegalArgumentException(
          Messages.format(
              "Value type {} is not an instance of the measure type {}",
              value.getClass().getName(),
              measure.getScenarioType().getName()));
    }
    return (Result<T>) result;
  }

  private String invalidRowIndexMessage(int rowIndex) {
    return Messages.format(
        "Row index must be greater than or equal to zero and less than the row count ({}), but it was {}",
        rowIndex);
  }

  private String invalidColumnIndexMessage(int columnIndex) {
    return Messages.format(
        "Column index must be greater than or equal to zero and less than the column count ({}), but it was {}",
        columnIndex);
  }

  private String invalidColumnMessage(ColumnDefinition<?, ?> column) {
    return "No column in the results matches the column definition " + column;
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code Results}.
   * @return the meta-bean, not null
   */
  public static Results.Meta meta() {
    return Results.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(Results.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static Results.Builder builder() {
    return new Results.Builder();
  }

  @Override
  public Results.Meta metaBean() {
    return Results.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the number of rows in the results.
   * @return the value of the property
   */
  public int getRowCount() {
    return rowCount;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the number of columns in the results.
   * @return the value of the property
   */
  public int getColumnCount() {
    return columnCount;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the results, with results for each target grouped together, ordered by column.
   * <p>
   * For example, given a set of results with two target, t1 and t2, and two columns c1 and c2, the
   * results will be:
   * <pre>
   * [t1c1, t1c2, t2c1, t2c2]
   * </pre>
   * @return the value of the property, not null
   */
  public ImmutableList<Result<?>> getItems() {
    return items;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      Results other = (Results) obj;
      return (rowCount == other.rowCount) &&
          (columnCount == other.columnCount) &&
          JodaBeanUtils.equal(items, other.items);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(rowCount);
    hash = hash * 31 + JodaBeanUtils.hashCode(columnCount);
    hash = hash * 31 + JodaBeanUtils.hashCode(items);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(128);
    buf.append("Results{");
    buf.append("rowCount").append('=').append(rowCount).append(',').append(' ');
    buf.append("columnCount").append('=').append(columnCount).append(',').append(' ');
    buf.append("items").append('=').append(JodaBeanUtils.toString(items));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code Results}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code rowCount} property.
     */
    private final MetaProperty<Integer> rowCount = DirectMetaProperty.ofImmutable(
        this, "rowCount", Results.class, Integer.TYPE);
    /**
     * The meta-property for the {@code columnCount} property.
     */
    private final MetaProperty<Integer> columnCount = DirectMetaProperty.ofImmutable(
        this, "columnCount", Results.class, Integer.TYPE);
    /**
     * The meta-property for the {@code items} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ImmutableList<Result<?>>> items = DirectMetaProperty.ofImmutable(
        this, "items", Results.class, (Class) ImmutableList.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "rowCount",
        "columnCount",
        "items");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 17743701:  // rowCount
          return rowCount;
        case -860736679:  // columnCount
          return columnCount;
        case 100526016:  // items
          return items;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public Results.Builder builder() {
      return new Results.Builder();
    }

    @Override
    public Class<? extends Results> beanType() {
      return Results.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code rowCount} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Integer> rowCount() {
      return rowCount;
    }

    /**
     * The meta-property for the {@code columnCount} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Integer> columnCount() {
      return columnCount;
    }

    /**
     * The meta-property for the {@code items} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ImmutableList<Result<?>>> items() {
      return items;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 17743701:  // rowCount
          return ((Results) bean).getRowCount();
        case -860736679:  // columnCount
          return ((Results) bean).getColumnCount();
        case 100526016:  // items
          return ((Results) bean).getItems();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code Results}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<Results> {

    private int rowCount;
    private int columnCount;
    private List<Result<?>> items = ImmutableList.of();

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(Results beanToCopy) {
      this.rowCount = beanToCopy.getRowCount();
      this.columnCount = beanToCopy.getColumnCount();
      this.items = beanToCopy.getItems();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 17743701:  // rowCount
          return rowCount;
        case -860736679:  // columnCount
          return columnCount;
        case 100526016:  // items
          return items;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 17743701:  // rowCount
          this.rowCount = (Integer) newValue;
          break;
        case -860736679:  // columnCount
          this.columnCount = (Integer) newValue;
          break;
        case 100526016:  // items
          this.items = (List<Result<?>>) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder setString(MetaProperty<?> property, String value) {
      super.setString(property, value);
      return this;
    }

    @Override
    public Builder setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public Results build() {
      return new Results(
          rowCount,
          columnCount,
          items);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the number of rows in the results.
     * @param rowCount  the new value
     * @return this, for chaining, not null
     */
    public Builder rowCount(int rowCount) {
      this.rowCount = rowCount;
      return this;
    }

    /**
     * Sets the number of columns in the results.
     * @param columnCount  the new value
     * @return this, for chaining, not null
     */
    public Builder columnCount(int columnCount) {
      this.columnCount = columnCount;
      return this;
    }

    /**
     * Sets the results, with results for each target grouped together, ordered by column.
     * <p>
     * For example, given a set of results with two target, t1 and t2, and two columns c1 and c2, the
     * results will be:
     * <pre>
     * [t1c1, t1c2, t2c1, t2c2]
     * </pre>
     * @param items  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder items(List<Result<?>> items) {
      JodaBeanUtils.notNull(items, "items");
      this.items = items;
      return this;
    }

    /**
     * Sets the {@code items} property in the builder
     * from an array of objects.
     * @param items  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder items(Result<?>... items) {
      return items(ImmutableList.copyOf(items));
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(128);
      buf.append("Results.Builder{");
      buf.append("rowCount").append('=').append(JodaBeanUtils.toString(rowCount)).append(',').append(' ');
      buf.append("columnCount").append('=').append(JodaBeanUtils.toString(columnCount)).append(',').append(' ');
      buf.append("items").append('=').append(JodaBeanUtils.toString(items));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}

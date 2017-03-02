/*
 * Copyright (C) 2015 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.report.trade;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.collect.ImmutableList;
import com.opengamma.strata.collect.io.IniFile;
import com.opengamma.strata.report.ReportTemplate;

/**
 * Describes the contents and layout of a trade report.
 */
@BeanDefinition
public final class TradeReportTemplate
    implements ReportTemplate, ImmutableBean {

  /**
   * The columns in the report.
   */
  @PropertyDefinition(validate = "notNull")
  private final List<TradeReportColumn> columns;

  /**
   * Creates a trade report template by reading a template definition in an ini file.
   *
   * @param ini  the ini file containing the definition of the template
   * @return a trade report template built from the definition in the ini file
   */
  public static TradeReportTemplate load(IniFile ini) {
    TradeReportTemplateIniLoader loader = new TradeReportTemplateIniLoader();
    return loader.load(ini);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code TradeReportTemplate}.
   * @return the meta-bean, not null
   */
  public static TradeReportTemplate.Meta meta() {
    return TradeReportTemplate.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(TradeReportTemplate.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static TradeReportTemplate.Builder builder() {
    return new TradeReportTemplate.Builder();
  }

  private TradeReportTemplate(
      List<TradeReportColumn> columns) {
    JodaBeanUtils.notNull(columns, "columns");
    this.columns = ImmutableList.copyOf(columns);
  }

  @Override
  public TradeReportTemplate.Meta metaBean() {
    return TradeReportTemplate.Meta.INSTANCE;
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
   * Gets the columns in the report.
   * @return the value of the property, not null
   */
  public List<TradeReportColumn> getColumns() {
    return columns;
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
      TradeReportTemplate other = (TradeReportTemplate) obj;
      return JodaBeanUtils.equal(columns, other.columns);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(columns);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("TradeReportTemplate{");
    buf.append("columns").append('=').append(JodaBeanUtils.toString(columns));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code TradeReportTemplate}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code columns} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<List<TradeReportColumn>> columns = DirectMetaProperty.ofImmutable(
        this, "columns", TradeReportTemplate.class, (Class) List.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "columns");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 949721053:  // columns
          return columns;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public TradeReportTemplate.Builder builder() {
      return new TradeReportTemplate.Builder();
    }

    @Override
    public Class<? extends TradeReportTemplate> beanType() {
      return TradeReportTemplate.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code columns} property.
     * @return the meta-property, not null
     */
    public MetaProperty<List<TradeReportColumn>> columns() {
      return columns;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 949721053:  // columns
          return ((TradeReportTemplate) bean).getColumns();
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
   * The bean-builder for {@code TradeReportTemplate}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<TradeReportTemplate> {

    private List<TradeReportColumn> columns = ImmutableList.of();

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(TradeReportTemplate beanToCopy) {
      this.columns = ImmutableList.copyOf(beanToCopy.getColumns());
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 949721053:  // columns
          return columns;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 949721053:  // columns
          this.columns = (List<TradeReportColumn>) newValue;
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
    public TradeReportTemplate build() {
      return new TradeReportTemplate(
          columns);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the columns in the report.
     * @param columns  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder columns(List<TradeReportColumn> columns) {
      JodaBeanUtils.notNull(columns, "columns");
      this.columns = columns;
      return this;
    }

    /**
     * Sets the {@code columns} property in the builder
     * from an array of objects.
     * @param columns  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder columns(TradeReportColumn... columns) {
      return columns(ImmutableList.copyOf(columns));
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(64);
      buf.append("TradeReportTemplate.Builder{");
      buf.append("columns").append('=').append(JodaBeanUtils.toString(columns));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}

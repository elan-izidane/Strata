/**
 * Copyright (C) 2017 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.pricer.credit;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanBuilder;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;
import org.joda.beans.impl.direct.DirectPrivateBeanBuilder;

import com.google.common.collect.ImmutableMap;
import com.opengamma.strata.basics.StandardId;
import com.opengamma.strata.basics.currency.Currency;
import com.opengamma.strata.basics.currency.FxConvertible;
import com.opengamma.strata.basics.currency.FxRateProvider;

/**
 * The result of calculating Jump-To-Default.
 * <p>
 * The result is an array of currency amounts, all with the same currency,
 * keyed by the legal entity {@code StandardId}.
 */
@BeanDefinition(builderScope = "private")
public final class JumpToDefault
    implements FxConvertible<JumpToDefault>, ImmutableBean, Serializable {

  /**
   * The currency of the amounts.
   */
  @PropertyDefinition(validate = "notNull")
  private final Currency currency;
  /**
   * The amounts, identified by legal entity ID.
   */
  @PropertyDefinition(validate = "notNull")
  private final ImmutableMap<StandardId, Double> amounts;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance from currency and map.
   * 
   * @param currency  the currency of the values
   * @param splitValues  the split values
   * @return the instance
   */
  public static JumpToDefault of(Currency currency, Map<StandardId, Double> splitValues) {
    return new JumpToDefault(currency, splitValues);
  }

  //-------------------------------------------------------------------------
  @Override
  public JumpToDefault convertedTo(Currency resultCurrency, FxRateProvider rateProvider) {
    Map<StandardId, Double> mutable = new HashMap<>();
    for (Entry<StandardId, Double> entry : amounts.entrySet()) {
      double converted = rateProvider.convert(entry.getValue(), currency, resultCurrency);
      mutable.put(entry.getKey(), converted);
    }
    return JumpToDefault.of(resultCurrency, mutable);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code JumpToDefault}.
   * @return the meta-bean, not null
   */
  public static JumpToDefault.Meta meta() {
    return JumpToDefault.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(JumpToDefault.Meta.INSTANCE);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private JumpToDefault(
      Currency currency,
      Map<StandardId, Double> amounts) {
    JodaBeanUtils.notNull(currency, "currency");
    JodaBeanUtils.notNull(amounts, "amounts");
    this.currency = currency;
    this.amounts = ImmutableMap.copyOf(amounts);
  }

  @Override
  public JumpToDefault.Meta metaBean() {
    return JumpToDefault.Meta.INSTANCE;
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
   * Gets the currency of the amounts.
   * @return the value of the property, not null
   */
  public Currency getCurrency() {
    return currency;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the amounts, identified by legal entity ID.
   * @return the value of the property, not null
   */
  public ImmutableMap<StandardId, Double> getAmounts() {
    return amounts;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      JumpToDefault other = (JumpToDefault) obj;
      return JodaBeanUtils.equal(currency, other.currency) &&
          JodaBeanUtils.equal(amounts, other.amounts);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(currency);
    hash = hash * 31 + JodaBeanUtils.hashCode(amounts);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("JumpToDefault{");
    buf.append("currency").append('=').append(currency).append(',').append(' ');
    buf.append("amounts").append('=').append(JodaBeanUtils.toString(amounts));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code JumpToDefault}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code currency} property.
     */
    private final MetaProperty<Currency> currency = DirectMetaProperty.ofImmutable(
        this, "currency", JumpToDefault.class, Currency.class);
    /**
     * The meta-property for the {@code amounts} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ImmutableMap<StandardId, Double>> amounts = DirectMetaProperty.ofImmutable(
        this, "amounts", JumpToDefault.class, (Class) ImmutableMap.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "currency",
        "amounts");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return currency;
        case -879772901:  // amounts
          return amounts;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public BeanBuilder<? extends JumpToDefault> builder() {
      return new JumpToDefault.Builder();
    }

    @Override
    public Class<? extends JumpToDefault> beanType() {
      return JumpToDefault.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code currency} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Currency> currency() {
      return currency;
    }

    /**
     * The meta-property for the {@code amounts} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ImmutableMap<StandardId, Double>> amounts() {
      return amounts;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return ((JumpToDefault) bean).getCurrency();
        case -879772901:  // amounts
          return ((JumpToDefault) bean).getAmounts();
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
   * The bean-builder for {@code JumpToDefault}.
   */
  private static final class Builder extends DirectPrivateBeanBuilder<JumpToDefault> {

    private Currency currency;
    private Map<StandardId, Double> amounts = ImmutableMap.of();

    /**
     * Restricted constructor.
     */
    private Builder() {
      super(meta());
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          return currency;
        case -879772901:  // amounts
          return amounts;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 575402001:  // currency
          this.currency = (Currency) newValue;
          break;
        case -879772901:  // amounts
          this.amounts = (Map<StandardId, Double>) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public JumpToDefault build() {
      return new JumpToDefault(
          currency,
          amounts);
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("JumpToDefault.Builder{");
      buf.append("currency").append('=').append(JodaBeanUtils.toString(currency)).append(',').append(' ');
      buf.append("amounts").append('=').append(JodaBeanUtils.toString(amounts));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}

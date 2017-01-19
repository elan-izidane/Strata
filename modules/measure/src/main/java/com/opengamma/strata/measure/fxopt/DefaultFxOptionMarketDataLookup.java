/**
 * Copyright (C) 2016 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.strata.measure.fxopt;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.light.LightMetaBean;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.opengamma.strata.basics.currency.CurrencyPair;
import com.opengamma.strata.calc.CalculationRules;
import com.opengamma.strata.calc.runner.CalculationParameter;
import com.opengamma.strata.calc.runner.FunctionRequirements;
import com.opengamma.strata.collect.Messages;
import com.opengamma.strata.data.MarketData;
import com.opengamma.strata.data.MarketDataId;
import com.opengamma.strata.data.MarketDataNotFoundException;
import com.opengamma.strata.data.scenario.ScenarioMarketData;
import com.opengamma.strata.pricer.fxopt.FxOptionVolatilities;
import com.opengamma.strata.pricer.fxopt.FxOptionVolatilitiesId;

/**
 * The FX options lookup, used to select volatilities for pricing.
 * <p>
 * This provides FX options volatilities by currency pair.
 * <p>
 * The lookup implements {@link CalculationParameter} and is used by passing it
 * as an argument to {@link CalculationRules}. It provides the link between the
 * data that the function needs and the data that is available in {@link ScenarioMarketData}.
 */
@BeanDefinition(style = "light")
final class DefaultFxOptionMarketDataLookup
    implements FxOptionMarketDataLookup, ImmutableBean, Serializable {

  /**
   * The volatility identifiers, keyed by currency pair.
   */
  @PropertyDefinition(validate = "notNull")
  private final ImmutableMap<CurrencyPair, FxOptionVolatilitiesId> volatilityIds;

  //-------------------------------------------------------------------------
  /**
   * Obtains an instance based on a single mapping from currency pair to volatility identifier.
   * <p>
   * The lookup provides volatilities for the specified currency pair.
   *
   * @param currencyPair  the currency pair
   * @param volatilityId  the volatility identifier
   * @return the FX options lookup containing the specified mapping
   */
  public static DefaultFxOptionMarketDataLookup of(CurrencyPair currencyPair, FxOptionVolatilitiesId volatilityId) {
    return new DefaultFxOptionMarketDataLookup(ImmutableMap.of(currencyPair, volatilityId));
  }

  /**
   * Obtains an instance based on a map of volatility identifiers.
   * <p>
   * The map is used to specify the appropriate volatilities to use for each currency pair.
   *
   * @param volatilityIds  the volatility identifiers, keyed by currency pair
   * @return the FX options lookup containing the specified volatilities
   */
  public static DefaultFxOptionMarketDataLookup of(Map<CurrencyPair, FxOptionVolatilitiesId> volatilityIds) {
    return new DefaultFxOptionMarketDataLookup(volatilityIds);
  }

  //-------------------------------------------------------------------------
  @Override
  public ImmutableSet<CurrencyPair> getVolatilityCurrencyPairs() {
    return volatilityIds.keySet();
  }

  @Override
  public ImmutableSet<MarketDataId<?>> getVolatilityIds(CurrencyPair currencyPair) {
    FxOptionVolatilitiesId id = volatilityIds.get(currencyPair);
    if (id == null) {
      throw new IllegalArgumentException(msgPairNotFound(currencyPair));
    }
    return ImmutableSet.of(id);
  }

  //-------------------------------------------------------------------------
  @Override
  public FunctionRequirements requirements(Set<CurrencyPair> currencyPairs) {
    for (CurrencyPair currencyPair : currencyPairs) {
      if (!volatilityIds.keySet().contains(currencyPair)) {
        throw new IllegalArgumentException(msgPairNotFound(currencyPair));
      }
    }
    return FunctionRequirements.builder()
        .valueRequirements(ImmutableSet.copyOf(volatilityIds.values()))
        .build();
  }

  //-------------------------------------------------------------------------
  @Override
  public FxOptionVolatilities volatilities(CurrencyPair currencyPair, MarketData marketData) {
    FxOptionVolatilitiesId volatilityId = volatilityIds.get(currencyPair);
    if (volatilityId == null) {
      throw new MarketDataNotFoundException(msgPairNotFound(currencyPair));
    }
    return marketData.getValue(volatilityId);
  }

  //-------------------------------------------------------------------------
  private String msgPairNotFound(CurrencyPair currencyPair) {
    return Messages.format("FxOption lookup has no volatilities defined for currency pair '{}'", currencyPair);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code DefaultFxOptionMarketDataLookup}.
   */
  private static final MetaBean META_BEAN = LightMetaBean.of(DefaultFxOptionMarketDataLookup.class);

  /**
   * The meta-bean for {@code DefaultFxOptionMarketDataLookup}.
   * @return the meta-bean, not null
   */
  public static MetaBean meta() {
    return META_BEAN;
  }

  static {
    JodaBeanUtils.registerMetaBean(META_BEAN);
  }

  /**
   * The serialization version id.
   */
  private static final long serialVersionUID = 1L;

  private DefaultFxOptionMarketDataLookup(
      Map<CurrencyPair, FxOptionVolatilitiesId> volatilityIds) {
    JodaBeanUtils.notNull(volatilityIds, "volatilityIds");
    this.volatilityIds = ImmutableMap.copyOf(volatilityIds);
  }

  @Override
  public MetaBean metaBean() {
    return META_BEAN;
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
   * Gets the volatility identifiers, keyed by currency pair.
   * @return the value of the property, not null
   */
  public ImmutableMap<CurrencyPair, FxOptionVolatilitiesId> getVolatilityIds() {
    return volatilityIds;
  }

  //-----------------------------------------------------------------------
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      DefaultFxOptionMarketDataLookup other = (DefaultFxOptionMarketDataLookup) obj;
      return JodaBeanUtils.equal(volatilityIds, other.volatilityIds);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(volatilityIds);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(64);
    buf.append("DefaultFxOptionMarketDataLookup{");
    buf.append("volatilityIds").append('=').append(JodaBeanUtils.toString(volatilityIds));
    buf.append('}');
    return buf.toString();
  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}

/**
 * generated by Xtext 2.12.0
 */
package com.hribol.bromium.dsl.bromium.impl;

import com.hribol.bromium.dsl.bromium.BromiumPackage;
import com.hribol.bromium.dsl.bromium.ClickCssSelector;
import com.hribol.bromium.dsl.bromium.ParameterValue;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Click Css Selector</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.hribol.bromium.dsl.bromium.impl.ClickCssSelectorImpl#getCssSelector <em>Css Selector</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ClickCssSelectorImpl extends WebDriverActionImpl implements ClickCssSelector
{
  /**
   * The cached value of the '{@link #getCssSelector() <em>Css Selector</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getCssSelector()
   * @generated
   * @ordered
   */
  protected ParameterValue cssSelector;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ClickCssSelectorImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return BromiumPackage.Literals.CLICK_CSS_SELECTOR;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ParameterValue getCssSelector()
  {
    return cssSelector;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetCssSelector(ParameterValue newCssSelector, NotificationChain msgs)
  {
    ParameterValue oldCssSelector = cssSelector;
    cssSelector = newCssSelector;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BromiumPackage.CLICK_CSS_SELECTOR__CSS_SELECTOR, oldCssSelector, newCssSelector);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setCssSelector(ParameterValue newCssSelector)
  {
    if (newCssSelector != cssSelector)
    {
      NotificationChain msgs = null;
      if (cssSelector != null)
        msgs = ((InternalEObject)cssSelector).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - BromiumPackage.CLICK_CSS_SELECTOR__CSS_SELECTOR, null, msgs);
      if (newCssSelector != null)
        msgs = ((InternalEObject)newCssSelector).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - BromiumPackage.CLICK_CSS_SELECTOR__CSS_SELECTOR, null, msgs);
      msgs = basicSetCssSelector(newCssSelector, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, BromiumPackage.CLICK_CSS_SELECTOR__CSS_SELECTOR, newCssSelector, newCssSelector));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case BromiumPackage.CLICK_CSS_SELECTOR__CSS_SELECTOR:
        return basicSetCssSelector(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case BromiumPackage.CLICK_CSS_SELECTOR__CSS_SELECTOR:
        return getCssSelector();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case BromiumPackage.CLICK_CSS_SELECTOR__CSS_SELECTOR:
        setCssSelector((ParameterValue)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case BromiumPackage.CLICK_CSS_SELECTOR__CSS_SELECTOR:
        setCssSelector((ParameterValue)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case BromiumPackage.CLICK_CSS_SELECTOR__CSS_SELECTOR:
        return cssSelector != null;
    }
    return super.eIsSet(featureID);
  }

} //ClickCssSelectorImpl
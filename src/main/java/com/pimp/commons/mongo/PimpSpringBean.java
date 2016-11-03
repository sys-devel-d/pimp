package com.pimp.commons.mongo;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.pimp.commons.exceptions.SpringBeanCreationException;

/**
 * @author Kevin Goy
 */
public class PimpSpringBean implements ISpringBean {

  private static final Logger LOGGER = LoggerFactory.getLogger(PimpSpringBean.class);
  private static ApplicationContext applicationContext;
  private String beanName;

  public PimpSpringBean() {
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    applicationContext = applicationContext;
  }

  public static void inject(Object target) {
    try {
      inject(applicationContext, target);
    }
    catch (Exception var2) {
      LOGGER.error("Could not inject beans and application context (" + applicationContext + ") into target " + target,
          var2);
    }

  }

  public static void inject(ApplicationContext applicationContext, Object target) {
    if (applicationContext != null && target != null) {
      applicationContext.getAutowireCapableBeanFactory().autowireBeanProperties(target, 0, false);
      if (target instanceof ApplicationContextAware) {
        ((ApplicationContextAware) target).setApplicationContext(applicationContext);
      }

    }
    else {
      throw new IllegalArgumentException(
          "Got invalid arguments applicationContext:" + applicationContext + " target:" + target);
    }
  }

  @PostConstruct
  private void rootPostConstruct() throws SpringBeanCreationException {
    this.postConstruct();
  }

  protected void postConstruct() throws SpringBeanCreationException {
  }

  public String getBeanName() {
    return this.beanName;
  }

  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }
}

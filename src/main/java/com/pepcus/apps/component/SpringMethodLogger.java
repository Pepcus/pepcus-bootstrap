package com.pepcus.apps.component;

import java.util.Optional;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.jcabi.aspects.Loggable;
import com.jcabi.aspects.aj.MethodLogger;
import lombok.NoArgsConstructor;

/**
 * The Class SpringMethodLogger makes {@link Loggable} working in Spring proxies to class and method
 * level logs.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Aspect
@Component
@NoArgsConstructor
public class SpringMethodLogger {

  private static final MethodLogger METHOD_LOGGER =
      Optional.ofNullable(MethodLogger.aspectOf()).orElse(new MethodLogger());

  /**
   * Method to add class level log
   * 
   * @param point
   * @return
   * @throws Throwable
   */
  @Around("execution(public * (@com.jcabi.aspects.Loggable *).*(..))" + " && !execution(String *.toString())"
      + " && !execution(int *.hashCode())" + " && !execution(boolean *.canEqual(Object))"
      + " && !execution(boolean *.equals(Object))")
  public Object wrapClass(final ProceedingJoinPoint point) throws Throwable {
    return METHOD_LOGGER.wrapClass(point);
  }

  /**
   * Method to add method level log
   * 
   * @param point
   * @return
   * @throws Throwable
   */
  @Around("@annotation(com.jcabi.aspects.Loggable)")
  public Object wrapMethod(final ProceedingJoinPoint point) throws Throwable {
    return METHOD_LOGGER.wrapMethod(point);
  }
}

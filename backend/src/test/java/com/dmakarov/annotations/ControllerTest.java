package com.dmakarov.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

/**
 * Represents backend controller test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@WebMvcTest
@AutoConfigureMockMvc
@ActiveProfiles
public @interface ControllerTest {

  /**
   * Specifies the controllers to test. This is an alias of {@link WebMvcTest#controllers()} which
   * can be used for brevity if no other attributes are defined. See {@link
   * WebMvcTest#controllers()} for details.
   *
   * @return the controllers to test
   * @see WebMvcTest#controllers()
   */
  @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
  Class<?>[] value() default {};

  /**
   * Active profiles list. Default is 'test'.
   *
   * @return active profiles array
   */
  @AliasFor(annotation = ActiveProfiles.class, attribute = "profiles")
  String[] activeProfiles() default {"test"};

}

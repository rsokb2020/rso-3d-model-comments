package si.fri.rso.kb6750.model3dcomments.api.v1.health;


import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used to decorate built-in health checks
 */
@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface BuiltInHealthCheck {
}
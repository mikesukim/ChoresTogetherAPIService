package chorestogetherapiservice.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.ws.rs.NameBinding;

/**
 * Annotation to be used at jersey's filter, so that filter is not applied globally (for all APIs).
 * <a href="https://javaee.github.io/javaee-spec/javadocs/javax/ws/rs/NameBinding.html">more info</a>
 * */
@NameBinding
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface Authenticated {
}

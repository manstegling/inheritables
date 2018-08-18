package se.motility.inheritables.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated classes must implement a default constructor (directly or 
 * indirectly) or compilation will fail. Interfaces and abstract classes
 * are excluded from this requirement.
 * <p>
 * This annotation is not only passed down to subclasses but can also be
 * used with interfaces to impose this requirement on classes implementing
 * this interface (anywhere in the type hierarchy).
 * <p>
 * This is convenient, for example, when using messaging frameworks which
 * work through reflection and thus rely on the existence of a default
 * constructor for calling {@link Class#newInstance()}, such as Jackson.
 * <p>
 * Simply annotate your 'Message' super-interface and all your message
 * types will automatically be checked at compile-time, saving you from
 * embarrassing run-time exceptions.
 * <p>
 * This annotation is retained at class level to allow inheritance across
 * modules and projects.
 * 
 * @author M Tegling
 * 
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RequireDefaultConstructor {

}

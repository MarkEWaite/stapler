package org.kohsuke.stapler.verb;

import jakarta.servlet.ServletException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import org.kohsuke.stapler.CancelRequestHandlingException;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.StaplerResponse2;
import org.kohsuke.stapler.interceptor.Interceptor;
import org.kohsuke.stapler.interceptor.InterceptorAnnotation;

/**
 * Restricts the routing to matching HTTP verbs.
 *
 * <h2>Usage</h2>
 * <p>
 * This package defines a number of HTTP verb (method) annotations that can be used to restrict
 * routing. For example,
 *
 * <pre>
 * &#64;WebMethod(name="") &#64;DELETE
 * public void delete() {
 *     // this method will be invoked only when the request is DELETE
 *     ...
 * }
 *
 * &#64;WebMethod(name="") &#64;POST
 * public void create(&#64;JsonBody Order order) {
 *     // this method will be invoked only when the request is POST
 *     ...
 * }
 * </pre>
 *
 * <p>
 * This class is the actual logic that implements this semantics on top of {@link Interceptor}.
 *
 * @author Kohsuke Kawaguchi
 * @see GET
 * @see POST
 * @see PUT
 * @see DELETE
 */
public class HttpVerbInterceptor extends Interceptor {
    @Override
    public Object invoke(StaplerRequest2 request, StaplerResponse2 response, Object instance, Object[] arguments)
            throws IllegalAccessException, InvocationTargetException, ServletException {
        if (matches(request)) {
            return target.invoke(request, response, instance, arguments);
        } else {
            throw new CancelRequestHandlingException();
        }
    }

    private boolean matches(StaplerRequest2 request) {
        String method = request.getMethod();

        for (Annotation a : target.getAnnotations()) {
            Class<? extends Annotation> t = a.annotationType();
            InterceptorAnnotation ia = t.getAnnotation(InterceptorAnnotation.class);
            if (ia != null && ia.value() == HttpVerbInterceptor.class) {
                if (t.getSimpleName().equals(method)) {
                    return true;
                }
            }
        }
        return false;
    }
}

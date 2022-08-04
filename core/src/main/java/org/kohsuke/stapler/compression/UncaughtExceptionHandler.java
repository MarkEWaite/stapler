package org.kohsuke.stapler.compression;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;

import static org.kohsuke.stapler.Stapler.escape;

/**
 * Handles an exception caught by {@link CompressionFilter}.
 *
 * See {@link CompressionFilter} javadoc for why this exception needs to be handled
 * by us and can't just be handled by the servlet container like it does all others.
 *
 * @author Kohsuke Kawaguchi
 */
public interface UncaughtExceptionHandler {
    /**
     * Called to render the exception as an HTTP response.
     */
    void reportException(Throwable e, ServletContext context, HttpServletRequest req, HttpServletResponse rsp)
            throws ServletException, IOException;


    UncaughtExceptionHandler DEFAULT = new UncaughtExceptionHandler() {
        @Override
        @SuppressFBWarnings(value = "XSS_SERVLET", justification = "Covered by the escape() method.")
        public void reportException(Throwable e, ServletContext context, HttpServletRequest req, HttpServletResponse rsp) throws ServletException, IOException {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.close();

            rsp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            rsp.setContentType("text/html");
            PrintWriter w = rsp.getWriter();
            String message = e.getMessage();
            w.print(MessageFormat.format("<html><head><title>Error {0}</title></head>\n" +
                    "<body bgcolor=#fff><h1>Status Code: {0}</h1>Exception: {1}<br>Stacktrace: <pre>{2}</pre><br><hr>\n" +
                    "<i>Generated by Stapler at {3}</i></body></html>",
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    message != null ? escape(message) : "?",
                    escape(sw.toString()),
                    new Date().toString()
            ));
            w.close();
        }
    };
}

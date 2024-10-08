package org.kohsuke.stapler.event;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.stapler.RequestImpl;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.StaplerResponse2;
import org.kohsuke.stapler.lang.FieldRef;

public interface FilteredFieldTriggerListener {
    boolean onFieldTrigger(FieldRef f, StaplerRequest2 req, StaplerResponse2 rsp, Object node, String expression);

    FilteredFieldTriggerListener JUST_WARN = new FilteredFieldTriggerListener() {
        private final Logger LOGGER = Logger.getLogger(FilteredFieldTriggerListener.class.getName());

        @Override
        public boolean onFieldTrigger(
                FieldRef f, StaplerRequest2 req, StaplerResponse2 rsp, Object node, String expression) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(String.format(
                        "BLOCKED -> evaluate(<%s>.%s,\"%s\")",
                        node, expression, ((RequestImpl) req).tokens.assembleOriginalRestOfPath()));
            }
            return false;
        }
    };
}

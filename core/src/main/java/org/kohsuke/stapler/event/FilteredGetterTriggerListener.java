/*
 * The MIT License
 *
 * Copyright (c) 2018, CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.kohsuke.stapler.event;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.stapler.Function;
import org.kohsuke.stapler.RequestImpl;
import org.kohsuke.stapler.StaplerRequest2;
import org.kohsuke.stapler.StaplerResponse2;

/**
 * Listener that is triggered when a getter function - that is no more accepted - is called.
 */
public interface FilteredGetterTriggerListener {
    boolean onGetterTrigger(Function f, StaplerRequest2 req, StaplerResponse2 rsp, Object node, String expression);

    FilteredGetterTriggerListener JUST_WARN = new FilteredGetterTriggerListener() {
        private final Logger LOGGER = Logger.getLogger(FilteredGetterTriggerListener.class.getName());

        @Override
        public boolean onGetterTrigger(
                Function f, StaplerRequest2 req, StaplerResponse2 rsp, Object node, String expression) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(String.format(
                        "BLOCKED -> evaluate(<%s>.%s,\"%s\")",
                        node, expression, ((RequestImpl) req).tokens.assembleOriginalRestOfPath()));
            }
            return false;
        }
    };
}

package org.kohsuke.stapler.json;

import java.net.URL;
import net.sf.json.JSONObject;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlPage;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.HttpResponses;
import org.kohsuke.stapler.StaticViewFacet;
import org.kohsuke.stapler.test.JettyTestCase;

/**
 * @author Kohsuke Kawaguchi
 */
public class SubmittedFormTest extends JettyTestCase {
    /**
     * To load form for test, allow *.html to be served as a view
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        webApp.facets.add(new StaticViewFacet("html"));
    }

    public void testMainFeature() throws Exception {
        WebClient wc = createWebClient();
        HtmlPage page = wc.getPage(new URL(url, "/form.html"));
        HtmlForm f = page.getFormByName("main");
        f.getInputByName("json").setValue("{\"first\":\"Kohsuke\",\"last\":\"Kawaguchi\"}");
        f.submit(null);
    }

    public HttpResponse doSubmit(@SubmittedForm JSONObject o) {
        assertEquals("Kohsuke", o.getString("first"));
        assertEquals("Kawaguchi", o.getString("last"));
        return HttpResponses.ok();
    }
}

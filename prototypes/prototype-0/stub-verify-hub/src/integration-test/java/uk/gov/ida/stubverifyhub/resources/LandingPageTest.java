package uk.gov.ida.stubverifyhub.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

import static javax.ws.rs.core.Response.Status.OK;
import static javax.ws.rs.core.Response.Status.SEE_OTHER;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.gov.ida.stubverifyhub.utils.Base64EncodeUtils.decode;
import static uk.gov.ida.stubverifyhub.utils.Base64EncodeUtils.encode;
import static uk.gov.ida.stubverifyhub.utils.JsonUtils.objectMapper;

public class LandingPageTest extends StubVerifyHubAppRuleTestBase {

    private static final String samlRequest = "a-saml-request";
    private static final String relayState = "a-relay-state";

    @Test
    public void sendAuthnRequest() throws Exception{
        Map<String, String> formData = new HashMap<String, String>() {{
            put("SAMLRequest", samlRequest);
            put("relayState", relayState);
        }};

        Response response = client.target(getUriForPath("/SAML2/SSO"))
            .request()
            .header("Content-Type", "application/x-www-form-urlencoded")
            .post(Entity.form(new MultivaluedHashMap<>(formData)));

        Map<String, String> formDataMap = objectMapper.readValue(
            decode(response.getCookies().get("sendAuthnRequest").getValue()),
            new TypeReference<Map<String, String>>() {
            }
        );

        assertThat(response.getStatus()).isEqualTo(SEE_OTHER.getStatusCode());
        assertThat(formDataMap).isEqualTo(formData);
    }

    @Test
    public void sendAuthnRequest_shouldReturnLandingPage() throws Exception {
        Map<String, String> formData = new HashMap<String, String>() {{
            put("SAMLRequest", samlRequest);
            put("relayState", relayState);
        }};

        Response response = client.target(getUriForPath("/SAML2/SSO"))
            .request()
            .cookie(new NewCookie("sendAuthnRequest", encode(objectMapper.writeValueAsString(formData))))
            .get();
        String html = response.readEntity(String.class);

        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(html).contains("You've sent a request.");
        assertThat(html).contains("<form action=\"/choose-response\" method=\"POST\">");
        assertThat(html).contains("<input type=\"hidden\" name=\"SAMLRequest\" value=\"" + samlRequest + "\"/>");
        assertThat(html).contains("<input type=\"hidden\" name=\"relayState\" value=\"" + relayState + "\"/>");
    }

}

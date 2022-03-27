package com.dmakarov.config;

import com.dmakarov.config.properties.K8sProperties;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Data
@RequiredArgsConstructor
public class K8sConfig {
  private static final String clientCert =
          "-----BEGIN CERTIFICATE-----\n" +
          "MIIDITCCAgmgAwIBAgIISlXVrG8zATowDQYJKoZIhvcNAQELBQAwFTETMBEGA1UE\n" +
          "AxMKa3ViZXJuZXRlczAeFw0yMTEyMDcxNDQ0MzNaFw0yMjEyMDcxNDQ0MzRaMDQx\n" +
          "FzAVBgNVBAoTDnN5c3RlbTptYXN0ZXJzMRkwFwYDVQQDExBrdWJlcm5ldGVzLWFk\n" +
          "bWluMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwcVZn8UOVZYratYS\n" +
          "eB3nfMPwHTQQLEecViqETYL+D1KNOgCzVqeLfo/DOr0hZoz6Z3pDzWFjzZ4mE7yA\n" +
          "wGa1cN4uJ8OZMi9GM6wtq3gppaRjObYp37FvdDerae52M6nZmz0RG4FqaxoGgmQV\n" +
          "cIVK+sNDewUJp2ze2oUCQOCKrI57Wg0RsBLFlXXZPtHdkKc5n+7cXL2b48ydTl6U\n" +
          "JPczmxZmY9eQicGxurRMHWTuLjXe7T6VInxGGj6NaFGWDKGCefVBTrHkt6tLgeEj\n" +
          "1sqgbQjtuulsDLG/FL9Cz4zzx1x/G4ZzApnKxXnfofoSA9fES/BJy71J8FKAJC32\n" +
          "tH7p+wIDAQABo1YwVDAOBgNVHQ8BAf8EBAMCBaAwEwYDVR0lBAwwCgYIKwYBBQUH\n" +
          "AwIwDAYDVR0TAQH/BAIwADAfBgNVHSMEGDAWgBQ/hj/kTBms1k+zdWk8rUQdlwUh\n" +
          "nTANBgkqhkiG9w0BAQsFAAOCAQEAAW2dexPy1yajFgudQxeX2n9kUd5n0RaekFuN\n" +
          "bRdiQ7rfqYdoOANZk/BHfTQwi/ztrqqc4/1XQ9RyqYBavBUI3WL9hCkPWgbw1mwL\n" +
          "81jwZ1T/R/rpSp4EAMSVmvqCdOh1GE5hswRamLkd7461W+9BbdO05X+CubGCElOD\n" +
          "MgV6CWDi55sJsTN8T6iDuF67rmkMgui9393SkaohmSoMlZ8J2xL23cM8NCnzWaj7\n" +
          "nLskXaaWgpAmqGT2Mt5kSzPdComdgqeGG5/aA/wvvHqxmEVULX1qozjunRlYEmvR\n" +
          "bZurIPC/Td/GSkEvi5fWqZ4g16Ec4BeAbwRLPxhuuBJDNC4FcA==\n" +
          "-----END CERTIFICATE-----";

  private static final String clientKey =
          "-----BEGIN RSA PRIVATE KEY-----\n" +
          "MIIEpAIBAAKCAQEAwcVZn8UOVZYratYSeB3nfMPwHTQQLEecViqETYL+D1KNOgCz\n" +
          "VqeLfo/DOr0hZoz6Z3pDzWFjzZ4mE7yAwGa1cN4uJ8OZMi9GM6wtq3gppaRjObYp\n" +
          "37FvdDerae52M6nZmz0RG4FqaxoGgmQVcIVK+sNDewUJp2ze2oUCQOCKrI57Wg0R\n" +
          "sBLFlXXZPtHdkKc5n+7cXL2b48ydTl6UJPczmxZmY9eQicGxurRMHWTuLjXe7T6V\n" +
          "InxGGj6NaFGWDKGCefVBTrHkt6tLgeEj1sqgbQjtuulsDLG/FL9Cz4zzx1x/G4Zz\n" +
          "ApnKxXnfofoSA9fES/BJy71J8FKAJC32tH7p+wIDAQABAoIBAHAmM9A38irxBmII\n" +
          "Upo43KEmmpOcbXvjWoW4h+iA12Cw69JVU3bk8+Slel5F1s4O7GL1rcZyBpycWvdz\n" +
          "U6WIDJ2tylLzb3hPSjdgAPLMUnZvZYqidyvj+1jtW4WtIbaXKL4jowIjbh9c6cWP\n" +
          "/aSpURM/7o+GA+dmKLma4AoCq+Bgd2v3JH6TgsH85Rj+adc+jMBkPo/p7Bt5mtai\n" +
          "Megd+vKR4LnDsOWWipbbszByodeABeStHz5FmLkRmgvjtUj0/r2zE0culmV7ug+0\n" +
          "YID7HKlda5Q6V09qRgPKbmMhn7buqkFPqYEdXZO0ukMU8Zf4+1WEp3Oc/yCkf0mZ\n" +
          "Xg7W98kCgYEA+Toue6DM9LnPdGlij1w0aoIdQrl24nDR4mrQHhoYgXUYXcXuZft7\n" +
          "AipSiN6J5Dn2CHlSIP93rqsf7oqiRX7IMGH/rI5nxEmsyDnfUaS5wdyZmfb13n0D\n" +
          "JbEzPoiit13jURTJ9PhpbUL8nYwdXe7p5lYkr2Zc7jSVe8pmsFjGxe8CgYEAxwle\n" +
          "9SgFiQKwaLHpLNAKHfkr0ogYKYkT8UqwcGzTummPB33909drfMf1cHk+RpEc3+47\n" +
          "Dd8vEErawfOBnYehj8oFn7JJPRe0xnczWe5VpSV7O6WDdB5qLIaGRaNiYLD/awZa\n" +
          "JwnoBhaTb5bPGXXjulJP4P1gZNHwTA8n7lQSiLUCgYBh9xXIcMe/tr4uMzKTyZms\n" +
          "+CJKVR6/GbvWtUfDXVyenomKasAwBC8xBbPVqywmswXcBNxcri68UPXHTWVFzfUg\n" +
          "15Spnnx5csBhICaBMC/4XR9fnuy8nQU8rq/G9+yGCXjOKU7NZUwDr94ei7ILd1sn\n" +
          "uFrJO4Jx++d6dm/xB6RyrwKBgQCvmyb6qcrobUTzujR+Yzbp5xOw9n5K9zRvKXSu\n" +
          "XHYZ/FRDSfgPVfrx5wI3r+EwJYwSToAv5wABIeoMQSnUsPvsf1Oi6IwRqL7IdBcM\n" +
          "e2l5Aa7Om2N1qfgHPNFu045SvRc9eCdGVkkuRfDi/W3PiuHXXBQ575OCoG+gy6Zx\n" +
          "ch/AGQKBgQDd5USMfnJee9r94i97GCYGrNCrKSUVmfRQQzpYH5bDwOWWF/AUZM1u\n" +
          "pqfc3LFFORP7dl22JtZ7tlF4NmofkAzZsEYt44N2g4qib14AnqUFUsQLF+9bjqkR\n" +
          "6yun21BajwQ9zNoBbmNBhJ0ZsssQQ8ppCHpAzzPA99FVFMS3kGm9Cw==\n" +
          "-----END RSA PRIVATE KEY-----";

  private final K8sProperties properties;

  /**
   * Kubernetes cluster client configuration.
   */
  @Bean
  public Config kubernetesConfig() {
    return new ConfigBuilder()
        .withClientCertData(clientCert)
        .withClientKeyData(clientKey)
        .withMasterUrl(properties.getEndpoint())
        .withNamespace(properties.getDefaultNamespace())
        .withConnectionTimeout(10000)
        .build();
  }

}

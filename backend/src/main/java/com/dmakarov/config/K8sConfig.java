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
          "MIIDCjCCAfKgAwIBAgIIFWseuVe7DjEwDQYJKoZIhvcNAQELBQAwEjEQMA4GA1UE\n" +
          "AxMHa3ViZS1jYTAeFw0yMjAxMDQwOTU3MDRaFw0zMjAxMDIwOTU3MDVaMC4xFzAV\n" +
          "BgNVBAoTDnN5c3RlbTptYXN0ZXJzMRMwEQYDVQQDEwprdWJlLWFkbWluMIIBIjAN\n" +
          "BgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA27ZP93Z2XvBuvCm6ml0z/D7kqG/j\n" +
          "wmZW+BVxBfdFjvDbARJwr9DL6YosyJE/7WD/Z5S5ER7e+51Ihg3ZR/iyoo31L2VC\n" +
          "xiDjGV/qbIQ8iLGJcB6VH1xNFp2qB/GJT6Xs4zS7OoBu9NGZIbc+DZxGW+C8eKfw\n" +
          "27LBcKS4eNpnjt6sBK4Qx5NehH+phOSlIjEhl9IZvLyL6WfpwB2CVnU31Pq7GRBk\n" +
          "QsJQpZEWYrO+qMdX6st23CNfdBBBPIKahhlvqgLPcN7zEyQb9mlRx0gaRZtoDRUu\n" +
          "VbqmZe8SNjiy3gYQlXiOlPpporMhMYfZWWZlsCeKZeB3Tfxwf7xwvK8l4QIDAQAB\n" +
          "o0gwRjAOBgNVHQ8BAf8EBAMCBaAwEwYDVR0lBAwwCgYIKwYBBQUHAwIwHwYDVR0j\n" +
          "BBgwFoAUbpu3sVbNdcTZikPjwe/BZvxOqgwwDQYJKoZIhvcNAQELBQADggEBAEtB\n" +
          "Of2+jBVfWOONtktsrAldwOHCkioi6vm6WEsXqT8mqy4sUEpoVYAjsDeTrbKXS3hm\n" +
          "B6scKQAXC0JOhjmPWHLwOu+OOi/jplBnDhANYjyReFUcXawSBSZq3mnYGOXA6om0\n" +
          "CgRfJszwoEJE6s6IrfphGhW1jIEhdvCetkwA9NdUHTZ5p8OyNp3GzzBXkp2g4rqq\n" +
          "B1RYrEw1PoM6U2AS1XiLEGNQ8FXXCg6hpmmNhmmDM9Z/JdsCz7jwT/sKERx83v9H\n" +
          "hN3YaDLMVSoFCWGNWmXOVu/4drJkQw2CHn7HQ+FwniUI2ymDnCzQ7UAgTiqIDsI9\n" +
          "sXJ8fCk7gaK5jVSnEH4=\n" +
          "-----END CERTIFICATE-----";

  private static final String clientKey =
          "-----BEGIN RSA PRIVATE KEY-----\n" +
          "MIIEpQIBAAKCAQEA27ZP93Z2XvBuvCm6ml0z/D7kqG/jwmZW+BVxBfdFjvDbARJw\n" +
          "r9DL6YosyJE/7WD/Z5S5ER7e+51Ihg3ZR/iyoo31L2VCxiDjGV/qbIQ8iLGJcB6V\n" +
          "H1xNFp2qB/GJT6Xs4zS7OoBu9NGZIbc+DZxGW+C8eKfw27LBcKS4eNpnjt6sBK4Q\n" +
          "x5NehH+phOSlIjEhl9IZvLyL6WfpwB2CVnU31Pq7GRBkQsJQpZEWYrO+qMdX6st2\n" +
          "3CNfdBBBPIKahhlvqgLPcN7zEyQb9mlRx0gaRZtoDRUuVbqmZe8SNjiy3gYQlXiO\n" +
          "lPpporMhMYfZWWZlsCeKZeB3Tfxwf7xwvK8l4QIDAQABAoIBAQDU9Pa5YucEyoJq\n" +
          "ev/VUvVqTwOOhzT5Xfx55T5xi+St0aJUfa0fnH+o0zbpCHAwA05K9YoToBp9Q0vi\n" +
          "M1YhPlrW2EWINNvt8j5sf91WZS3PSFVwiRP9G5fUjekaXfC7dJYQ+zFmWMozHV0R\n" +
          "ouzQgGJp94B2akE4kSCRr/blnLu3ayzCek7zPJCyAoXA5rwflBCx2mwe+WpgATo7\n" +
          "223qqpTrH/cv7odvfiZfujbliuKMop7cIa/PDqPYsgzzunAr+4QH8z++zWUrkeGP\n" +
          "OGpL6dwiKs6fkqE9IRieFc53jXmQAddBYHvo2yh5CxfPy4pZC4EeR6P/AKw8jdiq\n" +
          "8ClKl24JAoGBAPW3E63lFlbr0tlAwy6BscFDBaPARByv6SrPNrFQOKTfFQZwH7fH\n" +
          "hHdt2Dh/9RU80jMBIq9YeA6Eus8cYyrjPcK8YBwSMQdTqMxfBtLH0Mw2mxT3cxLL\n" +
          "/vDwN5sR3MCrHj9e0IeCEyebDX+A3kNjv1/lqHsly/pEOXDdsrPRPd4PAoGBAOTo\n" +
          "mr5D2XjttItSTn/pOVcSBgLEoZm8hfeC3XnDpjiEqf6WpxYw2w4gFEZjHtXyvEwx\n" +
          "8vMVs7xEZsrzJCr2KVoB0ClSlaCnv4tDvoFVhB7VcplPvgI63vL0Uxbp9Id74H5V\n" +
          "aDW4i1FkTP153y9+93JWrR9cLNlzkU+BofhUEa0PAoGBALJ514aBFxft1UpOuzcl\n" +
          "p2q8dvU5a3/22oNo3c4veeVv0ILLoI5KEtL464wmjWtusTOxCHOa7CGHkXVspi95\n" +
          "vducHqdlI02yG7LO91LVJrmelEjHcENf/38czRcLA3DDGoZnJUq4VZd4DXbmsQr8\n" +
          "Af7y/szsaFwRgDOWJP5t5uVLAoGAMUFz+rjy1LihftEpHxD6S6gPLw1MkJ4mXYoF\n" +
          "Siw1WjgBtEyKi5XbrQBuHG87tWHT7rlYw8HJuRsm20jSYYQyhbGaLyocFEPETZ04\n" +
          "IvBZy6C2VhL4mdY9Hx4/utCA055uQgDyuYyKPetNPcW5pqi1ah8IhZT5+ZiKjgcg\n" +
          "6SR94TUCgYEAxijB1kQUFjoq36+pqnP/42ZuPu4RM+5ThMen162ZWp3RodlKuZD7\n" +
          "g/VZ8eVMjNWwMElGxmdE6lSg5WzTqDQswuZo3u1gjbzpg+Ae1NPPg8e3fV5/4s5U\n" +
          "cYMaeinKkLIYUH50yqJwr669aQqAMui2FfDR2J3PTqeHCqMxJxueckc=\n" +
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
//        .withMasterUrl(properties.getEndpoint())
        .withNamespace(properties.getDefaultNamespace())
        .withConnectionTimeout(10000)
        .build();
  }

}

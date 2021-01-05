package si.fri.rso.kb6750.model3dcomments.api.v1.health;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.health.checks.KumuluzHealthCheck;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Liveness;

import si.fri.rso.kb6750.model3dcomments.config.RestProperties;


@Liveness
@ApplicationScoped
public class CustomHealthCheck extends KumuluzHealthCheck implements HealthCheck {

    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder healthCheckResponseBuilder = HealthCheckResponse.named(CustomHealthCheck.class.getSimpleName()).up();
        // System.out.println("NAme of the consul url: " + ConfigurationUtil.getInstance().get(name() + ".connection-url"));
        String connectionUrl = ConfigurationUtil.getInstance().get(name() + ".connection-url").orElse("");

        if (restProperties.getBroken()) {
            return HealthCheckResponse.down(CustomHealthCheck.class.getSimpleName());
        }
        else {
            checkConsulStatus(connectionUrl, healthCheckResponseBuilder);
            return healthCheckResponseBuilder.build();
        }
    }


    private static final String HEALTHY = "{\"health\":\"true\"}";
/*
    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder healthCheckResponseBuilder = HealthCheckResponse.named(EtcdHealthCheck.class.getSimpleName()).up();
        Optional<Integer> connectionUrls = ConfigurationUtil.getInstance().getListSize(name());

        if (connectionUrls.isPresent()) {
            for (int i = 0; i < connectionUrls.get(); i++) {
                String connectionUrl =
                        ConfigurationUtil.getInstance().get(name() + "[" + i + "].connection-url").orElse("");
                checkEtcdStatus(connectionUrl, healthCheckResponseBuilder);
            }
        } else {
            String connectionUrl = ConfigurationUtil.getInstance().get(name() + ".connection-url").orElse("");
            checkEtcdStatus(connectionUrl, healthCheckResponseBuilder);
        }

        return healthCheckResponseBuilder.build();
    }
    */
    /**
     * Helper method for checking if etcd is online.
     */
    private void checkConsulStatus(String connectionUrl, HealthCheckResponseBuilder healthCheckResponseBuilder) {
        WebTarget webTarget = ClientBuilder.newClient().target(connectionUrl);
        Response response = null;

        try {
            response = webTarget.request().get();

            if (response.getStatus() == 200) {
                // String result = response.readEntity(String.class).replaceAll("\\s+", "");
                // System.out.println("Consul ping result:" + response);
                //if (result != null && result.equals(HEALTHY)) {
                healthCheckResponseBuilder.withData(connectionUrl, HealthCheckResponse.State.UP.toString());
                return;
                //}
            }
        } catch (Exception exception) {
            System.out.println("Couldn't connect to Consul with exception: " + exception);
        } finally {
            if (response != null) {
                response.close();
            }
        }

        healthCheckResponseBuilder.withData(connectionUrl, HealthCheckResponse.State.DOWN.toString());
        healthCheckResponseBuilder.down();
    }

    @Override
    public String name() {
        return kumuluzBaseHealthConfigPath + "consul-health-check";
    }

    @Override
    public boolean initSuccess() {
        return true;
    }
}
package si.fri.rso.kb6750.model3dcomments.config;

import javax.enterprise.context.ApplicationScoped;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

@ConfigBundle("rest-properties")
@ApplicationScoped
public class RestProperties {

    @ConfigValue(watch = true)
    private Boolean maintenanceMode;
    @ConfigValue(watch = true)
    private Boolean broken;

    private String requestChainHeader;
    @ConfigValue(watch = true)
    private String parserServiceIp;

    public Boolean getMaintenanceMode() {
        return this.maintenanceMode;
    }

    public Boolean getBroken() {
        return this.broken;
    }

    public void setMaintenanceMode(final Boolean maintenanceMode) {
        this.maintenanceMode = maintenanceMode;
    }

    public void setBroken(final Boolean broken) {this.broken = broken; }

    public String getRequestChainHeader() {
        return this.requestChainHeader;
    }

    public void setRequestChainHeader(final String requestChainHeader) {
        this.requestChainHeader = requestChainHeader;
    }

    public String getParserServiceIp() {
        return parserServiceIp;
    }

    public void setParserServiceIp(String parserServiceIp) {
        this.parserServiceIp = parserServiceIp;
    }
}

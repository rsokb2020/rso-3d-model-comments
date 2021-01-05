package si.fri.rso.kb6750.model3dcomments.api.v1.filters;

import com.kumuluz.ee.logs.cdi.Log;
import si.fri.rso.kb6750.model3dcomments.config.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Log
@Provider
@ApplicationScoped
@PreMatching
public class PreMatchingHeaderFilter implements ContainerRequestFilter {

    @Inject
    private RestProperties restProperties;

    @Override
    public void filter(ContainerRequestContext ctx)  {
        System.out.println("Incoming headers: " + ctx.getHeaders().toString());

        String idFromHeader = ctx.getHeaderString("request-chain");

        if(idFromHeader == null){
            idFromHeader = UUID.randomUUID().toString();
        }
        restProperties.setRequestChainHeader(idFromHeader);
        ctx.getHeaders().add("request-chain", idFromHeader);

        System.out.println("model-3d-comments: Request-chain header set to: " + restProperties.getRequestChainHeader());
    }
}

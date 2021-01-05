package si.fri.rso.kb6750.model3dcomments.api.v1.interceptors;

import com.kumuluz.ee.common.config.EeConfig;
import com.kumuluz.ee.common.runtime.EeRuntime;
import com.kumuluz.ee.logs.cdi.Log;
import org.apache.logging.log4j.CloseableThreadContext;
import si.fri.rso.kb6750.model3dcomments.config.RestProperties;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;
import java.util.HashMap;

@Log
@Interceptor
@Priority(Interceptor.Priority.PLATFORM_BEFORE)
public class LogContextInterceptor {

    @Inject
    private RestProperties restProperties;

    @AroundInvoke
    public Object logMethodEntryAndExit(InvocationContext context) throws Exception {

        HashMap settings = new HashMap();

        settings.put("environmentType", EeConfig.getInstance().getEnv().getName());
        settings.put("applicationName", EeConfig.getInstance().getName());
        settings.put("applicationVersion", EeConfig.getInstance().getVersion());
        settings.put("uniqueInstanceId", EeRuntime.getInstance().getInstanceId());

        //Todo: z rest filetrjom nastavljal request HEADER zato da se po logih lahko sledi enemu requestu
        settings.put("uniqueRequestId", restProperties.getRequestChainHeader());

        System.out.println("model-3d-comments: uniqueRequestId set to: " + restProperties.getRequestChainHeader());

        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.putAll(settings)) {
            Object result = context.proceed();
            return result;
        }
    }
}
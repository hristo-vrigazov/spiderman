package test.chrome.replay;

import com.hribol.bromium.browsers.chrome.replay.ChromeDriverActionExecution;
import com.hribol.bromium.replay.execution.ExecutorBuilder;
import com.hribol.bromium.replay.execution.synchronization.SynchronizationEvent;
import com.hribol.bromium.replay.filters.ProxyFacade;
import com.hribol.bromium.replay.filters.ProxyFacadeSupplier;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriverService;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by hvrigazov on 08.05.17.
 */
public class ChromeDriverActionExecutionTest {

    @Test
    public void creatingExecution() throws IOException, URISyntaxException {
        ProxyFacade proxyFacade = mock(ProxyFacade.class);
        ExecutorBuilder executorBuilder = mock(ExecutorBuilder.class);
        when(executorBuilder.getProxyFacade()).thenReturn(proxyFacade);

        ChromeDriverActionExecution chromeDriverActionExecution = new ChromeDriverActionExecution(executorBuilder);
        assertNotNull(chromeDriverActionExecution);
        assertEquals(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, chromeDriverActionExecution.getSystemProperty());
        assertNotNull(chromeDriverActionExecution.createReplaySettings());
    }
}
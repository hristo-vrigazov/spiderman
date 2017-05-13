package tests.chrome.record;

import com.hribol.spiderman.browsers.chrome.record.ChromeRecordBrowser;
import org.junit.Test;
import org.openqa.selenium.chrome.ChromeDriverService;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hvrigazov on 08.05.17.
 */
public class ChromeRecordBrowserTest {

    @Test
    public void createsNotNullInstance() throws IOException {
        String pathToDriverExecutable = "chromedriver";
        String pathToJSInjectionFile = getClass().getResource("/eventsRecorder.js").getFile();

        ChromeRecordBrowser chromeRecordBrowser = new ChromeRecordBrowser(pathToDriverExecutable, pathToJSInjectionFile);

        assertNotNull(chromeRecordBrowser);
        assertNotNull(chromeRecordBrowser.getVisibleWebDriverSupplier());
        assertEquals(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, chromeRecordBrowser.getSystemProperty());
    }
}
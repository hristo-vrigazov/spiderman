package browser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.*;
import execution.DefaultApplicationActionFactory;
import execution.*;
import io.airlift.airline.*;
import io.airlift.airline.model.CommandMetadata;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import record.RecordBrowser;
import replay.ReplayBrowser;
import utils.Utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by hvrigazov on 14.03.17.
 */
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Cli.CliBuilder<Runnable> builder = Cli.<Runnable>builder("bot")
                .withDescription("Selenium record and replay bot for applications")
                .withDefaultCommand(Help.class)
                .withCommands(Help.class, Init.class);

        builder
                .withGroup("test")
                .withDescription("Commands related to test cases")
                .withDefaultCommand(Record.class)
                .withCommands(Record.class, Replay.class);

        Cli<Runnable> parser = builder.build();

        try {
            parser.parse(args).run();
        } catch (ParseOptionMissingException | ParseArgumentsUnexpectedException e) {

            final List<String> commandNames =
                    parser.getMetadata().getCommandGroups().stream()
                            .flatMap(cg -> cg.getCommands().stream().map(CommandMetadata::getName))
                            .collect(Collectors.toList());

            Help.help(parser.getMetadata(), commandNames);
        }
    }

    @Command(name = "record", description = "Opens up the browser and listens for application specific events")
    public static class Record implements Runnable {

        @Option(name = "-d", description = "Path to chrome driver executable", required = true)
        public String pathToChromedriver;

        @Option(name = "-j", description = "Path to js file to be injected into every response", required = true)
        public String pathToJSInjectionFile;

        @Option(name = "-u", description = "Base url of your application", required = true)
        public String baseUrl;

        @Option(name = "-o", description = "The output file, which will contain the test cases as JSON", required = true)
        public String outputFile;

        @Override
        public void run() {
            RecordBrowser recordBrowser = new RecordBrowser(pathToChromedriver, pathToJSInjectionFile);
            try {
                recordBrowser.record(baseUrl);
                System.out.println("Press Enter when finished recording");
                System.in.read();
                recordBrowser.dumpActions(outputFile);
            } catch (IOException | InterruptedException | URISyntaxException e) {
                e.printStackTrace();
            }

            recordBrowser.quit();

            System.exit(0);
        }
    }

    @Command(name = "replay", description = "Replays a test case")
    public static class Replay implements Runnable {

        @Option(name = "-d", description = "Path to chrome driver executable", required = true)
        public String pathToChromedriver;

        @Option(name = "-c", description = "Path to application configuration", required = true)
        public String pathToApplicationConfiguration;

        @Option(name = "-t", description = "Path to serialized test", required = true)
        public String pathToSerializedTest;

        @Override
        public void run() {
            try {
                ApplicationConfiguration applicationConfiguration =
                        Utils.parseApplicationConfiguration(pathToApplicationConfiguration);
                WebdriverActionExecutor chromeDriverActionExecutor =
                        new ChromeDriverActionExecutor(pathToChromedriver, false, 15, 50);
                WebdriverActionFactory predefinedWebdriverActionFactory = new PredefinedWebdriverActionFactory();
                ApplicationActionFactory applicationActionFactory =
                        new DefaultApplicationActionFactory("http://www.tenniskafe.com/", applicationConfiguration, predefinedWebdriverActionFactory);
                ReplayBrowser replayBrowser =
                        new ReplayBrowser(chromeDriverActionExecutor, applicationConfiguration, applicationActionFactory);

                replayBrowser.replay(pathToSerializedTest);
                replayBrowser.dumpAllMetrics("metrics.har", "metrics.csv");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }

            System.exit(0);
        }
    }

    @Command(name = "init", description = "Guides you through creating an automation layer for your application!")
    public static class Init implements Runnable {

        MainMenuChoice mainMenuChoice;
        ApplicationConfiguration applicationConfiguration;
        TextIO textIO;

        @Override
        public void run() {
            textIO = TextIoFactory.getTextIO();
            textIO.getTextTerminal().println("Welcome! I will guide you through creating an automation layer for you application");
            applicationConfiguration = new ApplicationConfiguration();

            textIO.getTextTerminal().println();
            applicationConfiguration.setApplicationName(textIO
                    .newStringInputReader()
                    .read("What is the name of your application?"));

            applicationConfiguration.setVersion(textIO
                    .newStringInputReader()
                    .withDefaultValue("0.0.1")
                    .read("What is the version of your application?"));

            textIO.getTextTerminal().println();
            textIO.getTextTerminal().println("Let's now define some actions and assertions!");
            textIO.getTextTerminal().println();
            textIO.getTextTerminal().println("The initial page loading is added by default");

            showAssertionActionChoice();

            try {
                String outputFilename = textIO
                        .newStringInputReader()
                        .read("Where should I save the configuration");
                Writer writer = new FileWriter(outputFilename);
                Gson gson = new GsonBuilder().create();
                gson.toJson(applicationConfiguration, writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.exit(0);
        }

        private void showAssertionActionChoice() {
            do {
                mainMenuChoice = textIO.newEnumInputReader(MainMenuChoice.class)
                        .read("Let's add another one! Choose from below");
                textIO.getTextTerminal().println();
                switch (mainMenuChoice) {
                    case ASSERTION:
                        textIO.getTextTerminal().println("Let's add an assertion!");
                        break;
                    case ACTION:
                        textIO.getTextTerminal().println("Let's add an action!");
                        showAddActionMenu();
                        break;
                    default:
                        break;
                }
            } while (!(mainMenuChoice == MainMenuChoice.SAVE_AND_EXIT));
        }

        private void showAddActionMenu() {
            String actionName = textIO.newStringInputReader().read("Action name: ");
            ApplicationActionConfiguration applicationActionConfiguration = new ApplicationActionConfiguration();
            applicationActionConfiguration.setName(actionName);

            WebdriverActionConfiguration preconditionConfiguration =
                    promptForActionConfigurationType("Precondition: ");
            applicationActionConfiguration.setConditionBeforeExecution(preconditionConfiguration);
            textIO.getTextTerminal().println();

            WebdriverActionConfiguration webdriverActionConfiguration =
                    promptForActionConfigurationType("Action: ");
            applicationActionConfiguration.setWebdriverAction(webdriverActionConfiguration);
            textIO.getTextTerminal().println();

            WebdriverActionConfiguration postconditionConfiguration =
                    promptForActionConfigurationType("Postaction: ");
            applicationActionConfiguration.setConditionAfterExecution(postconditionConfiguration);
            textIO.getTextTerminal().println();

            boolean expectsHttpRequest = textIO
                    .newBooleanInputReader()
                    .read("Expect HTTP request after the action?");

            applicationActionConfiguration.setExpectsHttpRequest(expectsHttpRequest);

            applicationConfiguration.addApplicationActionConfiguration(applicationActionConfiguration);

            textIO.getTextTerminal().println("Done! Awesome!");
            textIO.getTextTerminal().println();
        }

        private WebdriverActionConfiguration promptForActionConfigurationType(String prompt) {
            textIO.getTextTerminal().println(prompt);
            WebdriverActionConfiguration webdriverActionConfiguration = new WebdriverActionConfiguration();
            WebdriverActionType webdriverActionType = textIO
                    .newEnumInputReader(WebdriverActionType.class)
                    .read("Type: ");

            String webdriverAction = getWebdriverAction(webdriverActionType);

            Map<String, ParameterConfiguration> parameterConfigurations =
                    collectParametersConfiguration(webdriverActionType);
            webdriverActionConfiguration.setParametersConfiguration(parameterConfigurations);
            webdriverActionConfiguration.setWebdriverActionType(webdriverAction);

            return webdriverActionConfiguration;
        }

        private String getWebdriverAction(WebdriverActionType webdriverActionType) {
            if (webdriverActionType == WebdriverActionType.CUSTOM) {
                return textIO.newStringInputReader().read("Enter the custom type name: ");
            }

            return webdriverActionType.toString();
        }

        private Map<String, ParameterConfiguration> collectParametersConfiguration(WebdriverActionType webdriverActionType) {
            if (webdriverActionType == WebdriverActionType.NOTHING) {
                return new HashMap<>();
            }

            Map<String, ParameterConfiguration> parametersConfiguration = new HashMap<>();

            while (promptForAddParameters()) {
                ParameterConfiguration parameterConfiguration = promptForParameterConfiguration(webdriverActionType);
                parametersConfiguration.put(parameterConfiguration.getParameterName(), parameterConfiguration);
            }

            return parametersConfiguration;
        }

        private ParameterConfiguration promptForParameterConfiguration(WebdriverActionType webdriverActionType) {
            return getParameterConfigurationForCustom();
        }

        private ParameterConfiguration getParameterConfigurationForCustom() {
            textIO.getTextTerminal().println();
            ParameterConfiguration parameterConfiguration = new ParameterConfiguration();
            parameterConfiguration.setParameterName(promptForParameterName());
            return getParameterConfigurationForName(parameterConfiguration);
        }

        private ParameterConfiguration getParameterConfigurationForName(ParameterConfiguration parameterConfiguration) {
            parameterConfiguration.setExpose(
                    promptForParameterExposing(parameterConfiguration.getParameterName()));

            if (parameterConfiguration.isExposed()) {
                parameterConfiguration.setAlias(
                        promptForAlias(parameterConfiguration.getParameterName()));
                return parameterConfiguration;
            }

            parameterConfiguration.setValue(promptForValue(parameterConfiguration.getParameterName()));
            return parameterConfiguration;
        }

        private String promptForValue(String parameterName) {
            return textIO.newStringInputReader().read("Value of " + parameterName);
        }

        private String promptForAlias(String parameterName) {
            return textIO.newStringInputReader().read("Alias for " + parameterName);
        }

        private boolean promptForAddParameters() {
            textIO.getTextTerminal().println();
            return textIO.newBooleanInputReader().read("Add a parameter?");
        }

        private String promptForParameterName() {
            return textIO.newStringInputReader().read("Parameter name: ");
        }

        private boolean promptForParameterExposing(String parameterName) {
            return textIO.newBooleanInputReader().read("Should I expose " + parameterName);
        }
    }

    public enum MainMenuChoice {
        ASSERTION,
        ACTION,
        SAVE_AND_EXIT
    }
}

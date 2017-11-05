package com.hribol.bromium.common.generation.record.functions;

import com.hribol.bromium.common.builder.JsCollector;
import com.hribol.bromium.common.generation.helper.NameWebDriverActionConfiguration;
import com.hribol.bromium.common.generation.record.invocations.ClickClassByTextRecorderInvocation;
import com.hribol.bromium.common.generation.record.invocations.RecorderFunctionInvocation;
import com.hribol.bromium.core.config.WebDriverActionConfiguration;

import static com.hribol.bromium.common.builder.JsFunctionNames.CLICK_CLASS_BY_TEXT;
import static com.hribol.bromium.common.builder.JsFunctionNames.CLICK_CSS_SELECTOR;
import static com.hribol.bromium.common.replay.parsers.ClickClassByTextParser.INITIAL_COLLECTOR_CLASS;
import static com.hribol.bromium.core.utils.Constants.*;
import static com.hribol.bromium.core.utils.Constants.EVENT_NAME;
import static com.hribol.bromium.core.utils.Constants.PARAMETERS;
import static com.hribol.bromium.core.utils.JsEvents.CLICK;

/**
 * Created by hvrigazov on 04.11.17.
 */
public class ClickClassByTextRecorderFunction implements RecorderFunction {

    private String javascriptCode;

    public ClickClassByTextRecorderFunction(JsCollector jsCollector) {
        this.javascriptCode = jsCollector
                .declareFunction(CLICK_CLASS_BY_TEXT)
                    // text in here means the alias for text
                    // TODO: write alternative function for the case when the text is not exposed
                    .withParameters(INITIAL_COLLECTOR_CLASS, TEXT, EVENT_NAME)
                    .startBody()
                        .whenCssSelectorArrives("\".\" + " + INITIAL_COLLECTOR_CLASS)
                            .attachListenerForEvent(CLICK)
                                .startCollectingParameters(PARAMETERS)
                                .parameterWithConstantKey(EVENT, EVENT_NAME)
                                .parameter(TEXT, INNER_TEXT)
                            .buildParameters()
                            .notifyBromium(PARAMETERS)
                            .endListener()
                        .endArriveHandler()
                    .endBody()
                .build();
    }

    @Override
    public String getJavascriptCode() {
        return javascriptCode;
    }

    @Override
    public RecorderFunctionInvocation getInvocation(NameWebDriverActionConfiguration generationInformation) {
        String eventName = generationInformation.getEventName();
        WebDriverActionConfiguration webDriverActionConfiguration = generationInformation.getWebDriverActionConfiguration();

        String initialCollectorClass = webDriverActionConfiguration
                .getParametersConfiguration()
                .get(INITIAL_COLLECTOR_CLASS)
                .getValue();

        String aliasText = webDriverActionConfiguration
                .getParametersConfiguration()
                .get(TEXT)
                .getAlias();

        return new ClickClassByTextRecorderInvocation(initialCollectorClass, aliasText, eventName);
    }
}
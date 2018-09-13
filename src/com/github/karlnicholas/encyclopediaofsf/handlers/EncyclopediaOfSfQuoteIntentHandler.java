/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.github.karlnicholas.encyclopediaofsf.handlers;

import com.amazon.ask.dispatcher.request.handler.HandlerInput;
import com.amazon.ask.dispatcher.request.handler.RequestHandler;
import com.amazon.ask.model.Response;

import esf.lucene.SearchResult;
import quote.GetQuote;

import java.io.IOException;
import java.util.Optional;

import static com.amazon.ask.request.Predicates.intentName;

public class EncyclopediaOfSfQuoteIntentHandler implements RequestHandler {
    private GetQuote getQuote = new GetQuote();

    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("GetQuote"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {

    	/*		
		SearchResult searchResult;
		try {
			searchResult = getQuote.getRandomQuote();
		} catch (ParseException | IOException e) {
			throw new SpeechletException(e);
		}
		if ( searchResult.preamble.isEmpty()) {
			SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
			outputSpeech.setSsml("<speak>There is a problem connecting to the Encyclopedia of Philosophy at this time."
					+ " Please try again later.</speak>");
			return SpeechletResponse.newTellResponse(outputSpeech);
		}
		log.info("Intent = GetQuote: " + searchResult.subject);
		SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
		outputSpeech.setSsml("<speak>Random entry for "+searchResult.subject+"<break strength=\"x-strong\"/>" + searchResult.preamble + "</speak>");
		SimpleCard card = new SimpleCard();
		card.setTitle("Encyclopedia of Philosophy");
		card.setContent(searchResult.subject+"\n"+searchResult.url+"\n"+searchResult.preamble);
		SpeechletResponse speechletResponse = SpeechletResponse.newTellResponse(outputSpeech);
		speechletResponse.setCard(card);
		return speechletResponse;
*/    	
//    	
    	String speechText;
		SearchResult searchResult = null ;
		try {
			searchResult = getQuote.getRandomQuote();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if ( searchResult == null || searchResult.preamble.isEmpty()) {
			speechText = "There is a problem connecting to the Encyclopedia of Science Fiction at this time."
					+ " Please try again later.";
		} else {
			speechText = "Random entry for "+searchResult.subject+". " + searchResult.preamble;
			
		}
        String repromptText =
                "You can search for another phrase or a quote.";				

        return input.getResponseBuilder()
                .withSpeech(speechText)
                .withSimpleCard("Encyclopedia Of Science Fiction",  "http://www.sf-encyclopedia.com" + searchResult.url + " " + speechText)
                .withShouldEndSession(false)
                .withReprompt(repromptText)
                .build();
    }
}

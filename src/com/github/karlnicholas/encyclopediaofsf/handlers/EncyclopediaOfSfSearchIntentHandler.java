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
import com.amazon.ask.model.Intent;
import com.amazon.ask.model.IntentRequest;
import com.amazon.ask.model.Request;
import com.amazon.ask.model.Response;
import com.amazon.ask.model.Slot;

import esf.lucene.SearchFiles;
import esf.lucene.SearchResult;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.lucene.queryparser.classic.ParseException;

import static com.amazon.ask.request.Predicates.intentName;

public class EncyclopediaOfSfSearchIntentHandler implements RequestHandler {
    private static final String SLOT_SEARCH_PHRASE = "phrase";
    private SearchFiles searchFiles = new SearchFiles();
    @Override
    public boolean canHandle(HandlerInput input) {
        return input.matches(intentName("SearchIntent"));
    }

    @Override
    public Optional<Response> handle(HandlerInput input) {
/*
        // add a player to the current game,
        // terminate or continue the conversation based on whether the intent
        // is from a one shot command or not.
        String searchPhrase = intent.getSlot(SLOT_SEARCH_PHRASE).getValue();
        if (searchPhrase == null || searchPhrase.isEmpty()) {
			SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
			outputSpeech.setSsml("<speak>Sorry, I didn't understand that word or phrase.</speak>");
			log.info("Didn't understand the search phrase: ");
			return SpeechletResponse.newTellResponse(outputSpeech);
        }

		try {
			List<SearchResult> searchResults = searchFiles.query(searchPhrase);
			if ( searchResults.size() > 0 ) {
				// Create the plain text output
				SearchResult searchResult = searchResults.get(0);
				
				SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
				outputSpeech.setSsml("<speak>Results for \"" + searchPhrase +"\".<break strength=\"x-strong\"/>" + searchResults.get(0).preamble + "</speak>");
				log.info("Search found for " + searchPhrase + ". Result: " + searchResults.get(0).subject + " at " + searchResults.get(0).url);
				SimpleCard card = new SimpleCard();
				card.setTitle("Encyclopedia of Philosophy");
				card.setContent(searchResult.subject+searchResult.url+"\n"+searchResult.preamble);
				SpeechletResponse speechletResponse = SpeechletResponse.newTellResponse(outputSpeech);
				speechletResponse.setCard(card);
				return speechletResponse;
			} else {
				SsmlOutputSpeech outputSpeech = new SsmlOutputSpeech();
				outputSpeech.setSsml("<speak>Sorry, nothing found for " + searchPhrase + ".</speak>");
				log.info("No results for " + searchPhrase);
				return SpeechletResponse.newTellResponse(outputSpeech);
			}
		} catch (ParseException | IOException e) {
			throw new SpeechletException(e);
		}
*/    	
//    	
        Request request = input.getRequestEnvelope().getRequest();
        IntentRequest intentRequest = (IntentRequest) request;
        Intent intent = intentRequest.getIntent();
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot searchPhraseSlot = slots.get(SLOT_SEARCH_PHRASE);

        String speechText;
        boolean good = false;
        String url = null;

        // Check for favorite color and create output to user.
        if (searchPhraseSlot != null) {
            // Store the user's favorite color in the Session and create response.
            String searchPhrase = searchPhraseSlot.getValue();

			List<SearchResult> searchResults = null;
			try {
				searchResults = searchFiles.query(searchPhrase);
			} catch (ParseException | IOException e) {
				e.printStackTrace();
			}
			if ( searchResults != null && searchResults.size() > 0 ) {
				// Create the plain text output				
	            speechText = "Results for " + searchPhrase +". " + searchResults.get(0).preamble;
	            good = true;
	            url = searchResults.get(0).url;
			} else {
	            speechText = "Sorry, nothing found for " + searchPhrase;
			}

        } else {
            // Render an error since we don't know what the users favorite color is.
            speechText = "I didn't understand that.";
        }

        String repromptText =
                "You can search for another phrase or a quote.";				

        if ( good ) {
            return input.getResponseBuilder()
                    .withSpeech(speechText)
                    .withSimpleCard("Encyclopedia Of Science Fiction",  "http://www.sf-encyclopedia.com" + url + " " + speechText)
                    .withShouldEndSession(false)
                    .withReprompt(repromptText)
                    .build();
        } else {
	        return input.getResponseBuilder()
	                .withSpeech(speechText)
	                .withShouldEndSession(false)
	                .withReprompt(repromptText)
	                .build();
        }
    }

}

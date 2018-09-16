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
	            url = searchResults.get(0).url;
	            good = true;
			} else {
	            speechText = "Sorry, nothing found for " + searchPhrase + ". You can search for another entry or ask for a quote.";
			}

        } else {
            // Render an error since we don't know what the users favorite color is.
            speechText = "Sorry, I didn't understand that. You can search for another entry or ask for a quote.";
        }

        if ( good ) {
            return input.getResponseBuilder()
                    .withSpeech(speechText + "<p>You can search again or ask for a quote.</p>")
                    .withSimpleCard("Encyclopedia Of Science Fiction",  "http://www.sf-encyclopedia.com" + url + "\n" + speechText)
                    .withReprompt("You can search for another entry or ask for a quote, or stop.")
                    .withShouldEndSession(false)
                    .build();
        } else {
	        return input.getResponseBuilder()
	                .withSpeech(speechText)
                    .withReprompt("You can search for another entry or ask for a quote or stop.")
                    .withShouldEndSession(false)
	                .build();
        }
    }

}

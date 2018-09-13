/*
     Copyright 2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.

     Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file
     except in compliance with the License. A copy of the License is located at

         http://aws.amazon.com/apache2.0/

     or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
     the specific language governing permissions and limitations under the License.
*/

package com.github.karlnicholas.encyclopediaofsf;

import com.amazon.ask.Skill;
import com.amazon.ask.SkillStreamHandler;
import com.amazon.ask.Skills;
import com.github.karlnicholas.encyclopediaofsf.handlers.CancelandStopIntentHandler;
import com.github.karlnicholas.encyclopediaofsf.handlers.FallbackIntentHandler;
import com.github.karlnicholas.encyclopediaofsf.handlers.HelpIntentHandler;
import com.github.karlnicholas.encyclopediaofsf.handlers.LaunchRequestHandler;
import com.github.karlnicholas.encyclopediaofsf.handlers.EncyclopediaOfSfSearchIntentHandler;
import com.github.karlnicholas.encyclopediaofsf.handlers.SessionEndedRequestHandler;
import com.github.karlnicholas.encyclopediaofsf.handlers.EncyclopediaOfSfQuoteIntentHandler;

public class EncyclopediaOfSfStreamHandler extends SkillStreamHandler {

    private static Skill getSkill() {
        return Skills.standard()
                .addRequestHandlers(
                        new EncyclopediaOfSfQuoteIntentHandler(),
                        new EncyclopediaOfSfSearchIntentHandler(),
                        new LaunchRequestHandler(),
                        new CancelandStopIntentHandler(),
                        new SessionEndedRequestHandler(),
                        new HelpIntentHandler(),
                        new FallbackIntentHandler())
                // Add your skill id below
                .withSkillId("amzn1.ask.skill.000cb015-f612-4f2a-a83f-7d24090d136c")
                .build();
    }

    public EncyclopediaOfSfStreamHandler() {
        super(getSkill());
    }

}

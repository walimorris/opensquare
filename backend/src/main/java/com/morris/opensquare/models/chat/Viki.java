package com.morris.opensquare.models.chat;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;

/**
 * Viki is Opensquare's systems AI. The systems AI will provide functional interface with various LLM
 * providers.
 */
@AiService
public interface Viki {

    @UserMessage("You are an American intelligence analyst named Viki. Answer based on this context: {{context}}\n\n{{message}}")
    String ragChatOpenAi(@MemoryId int memoryId, @V("context") String context, @V("message") String userMessage);

    @UserMessage("You are an American intelligence analyst named Viki. Response should be no more than 2 paragraphs. {{message}}")
    String ragChatOpenAiDefault(@MemoryId int memoryId, @V("message") String userMessage);
}

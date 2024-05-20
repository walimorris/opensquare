package com.morris.opensquare.services.impl;

import com.morris.opensquare.models.youtube.YouTubeRagChainProperties;
import com.morris.opensquare.services.OpenAiService;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OpenAiServiceImpl implements OpenAiService {

    @Override
    public String processYouTubeRAGChain(YouTubeRagChainProperties youTubeRagChainProperties) {
        // need to initialize the vector store
        EmbeddingModel embeddingModel = new OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder()
                .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_ADA_002)
                .apiKey(youTubeRagChainProperties.getOpenaiKey())
                .maxRetries(2)
                .build();

        // MongoDB is our vector store, YoutubeRAGChainProperties hold a reference to this store.
        // We will use langchain4j conversational retrieval chain to execute questions based on
        // the content in our mongodb vector database (embedding store)
        EmbeddingStoreContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(youTubeRagChainProperties.getVectorStore())
                .build();

        // Create a prompt template giving the context and question (embed the question for relevant answers)
        String question = youTubeRagChainProperties.getPrompt();
        Embedding questionEmbedding = embeddingModel.embed(question).content();
        List<EmbeddingMatch<TextSegment>> relevantEmbeddings = youTubeRagChainProperties.getVectorStore()
                .findRelevant(questionEmbedding, 10, 0.7);

        // context gives our model the most relevant data to answer questions
        String context = relevantEmbeddings.stream()
                .map(match -> match.embedded().text())
                .collect(Collectors.joining("\n\n"));

        Map<String, String> promptVariables = new HashMap<>();
        promptVariables.put("question", question);
        promptVariables.put("context", context);

        PromptTemplate promptTemplate = PromptTemplate.from(
                        """
                        Answer the question based only on the following context, your name is Viki: \
                        {{context}}
                        
                        Question: {{question}}
                        """
        );
        Prompt prompt = promptTemplate.apply(promptVariables);

        // given the prompt with the added context and user question, we can now prompt our model
        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
                .apiKey(youTubeRagChainProperties.getOpenaiKey())
                .timeout(Duration.ofSeconds(60))
                .build();
        return chatLanguageModel.generate(prompt.toUserMessage()).content().text();
    }

    @Override
    public List<Float> processOpenAiAda002TextEmbedding(String key, String text) {

        // Create the embedding model used to vectorize the input text. This vectorization
        // process is used to perform similarity searches in RAG
        EmbeddingModel embeddingModel = new OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder()
                .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_ADA_002)
                .apiKey(key)
                .maxRetries(2)
                .build();

        // Now we get this embeddings in vector form and return that vector
        return embeddingModel.embed(text).content().vectorAsList();
    }
}

package com.morris.opensquare.services.impl;

import com.morris.opensquare.configurations.ApplicationPropertiesConfiguration;
import com.morris.opensquare.models.ai.VisionPulse;
import com.morris.opensquare.models.chat.Viki;
import com.morris.opensquare.models.youtube.YouTubeRagChainProperties;
import com.morris.opensquare.models.youtube.YouTubeVideo;
import com.morris.opensquare.services.OpenAiService;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.image.Image;
import dev.langchain4j.data.message.Content;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModelName;
import dev.langchain4j.model.openai.OpenAiImageModel;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

import static dev.ai4j.openai4j.image.ImageModel.DALL_E_QUALITY_HD;

@Service
public class OpenAiServiceImpl implements OpenAiService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenAiServiceImpl.class);

    private final ApplicationPropertiesConfiguration configuration;
    private final YouTubeServiceImpl youTubeService;

    private static final String GPT_4o = "gpt-4o";
    private static final String DALL_E_2 = "dall-e-2";

    @Autowired
    public OpenAiServiceImpl(ApplicationPropertiesConfiguration configuration, YouTubeServiceImpl youTubeService) {
        this.configuration = configuration;
        this.youTubeService = youTubeService;
    }

    @Override
    public String processYouTubeRAGChain(YouTubeRagChainProperties youTubeRagChainProperties) {

        // We will use initialize and use the ADA_002 to create embeddings on transcripts
        EmbeddingModel embeddingModel = new OpenAiEmbeddingModel.OpenAiEmbeddingModelBuilder()
                .modelName(OpenAiEmbeddingModelName.TEXT_EMBEDDING_ADA_002)
                .apiKey(youTubeRagChainProperties.getOpenaiKey())
                .maxRetries(2)
                .build();

        InMemoryEmbeddingStore<TextSegment> inMemoryEmbeddingStore = new InMemoryEmbeddingStore<>();

        // we can use youtube service to get most relevant search results in MDB vector
        List<YouTubeVideo> videosSearchResult = youTubeService.getYouTubeVideosFromVectorSearch(
                youTubeRagChainProperties.getOpenaiKey(), youTubeRagChainProperties.getPrompt()
        );

        videosSearchResult.forEach(youTubeVideo -> {
            TextSegment textSegment = TextSegment.from(youTubeVideo.getTranscript());
            Embedding embedding = embeddingModel.embed(textSegment).content();
            inMemoryEmbeddingStore.add(embedding, textSegment);
        });

        // use our embedding model and in memory store to as retriever for optimal answer
        EmbeddingStoreContentRetriever retriever = EmbeddingStoreContentRetriever.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(inMemoryEmbeddingStore)
                .build();

        String question = youTubeRagChainProperties.getPrompt();

        // given the prompt with the added context and user question, we can now build our model
        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
                .apiKey(youTubeRagChainProperties.getOpenaiKey())
                .modelName(GPT_4o)
                .timeout(Duration.ofSeconds(60))
                .build();

        // using our AI system interface build the prompt
        Viki viki = AiServices.builder(Viki.class)
                .chatLanguageModel(chatLanguageModel)
                .contentRetriever(retriever)
                .build();

        return viki.ragChatOpenAiDefault(1, question);
    }

    @Override
    public String processVisionPulse(VisionPulse visionPulse) {
        // GPT-4o has Vision capabilities - we init this model
        ChatLanguageModel chatLanguageModel = OpenAiChatModel.builder()
                .modelName(GPT_4o)
                .apiKey(configuration.openAI())
                .timeout(Duration.ofSeconds(60))
                .maxRetries(2)
                .build();

        TextContent textContent = new TextContent(visionPulse.getText());

        TextContent metadataTextContent = new TextContent("\nUse this data as extra context: " +
                visionPulse.getMetaData().toString());
        ImageContent imageContent = new ImageContent(visionPulse.getImageUrl());
        List<Content> chatContent = List.of(textContent, metadataTextContent, imageContent);

        // We create a user message to pass vision/text content
        UserMessage userMessage = new UserMessage(chatContent);
        return chatLanguageModel.generate(userMessage).content().text();
    }

    @Override
    public String generateImage(VisionPulse visionPulse) {
        OpenAiImageModel imageModel = OpenAiImageModel.builder()
                .apiKey(configuration.openAI())
                .modelName(DALL_E_2)
                .quality(DALL_E_QUALITY_HD)
                .build();

        Image image = Image.builder()
                .base64Data(visionPulse.getImageUrl())
                .build();

        Response<Image> response = imageModel.edit(image, visionPulse.getText());
        return response.content().url().toString();
    }
}

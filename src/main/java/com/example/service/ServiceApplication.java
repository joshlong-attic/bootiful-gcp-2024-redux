package com.example.service;

//import com.google.cloud.spring.data.spanner.core.mapping.Table;
//import com.google.cloud.spring.data.spanner.repository.SpannerRepository;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.spring.data.spanner.core.mapping.Table;
import com.google.cloud.spring.data.spanner.repository.SpannerRepository;
import com.google.cloud.spring.pubsub.core.publisher.PubSubPublisherTemplate;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.List;

@SpringBootApplication
public class ServiceApplication {

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}

@Configuration
@Profile("vision")
class VisionDemoConfiguration {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean
    ImageAnnotatorClient imageAnnotatorClient(CredentialsProvider cp) throws Exception {
        var build = ImageAnnotatorSettings
                .newBuilder()
                .setCredentialsProvider(cp)
                .build();
        return ImageAnnotatorClient.create(build);
    }

    @Bean
    ApplicationRunner visionDemo(
            @Value("gs://bootiful-gcp-2024-redux/peanut.jpg") Resource dog,
            ImageAnnotatorClient imageAnnotatorClient) {
        return (arguments) -> {
            var catBytes = FileCopyUtils.copyToByteArray(dog.getInputStream());
            var build = AnnotateImageRequest
                    .newBuilder()
                    .addFeatures(Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION))
                    .setImage(Image.newBuilder().setContent(ByteString.copyFrom(catBytes)))
                    .build();
            var response = imageAnnotatorClient.batchAnnotateImages(List.of(build));
            this.log.info(response.toString());
        };
    }
}

@Profile("pubsub")
@Configuration
class PubsubDemoConfiguration {

    @Bean
    ApplicationRunner pubsubDemo(PubSubPublisherTemplate publisher, PubSubSubscriberTemplate subscriber) {
        return arguments -> {
            var log = LoggerFactory.getLogger(getClass());
            subscriber.subscribe("reservations-sub", msg -> {
                var data = msg.getPubsubMessage().getData();
                var stringUtf8 = data.toStringUtf8();
                log.info("message: {}", stringUtf8);
                msg.ack();
            });
            publisher.publish("reservations", "Hello @ " + Instant.now().toString());
        };
    }
}

@Profile("gemini")
@Configuration
class GeminiDemoConfiguration {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder.build();
    }

    @Bean
    ApplicationRunner geminiDemo(ChatClient client) {
        var log = LoggerFactory.getLogger(getClass());
        return arguments -> {
            var content = client
                    .prompt("tell me a joke?")
                    .call()
                    .content();
            log.info("content [{}]", content);
        };
    }
}

@Configuration
class SpannerDemoConfiguration {

    @Bean
    ApplicationRunner spannerDemo(DogRepository repository) {
        return args -> {
            var log = LoggerFactory.getLogger(getClass());
            repository.findAll().forEach(d -> log.info(d.toString()));
        };
	}
}

interface DogRepository extends SpannerRepository<Dog, String> {
}

@Table(name = "dog")
record Dog(@Id String id, String name) {
}
 
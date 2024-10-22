# Notes on the presentation 

## intro 
* you need to specify your basic gcp key and project and the like.
* mention that I have this stuff in environment variables so it's already done for my application. forgive me for not leaking my API credentials. just trust that it's done.

## spanner 
* add `com.google.cloud`:`spring-cloud-gcp-starter-data-spanner`
* `spring.cloud.gcp.spanner.database=pooch-palace`
* `spring.cloud.gcp.spanner.instance-id=pooch-palace`
* create a `public` `Dog` (with a `String` `id`!) and `DogRepository` 
* then just enumerate the dogs!
* so many good reasons to love Spanner. But it's not your only choice! Quite the contrary. Some, you'll be pleased to know, are cheap. You can use at least two other different configurations of PostgreSQL, for example. 

## vision and storage
* add `com.google.cloud`:`spring-cloud-gcp-vision`
* add `com.google.cloud`:`spring-cloud-gcp-starter-storage`
* let's say we want to automatically generate usseful descriptions of the doggos, we could use Google Cloud Vision for that. one option is that ive got doggo photos stored in google cloud. like for example here's one of my terrible, terrible dog, Peanut.
* `gs://bootiful-gcp-2024-redux/peanut.jpg`  
* inject `ImageAnnotatorClient` 
* 

## pubsub
* subscribe: `dog-adoptions-sub`
* publish: `dog-adoptions`
* don't forget to do `.ack()`!

## gemini 
* 
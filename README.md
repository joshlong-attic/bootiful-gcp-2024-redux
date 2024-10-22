# Notes on the presentation 

## intro 
* you need to specify your basic gcp key and project and the like.

## spanner 
* `spring.cloud.gcp.spanner.database=pooch-palace`
* `spring.cloud.gcp.spanner.instance-id=pooch-palace`
* create a `public` `Dog` (with a `String` `id`!) and `DogRepository` 
* then just enumerate the dogs!


## vision and storage
* let's say we want to automatically generate usseful descriptions of the doggos, we could use Google Cloud Vision for that. one option is that ive got doggo photos stored in google cloud. like for example here's one of my terrible terrible dog, Peanut.
* `gs://bootiful-gcp-2024-redux/peanut.jpg`  
* 

## pubsub
* subscribe: `dog-adoptions-sub`
* publish: `dog-adoptions`
* don't forget to do `.ack()`!
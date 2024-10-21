package com.example.service;

import com.google.cloud.spring.data.spanner.repository.SpannerRepository;

public interface DogRepository extends SpannerRepository<Dog, String> {
}

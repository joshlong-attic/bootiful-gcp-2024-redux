package com.example.service;

import com.google.cloud.spring.data.spanner.core.mapping.Table;
import org.springframework.data.annotation.Id;

@Table(name = "dog")
 public record Dog(@Id String id, String name) {
}

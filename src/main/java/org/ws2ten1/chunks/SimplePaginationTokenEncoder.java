/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ws2ten1.chunks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simple JSON and Base64 URL implementation for {@link PaginationTokenEncoder}.
 *
 * @since 0.24
 * @version $Id$
 * @author daisuke
 */
@Slf4j
public class SimplePaginationTokenEncoder implements PaginationTokenEncoder {
	
	private static final String FIRST_KEY = "first_key";
	
	private static final String LAST_KEY = "last_key";
	
	@Getter
	@Setter
	@NonNull
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Getter
	@Setter
	@NonNull
	private Encoder encoder = Base64.getUrlEncoder().withoutPadding();
	
	@Getter
	@Setter
	@NonNull
	private Decoder decoder = Base64.getUrlDecoder();
	
	
	@Override
	public String encode(Object firstKey, Object lastKey) {
		Map<String, Object> map = new LinkedHashMap<>(2);
		map.put(FIRST_KEY, firstKey);
		map.put(LAST_KEY, lastKey);
		try {
			String json = objectMapper.writeValueAsString(map);
			return encoder.encodeToString(json.getBytes(StandardCharsets.UTF_8));
		} catch (JsonProcessingException e) {
			throw new InvalidKeyExpressionException(e);
		}
	}
	
	@Override
	public <T> Optional<T> extractFirstKey(String paginationToken, Class<T> clazz) {
		return extractKey(paginationToken, FIRST_KEY, clazz);
	}
	
	@Override
	public Optional<String> extractFirstKey(String paginationToken) {
		return extractKey(paginationToken, FIRST_KEY, String.class);
	}
	
	@Override
	public <T> Optional<T> extractLastKey(String paginationToken, Class<T> clazz) {
		return extractKey(paginationToken, LAST_KEY, clazz);
	}
	
	@Override
	public Optional<String> extractLastKey(String paginationToken) {
		return extractKey(paginationToken, LAST_KEY, String.class);
	}
	
	<T> Optional<T> extractKey(String paginationToken, String key, Class<T> clazz) {
		if (paginationToken == null) {
			return Optional.empty();
		}
		try {
			byte[] json = decoder.decode(paginationToken);
			JsonNode keyNode = objectMapper.readTree(json).path(key);
			T object = objectMapper.treeToValue(keyNode, clazz);
			return Optional.ofNullable(object);
		} catch (IOException | IllegalArgumentException e) {
			log.warn("Invalid pagination token: {}", paginationToken);
		}
		return Optional.empty();
	}
}

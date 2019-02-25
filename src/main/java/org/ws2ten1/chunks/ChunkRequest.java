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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import org.springframework.data.domain.Sort.Direction;

/**
 * Basic Java Bean implementation of {@code Chunkable}.
 */
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class ChunkRequest implements Chunkable {
	
	@Getter
	private String paginationToken;
	
	@Getter
	private PaginationRelation paginationRelation;
	
	@Getter
	private Integer maxPageSize;
	
	@Getter
	private Direction direction;
	
	
	/**
	 * Create instance.
	 *
	 * @param paginationToken the token
	 */
	public ChunkRequest(String paginationToken) {
		this(paginationToken, PaginationRelation.NEXT, null, Direction.ASC);
	}
	
	/**
	 * Create instance.
	 *
	 * @param maxPageSize max size
	 */
	public ChunkRequest(Integer maxPageSize) {
		this(null, null, maxPageSize, Direction.ASC);
	}
	
	/**
	 * Create instance.
	 *
	 * @param paginationToken the token
	 * @param maxPageSize max size
	 */
	public ChunkRequest(String paginationToken, Integer maxPageSize) {
		this(paginationToken, PaginationRelation.NEXT, maxPageSize, Direction.ASC);
	}
	
	/**
	 * Create instance.
	 *
	 * @param direction the sort direction
	 */
	public ChunkRequest(Direction direction) {
		this(null, null, null, direction);
	}
	
	/**
	 * Create instance.
	 *
	 * @param paginationToken the token
	 * @param direction the sort direction
	 */
	public ChunkRequest(String paginationToken, Direction direction) {
		this(paginationToken, PaginationRelation.NEXT, null, direction);
	}
	
	/**
	 * Create instance.
	 *
	 * @param maxPageSize max size
	 * @param direction the sort direction
	 */
	public ChunkRequest(Integer maxPageSize, Direction direction) {
		this(null, null, maxPageSize, direction);
	}
}

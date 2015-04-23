package com.skplanet.openapi.request.inbound;

public interface InBoundRequestHandler<T> {
	public T readValue(String data) throws Exception;
}

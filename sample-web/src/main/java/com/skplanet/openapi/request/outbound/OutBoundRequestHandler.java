package com.skplanet.openapi.request.outbound;

public interface OutBoundRequestHandler<D,R> {
	public R handle(D data);
}

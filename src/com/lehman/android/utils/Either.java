package com.lehman.android.utils;

public interface Either<T> {
	public Boolean isSuccess();
	public Boolean isFailure();
	public Throwable getError();
	public T getObject();
}

package com.lehman.android.utils;

public class Success<T> implements Either<T>{
	private T obj;
	public Success(T obj){
		this.obj = obj;
	}
	@Override
	public Boolean isSuccess() {
		return true;
	}

	@Override
	public Boolean isFailure() {
		return false;
	}

	@Override
	public T getObject() {
		return obj;
	}
	@Override
	public Throwable getError() {
		return null;
	}
}

package com.lehman.android.utils;

public class Failure<T> implements Either<T>{
	private Throwable e;
	public Failure(Throwable e){
		this.e = e;
	}
	@Override
	public Boolean isSuccess() {
		return false;
	}

	@Override
	public Boolean isFailure() {
		return true;
	}

	@Override
	public T getObject() {
		return null;
	}
	@Override
	public Throwable getError() {
		return e;
	}

}

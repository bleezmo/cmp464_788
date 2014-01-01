package com.lehman.android;

import com.lehman.android.utils.Either;

public interface CoolMessageCallback{
	public void onCoolMessageReceived(Either<String> coolMessage);
	public void onIdReceived(Either<String> id);
}

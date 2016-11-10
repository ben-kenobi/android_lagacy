package com.icanit.app.action;

import com.icanit.app.exception.AppException;

public interface BaseAction {
	void execute() throws AppException;
}

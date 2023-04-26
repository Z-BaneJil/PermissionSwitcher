package io.subtoqz.permission_switcher.exception;

public class CommandNotPreparedException extends RuntimeException{
    public CommandNotPreparedException() {
        super("no CommandExecutor was found");
    }
}

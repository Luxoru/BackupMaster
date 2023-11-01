package me.des.backupmaster.util.array;

public class NoMultiverseEnabledException extends Throwable {

    @Override
    public String getMessage() {
        return "Multiverse is not found in the plugin folder!";
    }

    @Override
    public synchronized Throwable getCause() {
        return new NoClassDefFoundError();
    }
}

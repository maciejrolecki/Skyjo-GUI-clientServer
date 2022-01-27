package skyjo.launcher;

import skyjo.server.motherServer.PrimaryServer;

/**
 * Main function for the server
 */
public class LauncherSrv {
    public static void main(String[] args) {
        var primaryServer = new PrimaryServer("Application -Mother Server-");
        primaryServer.start();
    }
}

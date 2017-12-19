package freerails.network;

import junit.framework.TestCase;

/**
 * Test cases that use EchoGameServer should extend this class.
 *
 */
public abstract class AbstractEchoGameServerTestCase extends TestCase {
    final String ipAddress = "127.0.0.1";
    InetConnectionAccepter server;
    EchoGameServer echoGameServer;

    /**
     *
     * @throws Exception
     */
    @Override
    protected synchronized void setUp() throws Exception {
        echoGameServer = EchoGameServer.startServer();

        /*
         * There was a problem that occurred intermittently when the unit tests
         * were run as a batch. I think it was to do with reusing ports in quick
         * succession. Passing 0 as the port allow us to listen on an
         * unspecified port whose number we obtain by calling getLocalPort().
         * This making this change, the problem has not occurred.
         */
        server = new InetConnectionAccepter(0, echoGameServer);

        Thread serverThread = new Thread(server);
        serverThread.start();
    }

    /**
     *
     * @throws Exception
     */
    @Override
    protected synchronized void tearDown() throws Exception {
        server.stop();
    }
}
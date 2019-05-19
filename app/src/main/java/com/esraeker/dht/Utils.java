package com.esraeker.dht;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Utils {
    // belirtilen host 'a ulaşılabiliyor mu
    // projede servis url'si için kullanılıyor
    public static boolean isHostAvailable(final String host, final int port) throws IOException {
        final Socket socket = new Socket();
        final InetAddress inetAddress = InetAddress.getByName(host);
        final InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, port);
        socket.connect(inetSocketAddress);
        return true;
    }

}

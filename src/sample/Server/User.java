package sample.Server;

import java.awt.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.UUID;

public class User{
    private Socket socket;
    private String userName;
    private UUID uuid;
    private Color color;

    public User(Socket socket) {
        this.socket = socket;
        this.userName = "Гость";
        this.uuid = UUID.randomUUID();
        this.color = colorFromGuid(this.uuid);
    }

    public void setUserName(String userName) { this.userName = userName; }
    public String getUserName() { return userName; }
    public Socket getSocket() { return socket; }
    public UUID getUuid() { return uuid; }
    public Color getColor(){ return this.color; }
    public boolean equals(User user) { return (this.uuid.toString().equals(user.getUuid().toString())); }

    public static Color colorFromGuid(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[8]);
        bb.putLong(uuid.getLeastSignificantBits());
        int red = bb.get(0) & 0xff;
        int green = bb.get(1) & 0xff;
        int blue = bb.get(2) & 0xff;
        int alpha = bb.get(3) & 0xff;

        Color color = new Color(red, green, blue, alpha);
        return color;
    }
}
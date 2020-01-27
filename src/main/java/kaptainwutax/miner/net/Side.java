package kaptainwutax.miner.net;

public enum Side {

    UNKNOWN, CLIENT, SERVER;

    public boolean isClient() {
        return this == Side.CLIENT;
    }

    public boolean isServer() {
        return this == Side.SERVER;
    }

    public static Side fromString(String s) {
        s = s.trim().toLowerCase();

        if(s.equals("client")) {
            return Side.CLIENT;
        } else if(s.equals("server")) {
            return Side.SERVER;
        } else {
            return Side.UNKNOWN;
        }
    }

}

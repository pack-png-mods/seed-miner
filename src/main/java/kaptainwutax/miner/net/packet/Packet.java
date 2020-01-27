package kaptainwutax.miner.net.packet;


import kaptainwutax.miner.net.Side;
import kaptainwutax.miner.net.packet.util.C2SMessage;
import kaptainwutax.miner.net.packet.util.S2CMessage;

public abstract class Packet {

    public boolean canSendFrom(Side side) {
        if(side == Side.CLIENT) {
            return this instanceof C2SMessage;
        } else if(side == Side.SERVER) {
            return this instanceof S2CMessage;
        }

        return false;
    }

}

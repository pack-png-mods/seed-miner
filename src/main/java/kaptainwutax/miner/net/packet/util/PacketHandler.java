package kaptainwutax.miner.net.packet.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kaptainwutax.miner.net.context.ClientContext;
import kaptainwutax.miner.net.context.Context;
import kaptainwutax.miner.net.context.ServerContext;
import kaptainwutax.miner.net.packet.Packet;

public class PacketHandler {

    private static Gson GSON = new GsonBuilder().create();

    public Packet onPacketReceived(String rawData, Context context) {
        if(rawData.length() < 2)return null;

        int id = Integer.parseInt(rawData.charAt(0) + "");
        Class<Packet> packetClass = PacketRegistry.getPacketClass(id);
        if(packetClass == null)return null;

        Packet packet = this.createPacket(rawData.substring(1), packetClass);
        Packet returnPacket = null;

        if(packet instanceof C2SMessage && context instanceof ServerContext) {
            returnPacket = ((C2SMessage)packet).onPacketReceived((ServerContext)context);
        } else if(packet instanceof S2CMessage && context instanceof ClientContext) {
            returnPacket = ((S2CMessage)packet).onPacketReceived((ClientContext)context);
        }

        return returnPacket;
    }

    public Packet createPacket(String data, Class<Packet> packetClass) {
        return GSON.fromJson(data, packetClass);
    }

    public String packPacket(Packet packet) {
        return PacketRegistry.getPacketId(packet) + GSON.toJson(packet);
    }

}

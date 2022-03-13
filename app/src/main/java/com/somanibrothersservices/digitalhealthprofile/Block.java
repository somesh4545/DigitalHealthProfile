package com.somanibrothersservices.digitalhealthprofile;

 import com.google.firebase.firestore.FieldValue;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
 import java.util.ArrayList;
 import java.util.List;

public class Block {
    public String timestamp;
    public String lastHash, hash, data;
    public List<String> access;

    public Block(String timestamp, String lastHash, String hash, String data , List<String> access) {
        this.timestamp = timestamp;
        this.lastHash = lastHash;
        this.hash = hash;
        this.data = data;
        this.access = access;
    }

    public String blockToString() {
        return "Block" +
                "\nTimestamp : " + this.timestamp +
                "\nLast Hash : " + this.lastHash  +
                "\nHash      : " + this.hash  +
                "\nData      : " + this.data + "\n";
    }

    public static Block genesis() {
        List<String> accessL = new ArrayList<>();
        accessL.add("1");accessL.add("2");accessL.add("4");accessL.add("5");
        return new Block(String.valueOf(FieldValue.serverTimestamp()), "not valid", "frsdo-ae2324", "data", accessL);
    }
//
//    public static Block mineBlock(Block lastBlock, String data) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        String hash ,timestamp;
//        String lastHash = lastBlock.hash;
//        timestamp = String.valueOf(FieldValue.serverTimestamp());
//        hash = Block.getHash(timestamp, lastHash, data);
//
//        return new Block(timestamp, lastHash, hash, data);
//    }

    public static String getHash(String timestamp, String lastHash, String data) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        String input = timestamp.toString() + lastHash + data;
        byte[] byteData = digest.digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static String blockHash(Block block) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        return Block.getHash(block.timestamp, block.lastHash, block.data);
    }
}

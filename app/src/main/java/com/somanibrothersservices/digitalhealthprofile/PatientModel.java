package com.somanibrothersservices.digitalhealthprofile;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatientModel {
    public String id , name , image , age;
    public Blockchain blockchain;

    public PatientModel(String id , String name, String image, String age, String blockchain) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        this.id = id;
        this.name = name;
        this.image = image;
        this.age = age;
        this.blockchain = new Blockchain();
        String[] blocks = blockchain.split(">");
        List<Block> blockList = new ArrayList<>();
        for (String block : blocks){
            String[] list = block.split(";");
            Log.i("Main Activity" , String.valueOf(list));
            blockList.add(new Block(list[0],list[1],list[2],list[3] , Arrays.asList(list[4].split(","))));
        }
        this.blockchain.replaceChain(blockList);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(Blockchain blockchain) {
        this.blockchain = blockchain;
    }
}

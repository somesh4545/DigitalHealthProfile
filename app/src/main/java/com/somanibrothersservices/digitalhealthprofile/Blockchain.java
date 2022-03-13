package com.somanibrothersservices.digitalhealthprofile;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    public List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        chain.add( Block.genesis());
    }
//
//    public Block addBlock(String data) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//		Block lastBlock = this.chain.get(this.chain.size() - 1);
//		Block block = Block.mineBlock(lastBlock,data) ;
//        this.chain.add(block) ;
//
//        return block ;
//    }

    public Block addBlock(Block block)  {
        this.chain.add(block) ;
        return block ;
    }

    public static Boolean isValidChain(List<Block> chain) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if(chain.get(0).blockToString() != Block.genesis().blockToString())  return false ;
        for(int i=1;i<chain.size();i++){
			Block block = chain.get(i);
			Block lastBlock = chain.get(i - 1);

            if(block.lastHash != lastBlock.hash || block.hash != Block.blockHash(block)){
                return false ;
            }
        }
        return true ;
    }

    public void replaceChain(List<Block> newChain) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        if(newChain.size() <= this.chain.size()){
//            //console.log('Chain not longer than current chain') ;
//            return ;
//        }
//        else if(!this.isValidChain(newChain)){
//            //console.log('received chain is not valid') ;
//            return ;
//        }

        //console.log('replacing blockchain eitn new chain') ;
        this.chain = newChain;
    }
}

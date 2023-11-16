package com.eshore.ctmp.protocol.utils;


public class SequenceGenerator {

    private static int seqId = 0;//RandomGenerator.getAbsInt();

    public static synchronized int nextSequence() {
        if (seqId == Integer.MAX_VALUE) {
            seqId = 0;
        }

        return seqId++;
    }


}

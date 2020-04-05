package com.ere.rabbit.producer.service;

public class SingleDocumentQueue {

    public static class Singleton {
        public static final SingleDocumentQueue HOLDER = new SingleDocumentQueue();
    }


}

package com.example.XMLtoJSON;

public interface SaxHmlHandlerListener {
    void onComplete(String json);
    void onError();
}

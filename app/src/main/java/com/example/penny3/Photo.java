package com.example.penny3;

class Photo {
    private String mSender;
    private String mMessage;
    private String mColour;
    private String mRoom;

    public Photo(String sender, String message, String colour, String room) {
        mSender = sender;
        mMessage = message;
        mColour = colour;
        mRoom = room;
    }

    String getSender() {
        return mSender;
    }

    String getMessage() {
        return mMessage;
    }

    String getColour() {
        return mColour;
    }

    String getRoom() {
        return mRoom;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "mSender='" + mSender + '\'' +
                ", mMessage='" + mMessage + '\'' +
                ", mColour='" + mColour + '\'' +
                ", mRoom='" + mRoom + '\'' +
                '}';
    }
}

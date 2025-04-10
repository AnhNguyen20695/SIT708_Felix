package com.example.task41;

public class Board {
    private int ID;
    private String boardName;

    public Board() {}

    public Board(int ID, String boardName) {
        this.ID = ID;
        this.boardName = boardName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }
}

package com.vasenin.workcube.misc;

public enum SocketsBadge {
    DONT_KNOW("Не знаю"),
    FEW("Мало"),
    MEDIUM("Достаточно"),
    MANY("Много");

    private String title;

    SocketsBadge(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

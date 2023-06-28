package com.vasenin.workcube.misc;

public enum QuietPlaceBadge {
    DONT_KNOW("Не знаю"),
    QUIET("Тихо"),
    MEDIUM("Заметно"),
    LOUD("Шумно");

    private String title;

    QuietPlaceBadge(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

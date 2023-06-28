package com.vasenin.workcube.misc;

public enum FastWifiBadge {
    DONT_KNOW("Не знаю"),
    NONE("Отсутствует"),
    SLOW("Медленный"),
    MEDIUM("Средний"),
    FAST("Быстрый");

    private String title;

    FastWifiBadge(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

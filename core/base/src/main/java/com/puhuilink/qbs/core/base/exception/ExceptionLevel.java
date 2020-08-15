package com.puhuilink.qbs.core.base.exception;

public enum ExceptionLevel {
    /**
     * warn 1
     * error 2
     * fatal 3
     */
    WARN(1, "warn"),
    ERROR(2, "error"),
    FATAL(3, "fatal"),
    RETRY(4, "retry");
    private int level;
    private String name;

    private ExceptionLevel(int level, String name) {
        this.name = name;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ExceptionLevel{" +
                "level=" + level +
                ", name='" + name + '\'' +
                '}';
    }
}

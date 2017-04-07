package com.comp460.common.ui;

/**
 * Created by Belinda on 4/5/17.
 */
public class ButtonTemplate {
    public String text;

    public Runnable action;
    public ButtonTemplate(String text, Runnable action) {
        this.text = text;
        this.action = action;
    }
}
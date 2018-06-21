package com.ihypnus.multilanguage;

/**
 * eventbus语言设置事件回调
 */

public class OnChangeLanguageEvent {
    public int languageType;

    public OnChangeLanguageEvent(int languageType) {
        this.languageType = languageType;
    }
}

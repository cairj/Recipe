package com.recipe.r.entity;

/**
 * Created by Administrator on 2016/9/20.
 */

public class SearchHistory {
    private String id;
    private String user;
    private String input;

    public SearchHistory(String id, String user, String input) {
        super();
        this.id = id;
        this.user = user;
        this.input = input;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}

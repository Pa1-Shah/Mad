package com.example.tripisss;

import java.util.HashMap;

public class Globe {       //will act as a global class. data here can be accessed anywhere
    public static Globe instance;
    public String user;
    public HashMap<String, String> users;
    Globe(){
        users = new HashMap<String, String>(); //email and address present in firebase

    }
    public static Globe getInstance() {
        if(instance == null){
            instance = new Globe();
        }
        return instance;
    }
}

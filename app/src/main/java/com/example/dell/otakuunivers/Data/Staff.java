package com.example.dell.otakuunivers.Data;

public class Staff {

    String name;
    String role;
    String image;
    String media;
    String description;

    public Staff(String c_name, String c_role, String c_image, String c_media, String c_description)
    {
        name = c_name;
        image = c_image;
        role  = c_role;
        media = c_media;
        description = c_description;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getImage() {
        return image;
    }
}

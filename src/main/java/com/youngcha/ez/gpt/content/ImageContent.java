package com.youngcha.ez.gpt.content;

public class ImageContent extends Content{

    private ImageUrl image_url;

    public ImageContent(String type, ImageUrl image_url) {
        super(type);
        this.image_url = image_url;
    }

    public ImageUrl getImage_url() {
        return image_url;
    }
    public void setImage_url(ImageUrl image_url) {
        this.image_url = image_url;
    }
}

package com.nicktoony.getaway;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Nick on 03/09/2015.
 */
public class SpriteSheetReader {

    private Map<String, TextureRegion> textureRegions = new HashMap<String, TextureRegion>();

    public SpriteSheetReader(FileHandle xmlFile, FileHandle imageFile) {
        Texture  texture = new Texture(imageFile);

        XmlReader reader = new XmlReader();
        try {
            XmlReader.Element root = reader.parse(xmlFile);

            Array<XmlReader.Element> subtextures = root.getChildrenByName("SubTexture");
            for (XmlReader.Element subtexture : subtextures) {
                int x = subtexture.getIntAttribute("x");
                int y = subtexture.getIntAttribute("y");
                int width = subtexture.getIntAttribute("width");
                int height = subtexture.getIntAttribute("height");
                String name = subtexture.getAttribute("name");

                TextureRegion textureRegion = new TextureRegion(texture, x, y, width, height);
                textureRegions.put(name, textureRegion);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TextureRegion getTexture(String name) {
        return textureRegions.get(name);
    }
}

package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public class GameResources implements Disposable {
    private final TiledMap level;
    private final Texture blueTankTexture;
    private final Texture greenTreeTexture;

    public GameResources(){
        level = new TmxMapLoader().load("level.tmx");
        blueTankTexture = new Texture("images/tank_blue.png");
        greenTreeTexture = new Texture("images/greenTree.png");
    }

    @Override
    public void dispose(){
        greenTreeTexture.dispose();
        blueTankTexture.dispose();
        level.dispose();
    }

    public TiledMap getLevel(){
        return level;
    }
    public Texture getBlueTankTexture(){
        return blueTankTexture;
    }
    public Texture getGreenTreeTexture(){
        return greenTreeTexture;
    }
}

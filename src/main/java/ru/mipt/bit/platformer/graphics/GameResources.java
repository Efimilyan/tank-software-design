package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public class GameResources implements Disposable {
    private final TiledMap level;
    private final Texture tankTexture;
    private final Texture greenTreeTexture;
    private final Texture bulletTexture;

    public GameResources(ResourcePaths paths){
        level = new TmxMapLoader().load(paths.map());
        tankTexture = new Texture(paths.tankTexture());
        greenTreeTexture = new Texture(paths.treeTexture());
        bulletTexture = new Texture(paths.bulletTexture());
    }

    @Override
    public void dispose(){
        bulletTexture.dispose();
        greenTreeTexture.dispose();
        tankTexture.dispose();
        level.dispose();
    }

    public TiledMap getLevel(){
        return level;
    }
    public Texture getTankTexture(){
        return tankTexture;
    }
    public Texture getGreenTreeTexture(){
        return greenTreeTexture;
    }
    public Texture getBulletTexture(){
        return bulletTexture;
    }
}
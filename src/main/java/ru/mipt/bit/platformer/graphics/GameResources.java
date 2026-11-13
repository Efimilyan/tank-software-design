package ru.mipt.bit.platformer.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public class GameResources implements Disposable {
    private static final int PIXEL_SIZE = 1;
    private final OwnedResources ownedResources = new OwnedResources();
    private final TiledMap level;
    private final Texture tankTexture;
    private final Texture greenTreeTexture;
    private final Texture bulletTexture;
    private final Texture pixelTexture;

    public GameResources(ResourcePaths paths){
        level = ownedResources.own(new TmxMapLoader().load(paths.map()));
        tankTexture = ownedResources.own(new Texture(paths.tankTexture()));
        greenTreeTexture = ownedResources.own(new Texture(paths.treeTexture()));
        bulletTexture = ownedResources.own(new Texture(paths.bulletTexture()));
        pixelTexture = ownedResources.own(createWhitePixel());
    }
    private static Texture createWhitePixel() {
        Pixmap pixmap = new Pixmap(PIXEL_SIZE, PIXEL_SIZE, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }
    @Override
    public void dispose(){
        ownedResources.dispose();
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
    public Texture getPixelTexture(){
        return pixelTexture;
    }
}
package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;

public class BulletGun implements Gun {
    private final Level level;
    private final CollisionChecker collisionChecker;
    private final float bulletSpeed;
    private final int bulletDamage;
    private final float reloadTime;
    private float reloadLeft = 0f;

    public BulletGun(Level level, CollisionChecker collisionChecker,
                     float bulletSpeed, int bulletDamage, float reloadTime) {
        this.level = level;
        this.collisionChecker = collisionChecker;
        this.bulletSpeed = bulletSpeed;
        this.bulletDamage = bulletDamage;
        this.reloadTime = reloadTime;
    }

    @Override
    public void update(float deltaTime) {
        reloadLeft = Math.max(0f, reloadLeft - deltaTime);
    }

    @Override
    public void fire(GridPoint2 from, Direction direction) {
        if (reloadLeft > 0f) {
            return;
        }
        reloadLeft = reloadTime;
        if (collisionChecker.isOccupied(from)) {
            collisionChecker.hit(from, bulletDamage);
            return;
        }
        level.add(new Bullet(from, direction, bulletSpeed, bulletDamage, collisionChecker));
    }
}

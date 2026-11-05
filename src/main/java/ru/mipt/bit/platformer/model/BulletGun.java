package ru.mipt.bit.platformer.model;

import com.badlogic.gdx.math.GridPoint2;
import ru.mipt.bit.platformer.model.api.BattleField;
import ru.mipt.bit.platformer.model.api.Direction;
import ru.mipt.bit.platformer.model.api.Gun;
import ru.mipt.bit.platformer.model.api.Spawner;

public class BulletGun implements Gun {
    private final Spawner spawner;
    private final BattleField battleField;
    private final float bulletSpeed;
    private final int bulletDamage;
    private final float reloadTime;
    private float reloadLeft = 0f;

    public BulletGun(Spawner spawner, BattleField battleField,
                     float bulletSpeed, int bulletDamage, float reloadTime) {
        this.spawner = spawner;
        this.battleField = battleField;
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
        if (battleField.isOccupied(from)) {
            battleField.hit(from, bulletDamage);
            return;
        }
        spawner.spawn(new Bullet(from, direction, bulletSpeed, bulletDamage, battleField));
    }
}
package ru.mipt.bit.platformer.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BulletTypeTest {
    private static final int NOMINAL_HEALTH = 100;
    private static final int MIN_HEALTH = 80;
    private static final int HITS_TO_KILL = 4;

    @Test
    void baseBulletDamageIsNominalHealthDividedByHits() {
        assertEquals(25, BulletType.BASE_BULLET.damageFor(NOMINAL_HEALTH));
    }

    @Test
    void damageIsRoundedUp() {
        assertEquals(2, BulletType.BASE_BULLET.damageFor(5));
    }

    @Test
    void baseBulletKillsAnyTankInRangeInExactlyFourHits() {
        int damage = BulletType.BASE_BULLET.damageFor(NOMINAL_HEALTH);

        for (int maxHealth = MIN_HEALTH; maxHealth <= NOMINAL_HEALTH; maxHealth++) {
            Health health = new Health(maxHealth);
            for (int hit = 1; hit < HITS_TO_KILL; hit++) {
                health.takeDamage(damage);
            }
            assertTrue(health.isAlive());
            health.takeDamage(damage);
            assertFalse(health.isAlive());
        }
    }

    @Test
    void baseBulletHasPositiveSpeed() {
        assertTrue(BulletType.BASE_BULLET.getSpeed() > 0f);
    }
}
package com.game.mario;

import java.util.ArrayList;
import java.util.List;

public class GameLevel {
    private List<Platform> platforms;
    private List<Enemy> enemies;
    private List<BossEnemy> bosses;
    private List<BossWeaponPickup> bossPickups;
    private List<HeroWeapon> heroWeapons;
    private int levelNumber;
    private double finishLineX;
    private double bossGroundY = 620;
    private int pickupTimer = 0;
    private int pickupInterval = 180;
    private final AudioManager audioManager = AudioManager.getInstance();
    
    public GameLevel(int levelNumber) {
        this.levelNumber = levelNumber;
        this.platforms = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.bosses = new ArrayList<>();
        this.bossPickups = new ArrayList<>();
        this.heroWeapons = new ArrayList<>();
        createLevel();
    }
    
    private void createLevel() {
        switch(levelNumber) {
            case 1: createLevel1(); break;
            case 2: createLevel2(); break;
            case 3: createLevel3(); break;
            case 4: createLevel4(); break;
            case 5: createLevel5(); break;
            case 6: createLevel6(); break;
            case 7: createLevel7(); break;
            case 8: createLevel8(); break;
            case 9: createLevel9(); break;
            case 10: createLevel10(); break;
            default: createLevel1(); break;
        }
    }
    
    private void createLevel1() {
        platforms.add(new Platform(0, 650, 400, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(500, 650, 700, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(300, 550, 150, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(550, 500, 150, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(800, 450, 150, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(400, 600, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(650, 450, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(900, 400, 30, 30, Platform.PlatformType.COIN));
        enemies.add(new Enemy(600, 605, 500, 700));
        enemies.add(new Enemy(850, 405, 800, 950));
        finishLineX = 1150;
    }
    
    private void createLevel2() {
        platforms.add(new Platform(0, 650, 200, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(250, 550, 100, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(400, 500, 100, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(550, 450, 100, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(700, 400, 100, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(850, 350, 100, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(1000, 400, 200, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(300, 500, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(500, 400, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(750, 350, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(900, 300, 30, 30, Platform.PlatformType.COIN));
        enemies.add(new Enemy(450, 455, 400, 500));
        enemies.add(new Enemy(750, 355, 700, 800));
        enemies.add(new Enemy(1050, 355, 1000, 1150));
        finishLineX = 1150;
    }
    
    private void createLevel3() {
        platforms.add(new Platform(0, 650, 150, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(200, 580, 100, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(350, 520, 100, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(500, 460, 100, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(650, 400, 100, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(800, 340, 100, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(950, 350, 250, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(250, 530, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(550, 410, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(850, 290, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(1050, 300, 30, 30, Platform.PlatformType.COIN));
        enemies.add(new Enemy(400, 475, 350, 450));
        enemies.add(new Enemy(700, 355, 650, 750));
        enemies.add(new Enemy(1050, 305, 950, 1150));
        finishLineX = 1150;
    }
    
    private void createLevel4() {
        platforms.add(new Platform(0, 650, 120, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(180, 580, 90, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(320, 520, 90, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(460, 460, 90, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(600, 400, 90, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(740, 340, 90, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(880, 400, 120, 40, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(1050, 500, 150, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(230, 530, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(510, 410, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(790, 290, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(930, 350, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(1100, 450, 30, 30, Platform.PlatformType.COIN));
        double enemyHeight = 35;
        enemies.add(new Enemy(205, 580 - enemyHeight, 185, 235)); // early platform
        enemies.add(new Enemy(485, 460 - enemyHeight, 470, 520)); // mid tower
        enemies.add(new Enemy(765, 340 - enemyHeight, 750, 810)); // high platform
        enemies.add(new Enemy(1100, 500 - enemyHeight, 1050, 1150)); // final ground
        finishLineX = 1150;
    }
    
    private void createLevel5() {
        bossGroundY = 620;
        platforms.add(new Platform(0, bossGroundY, 1200, 80, Platform.PlatformType.GROUND));
        finishLineX = 1180;
        bosses.add(new BossEnemy(850, bossGroundY - 90, 200, 1100, levelNumber));
        pickupInterval = 170;
        pickupTimer = pickupInterval;
    }
    
    private void createLevel6() {
        platforms.add(new Platform(0, 640, 400, 60, Platform.PlatformType.GROUND));
        platforms.add(new Platform(450, 640, 350, 60, Platform.PlatformType.GROUND));
        platforms.add(new Platform(820, 640, 380, 60, Platform.PlatformType.GROUND));
        platforms.add(new Platform(300, 560, 160, 35, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(620, 540, 160, 35, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(930, 520, 160, 35, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(220, 600, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(520, 570, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(740, 600, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(960, 500, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(1080, 600, 30, 30, Platform.PlatformType.COIN));
        enemies.add(new Enemy(180, 600, 120, 360));
        enemies.add(new Enemy(520, 600, 480, 680));
        enemies.add(new Enemy(900, 600, 860, 1040));
        finishLineX = 1150;
    }
    
    private void createLevel7() {
        platforms.add(new Platform(0, 640, 320, 60, Platform.PlatformType.GROUND));
        platforms.add(new Platform(360, 600, 180, 50, Platform.PlatformType.GROUND));
        platforms.add(new Platform(580, 640, 220, 60, Platform.PlatformType.GROUND));
        platforms.add(new Platform(830, 620, 180, 55, Platform.PlatformType.GROUND));
        platforms.add(new Platform(1030, 640, 170, 60, Platform.PlatformType.GROUND));
        platforms.add(new Platform(250, 560, 150, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(520, 540, 140, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(760, 560, 140, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(980, 580, 120, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(200, 610, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(410, 580, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(660, 590, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(860, 580, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(1080, 600, 30, 30, Platform.PlatformType.COIN));
        enemies.add(new Enemy(140, 600, 80, 260));
        enemies.add(new Enemy(450, 600, 360, 520));
        enemies.add(new Enemy(700, 600, 640, 820));
        enemies.add(new Enemy(980, 600, 920, 1120));
        finishLineX = 1150;
    }
    
    private void createLevel8() {
        platforms.add(new Platform(0, 640, 260, 60, Platform.PlatformType.GROUND));
        platforms.add(new Platform(300, 620, 220, 55, Platform.PlatformType.GROUND));
        platforms.add(new Platform(560, 600, 200, 55, Platform.PlatformType.GROUND));
        platforms.add(new Platform(780, 620, 220, 55, Platform.PlatformType.GROUND));
        platforms.add(new Platform(1020, 640, 180, 60, Platform.PlatformType.GROUND));
        platforms.add(new Platform(200, 560, 160, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(460, 540, 160, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(700, 560, 160, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(940, 580, 160, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(240, 600, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(520, 570, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(760, 580, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(1010, 600, 30, 30, Platform.PlatformType.COIN));
        enemies.add(new Enemy(150, 600, 80, 280));
        enemies.add(new Enemy(430, 600, 360, 520));
        enemies.add(new Enemy(670, 600, 600, 760));
        enemies.add(new Enemy(930, 600, 880, 1080));
        finishLineX = 1150;
    }
    
    private void createLevel9() {
        platforms.add(new Platform(0, 640, 280, 60, Platform.PlatformType.GROUND));
        platforms.add(new Platform(320, 620, 220, 55, Platform.PlatformType.GROUND));
        platforms.add(new Platform(560, 600, 220, 55, Platform.PlatformType.GROUND));
        platforms.add(new Platform(820, 620, 220, 55, Platform.PlatformType.GROUND));
        platforms.add(new Platform(1060, 640, 160, 60, Platform.PlatformType.GROUND));
        platforms.add(new Platform(200, 580, 150, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(450, 560, 150, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(700, 580, 150, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(950, 600, 150, 30, Platform.PlatformType.FLOATING));
        platforms.add(new Platform(250, 610, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(500, 590, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(750, 610, 30, 30, Platform.PlatformType.COIN));
        platforms.add(new Platform(1000, 630, 30, 30, Platform.PlatformType.COIN));
        enemies.add(new Enemy(170, 600, 100, 320));
        enemies.add(new Enemy(430, 600, 360, 540));
        enemies.add(new Enemy(690, 600, 620, 780));
        enemies.add(new Enemy(940, 600, 880, 1100));
        finishLineX = 1150;
    }
    
    private void createLevel10() {
        bossGroundY = 610;
        platforms.add(new Platform(0, bossGroundY, 1250, 90, Platform.PlatformType.GROUND));
        finishLineX = 1180;
        bosses.add(new BossEnemy(900, bossGroundY - 90, 150, 1120, levelNumber));
        pickupInterval = 140;
        pickupTimer = pickupInterval;
    }
    
    public void handleCollisions(Player player) {
        player.setOnGround(false);
        for (Platform platform : platforms) {
            if (platform.getType() == Platform.PlatformType.COIN) {
                if (platform.intersects(player.getX(), player.getY(), player.getWidth(), player.getHeight())) {
                    player.collectCoin();
                    platforms.remove(platform);
                    break;
                }
                continue;
            }
            if (platform.intersects(player.getX(), player.getY(), player.getWidth(), player.getHeight())) {
                double playerBottom = player.getY() + player.getHeight();
                double platformTop = platform.getCollisionTop();
                double playerTop = player.getY();
                double platformBottom = platformTop + platform.getCollisionHeight();
                if (player.getVelocityY() > 0 && playerBottom - player.getVelocityY() <= platformTop + 5) {
                    player.setY(platformTop - player.getHeight());
                    player.setVelocityY(0);
                    player.setOnGround(true);
                } else if (player.getVelocityY() < 0 && playerTop - player.getVelocityY() >= platformBottom - 5) {
                    player.setY(platformBottom);
                    player.setVelocityY(0);
                }
            }
        }
        for (Enemy enemy : enemies) {
            if (!enemy.isAlive()) continue;
            if (isColliding(player, enemy)) {
                double playerBottom = player.getY() + player.getHeight();
                double enemyTop = enemy.getY();
                if (player.getVelocityY() > 0 && playerBottom - player.getVelocityY() <= enemyTop + 10) {
                    enemy.kill();
                    player.setVelocityY(-10);
                    audioManager.playEffect(AudioManager.SFX_ENEMY_STOMP);
                } else {
                    player.takeDamage(10);
                }
            }
        }

        for (BossEnemy boss : bosses) {
            for (BossEnemy.BossProjectile projectile : boss.getProjectiles()) {
                if (!projectile.isActive()) continue;
                if (isColliding(player, projectile)) {
                    player.takeDamage(20);
                    projectile.deactivate();
                }
            }
            if (!boss.isAlive()) continue;
            if (isColliding(player, boss)) {
                player.takeDamage(25);
            }
        }
        if (player.getY() > 700) {
            player.takeDamage(100);
        }
    }
    
    private boolean isColliding(GameObject a, GameObject b) {
        return a.getX() < b.getX() + b.getWidth() &&
               a.getX() + a.getWidth() > b.getX() &&
               a.getY() < b.getY() + b.getHeight() &&
               a.getY() + a.getHeight() > b.getY();
    }
    
    public boolean isLevelComplete(Player player) {
        if (!bosses.isEmpty()) {
            boolean bossesDown = true;
            for (BossEnemy boss : bosses) {
                if (boss.isAlive()) {
                    bossesDown = false;
                    break;
                }
            }
            if (!bossesDown) return false;
        }
        return player.getX() >= finishLineX;
    }
    
    public List<Platform> getPlatforms() { return platforms; }
    public List<Enemy> getEnemies() { return enemies; }
    public int getLevelNumber() { return levelNumber; }
    public double getFinishLineX() { return finishLineX; }
    public List<BossEnemy> getBosses() { return bosses; }
    public List<BossWeaponPickup> getBossPickups() { return bossPickups; }
    public List<HeroWeapon> getHeroWeapons() { return heroWeapons; }
    public boolean hasBossFight() { return !bosses.isEmpty(); }
    public boolean hasActiveBoss() {
        for (BossEnemy boss : bosses) {
            if (boss.isAlive()) return true;
        }
        return false;
    }
    public double getBossHealthPercent() {
        if (bosses.isEmpty()) return 0;
        double total = 0;
        int count = 0;
        for (BossEnemy boss : bosses) {
            total += boss.getHealthPercent();
            count++;
        }
        return count == 0 ? 0 : total / count;
    }

    public void updateBossSystems(Player player) {
        if (bosses.isEmpty()) return;
        if (!hasActiveBoss()) {
            bossPickups.clear();
            heroWeapons.clear();
            return;
        }
        pickupTimer++;
        if (pickupTimer >= pickupInterval) {
            pickupTimer = 0;
            spawnWeaponDrop();
        }
        for (BossWeaponPickup pickup : bossPickups) {
            pickup.update();
        }
        for (HeroWeapon weapon : heroWeapons) {
            weapon.update();
        }
        handlePickupCollection(player);
        handleHeroWeaponHits();
        bossPickups.removeIf(BossWeaponPickup::shouldRemove);
        heroWeapons.removeIf(weapon -> !weapon.isActive());
    }

    private void spawnWeaponDrop() {
        double minX = 150;
        double maxX = Math.max(minX + 100, finishLineX - 150);
        double spawnX = minX + Math.random() * (maxX - minX);
        bossPickups.add(new BossWeaponPickup(spawnX, -60, bossGroundY));
    }

    private void handlePickupCollection(Player player) {
        for (BossWeaponPickup pickup : bossPickups) {
            if (!pickup.isAvailable()) continue;
            if (isColliding(player, pickup)) {
                pickup.collect();
                launchHeroWeapon(player);
            }
        }
    }

    private void launchHeroWeapon(Player player) {
        BossEnemy target = getFirstAliveBoss();
        if (target == null) return;
        double startX = player.getX() + player.getWidth() / 2 - 10;
        double startY = player.getY() + player.getHeight() / 2;
        HeroWeapon weapon = new HeroWeapon(startX, startY, target.getX() + target.getWidth() / 2);
        heroWeapons.add(weapon);
    }

    private BossEnemy getFirstAliveBoss() {
        for (BossEnemy boss : bosses) {
            if (boss.isAlive()) return boss;
        }
        return null;
    }

    private void handleHeroWeaponHits() {
        for (HeroWeapon weapon : heroWeapons) {
            if (!weapon.isActive()) continue;
            for (BossEnemy boss : bosses) {
                if (!boss.isAlive()) continue;
                if (isColliding(weapon, boss)) {
                    boss.takeDamage(1);
                    weapon.deactivate();
                    audioManager.playEffect(AudioManager.SFX_BOSS_HIT);
                    break;
                }
            }
        }
    }
}

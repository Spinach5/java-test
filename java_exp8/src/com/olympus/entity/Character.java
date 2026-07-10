package com.olympus.entity;

/**
 * 角色实体类 - 封装奥林匹斯诸神与泰坦的属性
 */
public class Character {
    private String name;       // 角色名称
    private String title;      // 称号
    private String faction;    // 阵营：Olympus(奥林匹斯) / Titan(泰坦)
    private int maxHp;         // 最大生命值
    private int hp;            // 当前生命值
    private int attack;        // 攻击力
    private int defense;       // 防御力
    private String skill;      // 技能名称
    private int skillDamage;   // 技能伤害

    public Character(String name, String title, String faction,
                     int maxHp, int attack, int defense,
                     String skill, int skillDamage) {
        this.name = name;
        this.title = title;
        this.faction = faction;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.attack = attack;
        this.defense = defense;
        this.skill = skill;
        this.skillDamage = skillDamage;
    }

    /** 受到伤害，返回实际扣血量 */
    public int takeDamage(int dmg) {
        int actual = Math.max(1, dmg - this.defense / 3);
        this.hp = Math.max(0, this.hp - actual);
        return actual;
    }

    /** 治疗回复 */
    public void heal(int amount) {
        this.hp = Math.min(this.maxHp, this.hp + amount);
    }

    /** 是否存活 */
    public boolean isAlive() {
        return this.hp > 0;
    }

    // getter / setter
    public String getName() { return name; }
    public String getTitle() { return title; }
    public String getFaction() { return faction; }
    public int getMaxHp() { return maxHp; }
    public int getHp() { return hp; }
    public void setHp(int hp) { this.hp = hp; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public String getSkill() { return skill; }
    public int getSkillDamage() { return skillDamage; }

    /** 返回简要信息字符串 */
    public String getBriefInfo() {
        return String.format("%s·%s [%s]  HP:%d/%d  ATK:%d  DEF:%d",
                name, title, faction.equals("Olympus") ? "奥林匹斯" : "泰坦",
                hp, maxHp, attack, defense);
    }

    @Override
    public String toString() {
        return name + "·" + title;
    }
}

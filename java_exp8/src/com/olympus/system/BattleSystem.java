package com.olympus.system;

import com.olympus.entity.BattleRecord;
import com.olympus.entity.Character;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 战斗系统 - 核心业务逻辑
 * 负责角色初始化、战斗回合处理、战利品统计、战绩记录
 */
public class BattleSystem {

    private final List<BattleRecord> battleRecords = new ArrayList<>();
    private final Random random = new Random();
    private final StringBuilder logBuilder = new StringBuilder();

    /** 初始化奥林匹斯诸神 */
    public List<Character> initGods() {
        List<Character> gods = new ArrayList<>();
        gods.add(new Character("宙斯", "雷霆之王", "Olympus", 200, 35, 15, "雷霆万钧", 50));
        gods.add(new Character("波塞冬", "海洋之主", "Olympus", 220, 30, 20, "海啸冲击", 45));
        gods.add(new Character("雅典娜", "智慧女神", "Olympus", 170, 28, 18, "神盾格挡", 40));
        gods.add(new Character("阿瑞斯", "战神", "Olympus", 190, 40, 12, "狂暴突袭", 55));
        gods.add(new Character("阿波罗", "光明之神", "Olympus", 175, 32, 14, "烈日光束", 48));
        return gods;
    }

    /** 初始化泰坦残部 */
    public List<Character> initTitans() {
        List<Character> titans = new ArrayList<>();
        titans.add(new Character("克洛诺斯", "时间泰坦", "Titan", 230, 38, 16, "时间扭曲", 52));
        titans.add(new Character("阿特拉斯", "擎天泰坦", "Titan", 250, 25, 28, "山岳崩裂", 42));
        titans.add(new Character("普罗米修斯", "先知泰坦", "Titan", 185, 30, 15, "天火降临", 46));
        titans.add(new Character("厄庇墨透斯", "后知泰坦", "Titan", 195, 33, 13, "混沌之力", 44));
        titans.add(new Character("科俄斯", "北极泰坦", "Titan", 210, 29, 22, "极寒风暴", 41));
        return titans;
    }

    /** 普通攻击 */
    public String normalAttack(Character attacker, Character defender) {
        int damage = attacker.getAttack() + random.nextInt(10) - 5;
        int actual = defender.takeDamage(damage);
        String msg = String.format("  > %s 发起普通攻击，对 %s 造成 %d 点伤害！（%s HP:%d）",
                attacker.getName(), defender.getName(), actual,
                defender.getName(), defender.getHp());
        appendLog(msg);
        return msg;
    }

    /** 技能攻击 */
    public String skillAttack(Character attacker, Character defender) {
        int damage = attacker.getSkillDamage() + random.nextInt(15);
        int actual = defender.takeDamage(damage);
        String msg = String.format("  > %s 释放技能【%s】！对 %s 造成 %d 点技能伤害！（%s HP:%d）",
                attacker.getName(), attacker.getSkill(), defender.getName(),
                actual, defender.getName(), defender.getHp());
        appendLog(msg);
        return msg;
    }

    /** 防御回复 */
    public String defend(Character self) {
        int healAmount = 15 + random.nextInt(10);
        self.heal(healAmount);
        String msg = String.format("  > %s 进入防御姿态，回复 %d 点生命！（HP:%d/%d）",
                self.getName(), healAmount, self.getHp(), self.getMaxHp());
        appendLog(msg);
        return msg;
    }

    /** AI回合行动 */
    public String aiTurn(Character ai, Character player) {
        int choice = random.nextInt(10);
        if (choice < 5) {
            return normalAttack(ai, player);
        } else if (choice < 8) {
            return skillAttack(ai, player);
        } else {
            return defend(ai);
        }
    }

    /** 记录战斗结果 */
    public BattleRecord recordBattle(Character god, Character titan, int rounds, String summary) {
        String winner = god.isAlive() ? god.getName() : titan.getName();
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        BattleRecord record = new BattleRecord(
                god.getName(), titan.getName(), winner, rounds, time, summary);
        battleRecords.add(record);
        return record;
    }

    /** 生成战利品 */
    public String generateLoot(Character winner, Character loser) {
        String[] lootPool = {"奥利哈康矿石", "神界精铁", "泰坦之血", "奥林匹斯圣水",
                "冥河碎片", "混沌水晶", "永恒之火", "命运丝线"};
        int count = 1 + random.nextInt(3);
        StringBuilder sb = new StringBuilder();
        sb.append("  战利品掉落: ");
        for (int i = 0; i < count; i++) {
            String loot = lootPool[random.nextInt(lootPool.length)];
            sb.append(loot);
            if (i < count - 1) sb.append(", ");
        }
        sb.append(String.format(" | 经验值: +%d | 金币: +%d",
                50 + random.nextInt(100), 20 + random.nextInt(50)));
        appendLog(sb.toString());
        return sb.toString();
    }

    public List<BattleRecord> getBattleRecords() {
        return battleRecords;
    }

    public String getLog() {
        return logBuilder.toString();
    }

    public void clearLog() {
        logBuilder.setLength(0);
    }

    private void appendLog(String msg) {
        logBuilder.append(msg).append("\n");
    }
}

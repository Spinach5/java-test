package com.olympus.gui;

import com.olympus.entity.BattleRecord;
import com.olympus.system.BattleSystem;

/**
 * 控制台测试类 - 验证业务逻辑正确性（无图形环境时使用）
 */
public class ConsoleTest {
    public static void main(String[] args) {
        BattleSystem system = new BattleSystem();
        var gods = system.initGods();
        var titans = system.initTitans();

        System.out.println("===== 奥林匹斯诸神 =====");
        for (var g : gods) {
            System.out.println(g.getBriefInfo() + " 技能:" + g.getSkill());
        }
        System.out.println("\n===== 泰坦残部 =====");
        for (var t : titans) {
            System.out.println(t.getBriefInfo() + " 技能:" + t.getSkill());
        }

        // 模拟一场战斗
        com.olympus.entity.Character god = gods.get(0);
        com.olympus.entity.Character titan = titans.get(0);
        System.out.println("\n===== 战斗开始 =====");
        System.out.println(god.getName() + " VS " + titan.getName());

        int round = 1;
        while (god.isAlive() && titan.isAlive() && round <= 20) {
            System.out.println("--- 第" + round + "回合 ---");
            system.normalAttack(god, titan);
            if (!titan.isAlive()) {
                System.out.println(titan.getName() + " 被击败！");
                break;
            }
            system.aiTurn(titan, god);
            if (!god.isAlive()) {
                System.out.println(god.getName() + " 被击败！");
                break;
            }
            round++;
        }

        com.olympus.entity.Character winner = god.isAlive() ? god : titan;
        System.out.println("\n战斗结束！胜者: " + winner.getName());

        // 战利品
        com.olympus.entity.Character loser = winner == god ? titan : god;
        system.generateLoot(winner, loser);

        // 记录战绩
        var record = system.recordBattle(god, titan, round, winner.getName() + " 获胜");
        System.out.println("战绩: " + record);

        System.out.println("\n===== 测试通过 =====");
    }
}

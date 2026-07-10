package com.olympus.entity;

/**
 * 战斗记录实体类 - 记录每场战斗的结果
 */
public class BattleRecord {
    private String godName;       // 神方名称
    private String titanName;     // 泰坦方名称
    private String winner;        // 胜利者名称
    private int rounds;           // 战斗回合数
    private String battleTime;    // 战斗时间
    private String summary;       // 战斗摘要

    public BattleRecord(String godName, String titanName, String winner,
                        int rounds, String battleTime, String summary) {
        this.godName = godName;
        this.titanName = titanName;
        this.winner = winner;
        this.rounds = rounds;
        this.battleTime = battleTime;
        this.summary = summary;
    }

    public String getGodName() { return godName; }
    public String getTitanName() { return titanName; }
    public String getWinner() { return winner; }
    public int getRounds() { return rounds; }
    public String getBattleTime() { return battleTime; }
    public String getSummary() { return summary; }

    @Override
    public String toString() {
        return String.format("[%s] %s vs %s -> 胜者:%s (%d回合)",
                battleTime, godName, titanName, winner, rounds);
    }
}

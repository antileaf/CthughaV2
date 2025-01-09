package com.cthugha.utils;

@Deprecated
public class StaticHelper {
    public static int usedEnergy = 0; // 已使用能量
    public static int canBaoYanNums = 6; // 爆炎的炎之精数量

    public static void resetvaluesAtTurnStart() {
        usedEnergy = 0;
    }

    public static void resetvaluesAtBattleStart() {
        usedEnergy = 0;
        canBaoYanNums = 6;
    }

}

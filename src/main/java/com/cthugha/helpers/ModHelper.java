package com.cthugha.helpers;

import com.cthugha.cards.Defend;
import com.cthugha.enums.CustomTags;
import com.cthugha.relics.LieSiTaShuJian;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

public class ModHelper {
    public static String MakePath(String id) {
        return "Cthugha:" + id;
    }

    // 灼伤
    public static boolean IsBurn(AbstractCard card) {
        return card.cardID == "Burn";
    }

    // 灼伤牌
    public static boolean IsBurnCard(AbstractCard card) {
        return card.cardID == "Burn" || card.hasTag(CustomTags.Burn_Card)
                || AbstractDungeon.player.hasRelic(LieSiTaShuJian.ID);
    }

    // 打击牌
    public static boolean IsStrikeCard(AbstractCard card) {
        return card.hasTag(AbstractCard.CardTags.STRIKE);
    }

    // 防御牌
    public static boolean IsDefendCard(AbstractCard card) {
        return card.cardID.equals(Defend.ID); // TODO: 我觉得 instanceof 好点
    }

    public static boolean isInBattle() {
        return CardCrawlGame.dungeon != null &&
                AbstractDungeon.isPlayerInDungeon() &&
                AbstractDungeon.currMapNode != null &&
                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
    }
}

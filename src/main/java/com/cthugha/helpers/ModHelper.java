package com.cthugha.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.cthugha.cards.Defend;
import com.cthugha.enums.CustomTags;
import com.cthugha.relics.LieSiTaShuJian;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;

public class ModHelper {
    public static String MakePath(String id) {
        return "Cthugha:" + id;
    }

    // 灼伤
    public static boolean IsBurn(AbstractCard card) {
        return card.cardID.equals("Burn");
    }

    // 灼伤牌
    public static boolean IsBurnCard(AbstractCard card) {
        return card.cardID.equals("Burn") || card.hasTag(CustomTags.Burn_Card)
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

    public static final ArrayList<AbstractGameAction> buffer = new ArrayList<>();

    public static void addToBuffer(AbstractGameAction action) {
        buffer.add(action);
    }

    public static void commitBuffer() {
        for (int i = buffer.size() - 1; i >= 0; i--)
            AbstractDungeon.actionManager.addToTop(buffer.get(i));
        buffer.clear();
    }

    public static final Texture BurnAttack = ImageMaster.loadImage("cthughaResources/img/card/灼伤_p.png");
    public static final TextureAtlas.AtlasRegion BurnAttackRegion = new TextureAtlas.AtlasRegion(BurnAttack, 0, 0,
            BurnAttack.getWidth(), BurnAttack.getHeight());

    public static final TextureAtlas cardAtlas = new TextureAtlas(Gdx.files.internal("cards/cards.atlas"));
    public static final TextureAtlas.AtlasRegion BurnRegion = cardAtlas.findRegion("status/burn");

    public static void changeBurn(AbstractCard c) {
        if (c.type == AbstractCard.CardType.STATUS) {
            c.type = AbstractCard.CardType.ATTACK;
            c.target = AbstractCard.CardTarget.ENEMY;

            c.baseDamage = c.baseMagicNumber;
            c.rawDescription = "造成 !D! 点伤害。";
            c.initializeDescription();
            // c.applyPowers();

            c.jokePortrait = c.portrait = BurnAttackRegion;
        } else if (c.type == AbstractCard.CardType.ATTACK) {
            c.type = AbstractCard.CardType.STATUS;
            c.target = AbstractCard.CardTarget.NONE;
            c.damage = c.baseDamage = -1;

            c.rawDescription = CardCrawlGame.languagePack.getCardStrings("Burn").DESCRIPTION;
            if (c.upgraded) {
                c.rawDescription = CardCrawlGame.languagePack.getCardStrings("Burn").UPGRADE_DESCRIPTION;
            }
            c.initializeDescription();

            c.jokePortrait = c.portrait = BurnRegion;
        }
    }

    public static AbstractCard getAttackBurn() {
        AbstractCard tmp = new Burn();
        ModHelper.changeBurn(tmp);
        return tmp;
    }
}

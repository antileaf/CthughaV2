package com.cthugha.patch;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.power.FenJiPower;
import com.cthugha.power.TaoHuoShiYanPower;
import com.cthugha.relics.HuoTiHuoYan;
import com.cthugha.relics.ShengLingLieYan;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class BurnPatch {

    @SpirePatch(clz = Burn.class, method = SpirePatch.CONSTRUCTOR)
    public static class MeteorStrikePatch {
        @SpirePostfixPatch
        public static void Postfix(Burn _inst) {
            if (AbstractDungeon.player != null && AbstractDungeon.player.hasPower(TaoHuoShiYanPower.POWER_ID)) {
                _inst.upgrade();
            }
        }
    }

    @SpirePatch(clz = Burn.class, method = "triggerOnEndOfTurnForPlayingCard")
    public static class Renpi_player11 {
        @SpirePrefixPatch
        public static SpireReturn Prefix(AbstractCard _inst) {
            if (_inst.type != CardType.STATUS) {
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = Burn.class, method = "upgrade")
    public static class Renpi_player12 {
        public static SpireReturn Postfix(AbstractCard _inst) {
            if (_inst.type != CardType.STATUS) {
                _inst.rawDescription = "造成 !D! 点伤害。";
                _inst.initializeDescription();
                _inst.baseDamage = _inst.baseMagicNumber;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = Burn.class, method = "use", paramtypez = { AbstractPlayer.class, AbstractMonster.class })
    public static class Renpi_player {
        public static void Replace(AbstractCard _inst, AbstractPlayer p, AbstractMonster m) {
            if (AbstractDungeon.player.hasPower(FenJiPower.POWER_ID)) {
                AbstractPower power = AbstractDungeon.player.getPower(FenJiPower.POWER_ID);
                _inst.magicNumber += power.amount;
                _inst.damage += power.amount;
            }
            if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID)) {
                _inst.magicNumber += 3;
                _inst.damage += 3;
            }
            if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID)) {
                _inst.magicNumber += 8;
                _inst.damage += 8;
            }

            if (_inst.type == CardType.ATTACK) { // 攻击牌
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(m,
                                new DamageInfo(AbstractDungeon.player, _inst.damage,
                                        DamageInfo.DamageType.NORMAL),
                                AbstractGameAction.AttackEffect.NONE));
            } else { // 自动打出
                if (_inst.dontTriggerOnUseCard) {
                    if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID)) {
                        AbstractDungeon.actionManager.addToBottom(
                                new DamageRandomEnemyAction(
                                        new DamageInfo(AbstractDungeon.player, _inst.magicNumber,
                                                DamageInfo.DamageType.THORNS),
                                        AbstractGameAction.AttackEffect.FIRE));
                    } else if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID)) {
                        AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(null, true,
                                AbstractDungeon.cardRandomRng);
                        if (monster != null) {
                            AbstractDungeon.actionManager.addToBottom(
                                    new DamageAction(monster,
                                            new DamageInfo(AbstractDungeon.player, _inst.magicNumber,
                                                    DamageInfo.DamageType.THORNS),
                                            AbstractGameAction.AttackEffect.FIRE));
                            AbstractDungeon.actionManager
                                    .addToBottom(new DecreaseMonsterMaxHealthAction(monster, _inst.magicNumber));
                        }
                    } else {
                        AbstractDungeon.actionManager
                                .addToBottom(new DamageAction(AbstractDungeon.player,
                                        new DamageInfo(AbstractDungeon.player, _inst.magicNumber,
                                                DamageInfo.DamageType.THORNS),
                                        AbstractGameAction.AttackEffect.FIRE));
                    }
                }
            }

        }
    }

}

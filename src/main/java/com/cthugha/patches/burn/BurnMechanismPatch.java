package com.cthugha.patches.burn;

import basemod.ReflectionHacks;
import com.cthugha.actions.DecreaseMonsterMaxHealthAction;
import com.cthugha.blight.TheBurningOne;
import com.cthugha.characters.Cthugha;
import com.cthugha.orbs.FireVampire;
import com.cthugha.power.WuYouBuZhuPower;
import com.cthugha.relics.cthugha.EmeraldTabletVolumeVII;
import com.cthugha.utils.CthughaHelper;
import com.cthugha.power.FenJiPower;
import com.cthugha.power.RiShiPower;
import com.cthugha.relics.cthugha.HuoTiHuoYan;
import com.cthugha.relics.cthugha.ShengLingLieYan;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.TriggerEndOfTurnOrbsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class BurnMechanismPatch {
	private static final Logger logger = LogManager.getLogger(BurnMechanismPatch.class.getName());

	public static boolean hasCthughaBlight() {
		return AbstractDungeon.player.hasBlight(TheBurningOne.ID);
	}

	private static String getCardStringForBurn(AbstractCard c) {
		CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(CthughaHelper.makeID("Burn"));

		if (c.type == AbstractCard.CardType.ATTACK)
			return !CthughaHelper.isInBattle() || !AbstractDungeon.player.hasRelic(ShengLingLieYan.ID) ?
					cardStrings.EXTENDED_DESCRIPTION[2] :
					cardStrings.EXTENDED_DESCRIPTION[3];

		if (CthughaHelper.isInBattle() && hasCthughaBlight())
			return !AbstractDungeon.player.hasRelic(ShengLingLieYan.ID) ?
					cardStrings.EXTENDED_DESCRIPTION[0] :
					cardStrings.EXTENDED_DESCRIPTION[1];

		return CardCrawlGame.languagePack.getCardStrings("Burn").DESCRIPTION;
	}

	@SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy", paramtypez = {})
	public static class MakeStatEquivalentCopyPatch {
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
				int[] tmp = LineFinder.findAllInOrder(ctBehavior,
						new Matcher.MethodCallMatcher(AbstractCard.class, "makeCopy"));
				return new int[] { tmp[0] + 1 };
			}
		}

		@SpireInsertPatch(locator = Locator.class, localvars = {"card"})
		public static void Insert(AbstractCard _inst, AbstractCard card) {
			if (_inst instanceof Burn && _inst.type == AbstractCard.CardType.ATTACK) {
				CthughaHelper.changeBurn(card);
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "initializeDescription", paramtypez = {})
	public static class InitializeDescriptionPatch {
		@SpirePrefixPatch
		public static void Prefix(AbstractCard _inst) {
			if (_inst instanceof Burn)
				_inst.rawDescription = getCardStringForBurn(_inst);
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "applyPowers", paramtypez = {})
	public static class ApplyPowersPatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst) {
			if (_inst instanceof Burn && _inst.type == AbstractCard.CardType.STATUS && hasCthughaBlight()) {
				Burn burn = (Burn) _inst;

				int bonus = 0;
				if (AbstractDungeon.player.hasRelic(HuoTiHuoYan.ID))
					bonus += HuoTiHuoYan.BONUS;
				if (AbstractDungeon.player.hasRelic(ShengLingLieYan.ID))
					bonus += ShengLingLieYan.BONUS;

				if (AbstractDungeon.player.hasPower(FenJiPower.POWER_ID))
					bonus += AbstractDungeon.player.getPower(FenJiPower.POWER_ID).amount;

				if (AbstractDungeon.player.hasPower(VigorPower.POWER_ID) &&
						AbstractDungeon.player.hasRelic(EmeraldTabletVolumeVII.ID)) {
					int vigor = Math.min(AbstractDungeon.player.getPower(VigorPower.POWER_ID).amount,
							burn.baseMagicNumber + bonus);

					bonus += vigor;
				}

				burn.magicNumber = burn.baseMagicNumber + bonus;
				burn.isMagicNumberModified = burn.magicNumber != burn.baseMagicNumber;
			}
		}
	}

	@SpirePatch(clz = AbstractCard.class, method = "calculateCardDamage",
			paramtypez = {AbstractMonster.class})
	public static class CalculateCardDamagePatch {
		@SpirePostfixPatch
		public static void Postfix(AbstractCard _inst, AbstractMonster mo) {
			ApplyPowersPatch.Postfix(_inst); // 懒了
		}
	}

	@SpirePatch(clz = Burn.class, method = "upgrade", paramtypez = {})
	public static class UpgradePatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.FieldAccessMatcher(AbstractCard.class, "rawDescription"));
//			}
//		}
//
//		@SpireInsertPatch(locator = Locator.class)
//		public static void Insert(Burn _inst) {
//			if (_inst.type == AbstractCard.CardType.ATTACK)
//				ReflectionHacks.privateMethod(AbstractCard.class, "upgradeDamage", int.class)
//						.invoke(_inst, 2);
//		}

		@SpirePrefixPatch
		public static void Prefix(Burn _inst) {
			if (!_inst.upgraded && _inst.type == AbstractCard.CardType.ATTACK)
				ReflectionHacks.privateMethod(AbstractCard.class, "upgradeDamage", int.class)
						.invoke(_inst, 2);
		}
	}

	@SpirePatch(clz = Burn.class, method = "triggerOnEndOfTurnForPlayingCard", paramtypez = {})
	public static class TriggerOnEndOfTurnForPlayingCardPatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(Burn _inst) {
//			logger.info("type = {}, hasCthughaBlight = {}, vampireMap.containsKey = {}",
//					_inst.type, hasCthughaBlight(), vampireMap.containsKey(_inst));
//			if ((_inst.type == AbstractCard.CardType.STATUS && hasCthughaBlight()) ||
//					vampireMap.containsKey(_inst)) {
//				return SpireReturn.Continue();
//			}
//			return SpireReturn.Return();

			if (_inst.type == AbstractCard.CardType.ATTACK) {
//				_inst.dontTriggerOnUseCard = true;
//				CardQueueItem item = new CardQueueItem(_inst, true);
//				item.autoplayCard = true;
//				AbstractDungeon.actionManager.cardQueue.add(item);

				logger.debug("矢野我测你的码");

//				return SpireReturn.Return();

//				CthughaHelper.changeBurn(_inst);
			}

			return SpireReturn.Continue();
		}
	}

	private static Map<AbstractCard, FireVampire> vampireMap = new HashMap<>();
	private static ArrayList<AbstractCard> changed = new ArrayList<>();

	@SpirePatch(clz = TriggerEndOfTurnOrbsAction.class, method = "update", paramtypez = {})
	public static class PreWorkPatch {
//		private static class Locator extends SpireInsertLocator {
//			@Override
//			public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
//				return LineFinder.findInOrder(ctBehavior,
//						new Matcher.FieldAccessMatcher(AbstractPlayer.class, "hand"));
//			}
//		}

		public static void Postfix(TriggerEndOfTurnOrbsAction _inst) {
			if (!_inst.isDone)
				return;

			vampireMap.clear();

			ArrayList<FireVampire> vampires = new ArrayList<>();
			for (AbstractOrb orb : AbstractDungeon.player.orbs)
				if (orb instanceof FireVampire) {
					FireVampire vampire = (FireVampire) orb;
					while (vampire.charge > 0) {
						vampires.add(vampire);
						vampire.charge--;
					}
				}

			for (AbstractCard card : AbstractDungeon.player.hand.group)
				if (card instanceof Burn) {
					FireVampire vampire = vampires.isEmpty() ? null : vampires.remove(0);
					if (vampire == null)
						break;

					vampireMap.put(card, vampire);
					if (vampires.isEmpty())
						break;
				}

			logger.info("vampireMap.size() = {}", vampireMap.size());

			ArrayList<CardQueueItem> toRemove = new ArrayList<>();
			for (CardQueueItem item : AbstractDungeon.actionManager.cardQueue)
				if (item.card instanceof Burn && item.card.dontTriggerOnUseCard) {
					if (item.card.type != AbstractCard.CardType.STATUS &&
							!vampireMap.containsKey(item.card)) {
						toRemove.add(item);
						item.card.dontTriggerOnUseCard = false;

						logger.info("Remove, item.card = {}", item.card);
				}
			}
			AbstractDungeon.actionManager.cardQueue.removeAll(toRemove);

			changed.clear();

			for (AbstractCard card : vampireMap.keySet())
				if (card.type == AbstractCard.CardType.ATTACK) {
					CthughaHelper.changeBurn(card);
					changed.add(card);
				}
		}
	}

	@SpirePatch(clz = Burn.class, method = "use", paramtypez = {AbstractPlayer.class, AbstractMonster.class})
	public static class UsePatch {
		@SpirePrefixPatch
		public static SpireReturn<Void> Prefix(Burn _inst, AbstractPlayer p, AbstractMonster m) {
			logger.info("Burn type = {}, dontTriggerOnUseCard = {}", _inst.type, _inst.dontTriggerOnUseCard);

			if (!_inst.dontTriggerOnUseCard) {
				if (_inst.type == AbstractCard.CardType.ATTACK) {
					if (p.hasPower(RiShiPower.POWER_ID))
						AbstractDungeon.actionManager.addToBottom(
								((RiShiPower) p.getPower(RiShiPower.POWER_ID)).getAction(m));

					if (!p.hasRelic(ShengLingLieYan.ID))
						AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
								new DamageInfo(p, _inst.damage, DamageInfo.DamageType.NORMAL),
								AbstractGameAction.AttackEffect.FIRE));
					else {
						AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
								new DamageInfo(p, _inst.damage, DamageInfo.DamageType.HP_LOSS),
								AbstractGameAction.AttackEffect.FIRE));

						AbstractDungeon.actionManager.addToBottom(new DecreaseMonsterMaxHealthAction(m, _inst.damage));

						if (p.hasPower(WuYouBuZhuPower.POWER_ID)) {
							p.getPower(WuYouBuZhuPower.POWER_ID).flash();
							AbstractDungeon.actionManager.addToBottom(
									new DecreaseMonsterMaxHealthAction(m, _inst.damage));
						}
					}
					return SpireReturn.Return();
				}
				else
					return SpireReturn.Continue();
			}
			else if (vampireMap.containsKey(_inst) || hasCthughaBlight()) {
				AbstractMonster monster = AbstractDungeon.getMonsters().getRandomMonster(
						null, true, AbstractDungeon.cardRandomRng);
				if (monster != null) {
					if (p.hasPower(RiShiPower.POWER_ID))
						AbstractDungeon.actionManager.addToBottom(
								((RiShiPower) p.getPower(RiShiPower.POWER_ID)).getAction(monster));

					int damage = _inst.magicNumber;
					FireVampire vampire = vampireMap.get(_inst);
					if (vampire != null) {
						logger.info("vampire != null");
						damage += FireVampire.BONUS;
						vampire.passiveVFX(monster);
					}

					AbstractDungeon.actionManager.addToBottom(new DamageAction(monster,
							new DamageInfo(p, damage, DamageInfo.DamageType.THORNS),
							AbstractGameAction.AttackEffect.FIRE));

					if ((vampire == null && AbstractDungeon.player.hasRelic(ShengLingLieYan.ID)) ||
							(vampire != null && AbstractDungeon.player.hasPower(WuYouBuZhuPower.POWER_ID))) {
						if (vampire != null)
							AbstractDungeon.player.getPower(WuYouBuZhuPower.POWER_ID).flash();

						AbstractDungeon.actionManager.addToBottom(
								new DecreaseMonsterMaxHealthAction(monster, damage));
					}
				}

				if (changed.contains(_inst)) {
					CthughaHelper.changeBurn(_inst);
					changed.remove(_inst);
				}

				return SpireReturn.Return();
			}
			else
				return SpireReturn.Continue();
		}
	}
}

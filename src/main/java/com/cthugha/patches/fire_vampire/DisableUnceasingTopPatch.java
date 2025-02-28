package com.cthugha.patches.fire_vampire;

import basemod.ReflectionHacks;
import com.cthugha.actions.utils.AnonymousAction;
import com.cthugha.orbs.FireVampire;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.defect.TriggerEndOfTurnOrbsAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.UnceasingTop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class DisableUnceasingTopPatch { // 矢野我测你的码
	private static final Logger logger = LogManager.getLogger(DisableUnceasingTopPatch.class.getName());

	@SpirePatch(clz = TriggerEndOfTurnOrbsAction.class, method = "update", paramtypez = {})
	public static class TriggerEndOfTurnOrbsActionPatch {
		@SpirePrefixPatch
		public static void Prefix(TriggerEndOfTurnOrbsAction _inst) {
			if (AbstractDungeon.player.hasRelic(UnceasingTop.ID) &&
			AbstractDungeon.player.orbs.stream()
					.anyMatch(o -> o instanceof FireVampire))
				AbstractDungeon.actionManager.addToBottom(new AnonymousAction(() -> {
					ReflectionHacks.setPrivate(AbstractDungeon.player.getRelic(UnceasingTop.ID),
							UnceasingTop.class, "disabledUntilEndOfTurn", true);

					logger.info("Unceasing Top Disabled");
				}));
		}

//		@SpirePostfixPatch
//		public static void Postfix(TriggerEndOfTurnOrbsAction _inst) {
//			if (AbstractDungeon.player.hasRelic(UnceasingTop.ID))
//				AbstractDungeon.actionManager.addToBottom(new AnonymousAction(() -> {
//					ReflectionHacks.setPrivate(AbstractDungeon.player.getRelic(UnceasingTop.ID),
//							UnceasingTop.class, "disabledUntilEndOfTurn", false);
//
//					logger.info("Unceasing Top Enabled");
//				}));
//		}
	}
}

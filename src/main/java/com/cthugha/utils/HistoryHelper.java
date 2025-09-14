package com.cthugha.utils;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.cthugha.cards.cthugha.*;
import com.cthugha.dungeons.the_tricuspid_gate.monsters.Areshkagal;
import com.cthugha.enums.MyPlayerClassEnum;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.runHistory.RunHistoryPath;
import com.megacrit.cardcrawl.screens.runHistory.RunPathElement;
import com.megacrit.cardcrawl.screens.stats.BattleStats;
import com.megacrit.cardcrawl.screens.stats.RunData;
import me.antileaf.signature.utils.SignatureHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class HistoryHelper {
	private static final Logger logger = LogManager.getLogger(HistoryHelper.class.getName());

	public static boolean checkIsDeified(RunHistoryPath path) {
		if (path != null && !path.pathElements.isEmpty()) {
			boolean hasDefeatedAreshkagal = path.pathElements.stream()
					.filter(e -> e.nodeType == RunPathElement.PathNodeType.BOSS)
					.map(e -> (BattleStats) ReflectionHacks
							.getPrivate(e, RunPathElement.class, "battleStats"))
					.filter(Objects::nonNull)
					.anyMatch(s -> s.enemies.equals(Areshkagal.ID));

			RunPathElement last = path.pathElements.get(path.pathElements.size() - 1);

			return hasDefeatedAreshkagal && last.nodeType != RunPathElement.PathNodeType.BOSS;
		}

		return false;
	}

	public static RunHistoryPath getPath(RunData data) {
		RunHistoryPath path = new RunHistoryPath();
		path.setRunData(data);

		return path;
	}

	private static Stats checkHistories() {
		Stats res = new Stats();

		FileHandle[] subfolders = Gdx.files.local("runs" + File.separator).list();

		for (FileHandle subfolder : subfolders) {
			if (CardCrawlGame.saveSlot == 0) {
				if (subfolder.name().contains("0_") || subfolder.name().contains("1_") ||
						subfolder.name().contains("2_"))
					continue;
			}
			else {
				if (!subfolder.name().contains(CardCrawlGame.saveSlot + "_"))
					continue;
			}

			if (!subfolder.name().contains(MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS.name()) &&
					Arrays.stream(AbstractPlayer.PlayerClass.values())
							.noneMatch(c -> subfolder.name().contains(c.name())))
				continue;

			Gson gson = new Gson();

			for (FileHandle file : subfolder.list()) {
				RunData data;

				try {
					data = gson.fromJson(file.readString(), RunData.class);
					if (data != null) {
						try {
							AbstractPlayer.PlayerClass.valueOf(data.character_chosen);
						}
						catch (NullPointerException | IllegalArgumentException e) {
							continue;
						}
					}
				}
				catch (JsonSyntaxException e) {
					continue;
				}

				if (data == null)
					continue;

				boolean isA20 = data.is_ascension_mode && data.ascension_level >= 20;
				boolean isCthugha = data.character_chosen.equals(MyPlayerClassEnum.CTHUGHA_PLAYER_CLASS.name());
				boolean isOriginalChar = Arrays.stream(AbstractPlayer.PlayerClass.values())
						.anyMatch(c -> c.name().equals(data.character_chosen));

				boolean deified = checkIsDeified(getPath(data));

				if (deified)
					res.deified = true;

				if (deified && isA20 && isCthugha)
					res.deifiedA20 = true;

				if (deified && isA20 && isOriginalChar)
					res.originalCharA20 = true;
			}
		}

		return res;
	}

	public static class Stats {
		public boolean deified = false;
		public boolean deifiedA20 = false;
		public boolean originalCharA20 = false;
	}

	public static void runCheck() {
		HistoryHelper.Stats stats = HistoryHelper.checkHistories();

		if (stats.deified) {
			SignatureHelper.unlock(Burn.ID, true);
			SignatureHelper.unlock(ShiftingStar.ID, true);
			logger.info("Has defeated Areshkagal before, Signature of Burn and Shifting Star unlocked!");
		}

		ArrayList<String> A20 = new ArrayList<String>() {{
			add(DaoHuoShiYan.ID);
			add(HeLuSiZhiYan.ID);
			add(YingHuoLiHuo.ID);
		}};

		ArrayList<String> OriginalCharA20 = new ArrayList<String>() {{
			add(ChiYanHuaZhan.ID);
		}};

		if (stats.deifiedA20) {
			A20.forEach(id -> {
				SignatureHelper.unlock(id, true);
			});

			logger.info("Has defeated Areshkagal A20 before, Signature of A20 cards unlocked!");
		}

		if (stats.originalCharA20) {
			OriginalCharA20.forEach(id -> {;
				SignatureHelper.unlock(id, true);
			});

			logger.info("Has completed A20 as original character before, Signature of 黄昏与白银 unlocked!");
		}
	}
}

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.cthugha.actions.utils;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import java.util.ArrayList;
import java.util.Arrays;

public class AnonymousWithDurationAction extends AbstractGameAction {
	ArrayList<Runnable> runnableList;
	
	public AnonymousWithDurationAction(float duration, Runnable... runnableList) {
		this.actionType = ActionType.SPECIAL;
		
		this.duration = this.startDuration = duration;
		this.runnableList = new ArrayList<>();
		this.runnableList.addAll(Arrays.asList(runnableList));
	}
	
	public void update() {
		if (this.duration == this.startDuration) {
			for (Runnable o : this.runnableList)
				if (o != null)
					o.run();
		}
		
		this.tickDuration();
	}
}

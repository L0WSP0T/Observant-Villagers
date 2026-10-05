package com.stereowalker.obville.dat;

import net.minecraft.nbt.CompoundTag;

public class Reput {
	public boolean generatedBounty = false;
	public boolean droppedBounty = false;
	public boolean hasSpokenToLeader = false;
	public boolean hasCommitedCrimeBefore = false;
	public boolean hasHeardLeaderGreeting = false;
	public int undeadKills = 0;
	
	public static Reput read(CompoundTag tag) {
		Reput rep = new Reput();
		rep.generatedBounty = tag.getBoolean("generatedBounty");
		rep.droppedBounty = tag.getBoolean("droppedBounty");
		rep.hasSpokenToLeader = tag.getBoolean("hasSpokenToLeader");
		rep.hasCommitedCrimeBefore = tag.getBoolean("hasCommitedCrimeBefore");
		rep.hasHeardLeaderGreeting = tag.getBoolean("hasHeardLeaderGreeting");
		rep.undeadKills = tag.getInt("undeadKills");
		return rep;
	}
	
	public CompoundTag write() {
		CompoundTag tag = new CompoundTag();
		tag.putBoolean("generatedBounty", generatedBounty);
		tag.putBoolean("droppedBounty", droppedBounty);
		tag.putBoolean("hasSpokenToLeader", hasSpokenToLeader);
		tag.putBoolean("hasCommitedCrimeBefore", hasCommitedCrimeBefore);
		tag.putBoolean("hasHeardLeaderGreeting", hasHeardLeaderGreeting);
		tag.putInt("undeadKills", undeadKills);
		return tag;
	}
}

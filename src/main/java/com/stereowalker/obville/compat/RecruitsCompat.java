package com.stereowalker.obville.compat;

import java.util.List;
import java.util.function.Predicate;

import com.stereowalker.obville.Crime;
import com.stereowalker.obville.ObVille;
import com.stereowalker.obville.network.protocol.game.ClientboundVillagerMessagePacket;
import com.talhanation.recruits.entities.AbstractRecruitEntity;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;

public class RecruitsCompat {

	public static boolean isRecruit(Entity entity) {
		return entity instanceof AbstractRecruitEntity;
	}

	public static boolean isHiredBy(AbstractRecruitEntity recruit, Player player) {
		return recruit.isOwnedBy(player);
	}

	public static void tryToAnger(Player player, boolean angerOnlyIfCanSee, List<LivingEntity> angeredEntities, List<Villager> villagers) {
		Predicate<AbstractRecruitEntity> shouldWitness = recruit -> 
			!isHiredBy(recruit, player) && (villagers.size() > 0 || !angerOnlyIfCanSee || ObVille.isLookingAtPlayer(recruit, player));
		List<AbstractRecruitEntity> recruits = player.level.getEntitiesOfClass(AbstractRecruitEntity.class, player.getBoundingBox().inflate(16.0));
		recruits.stream().filter(shouldWitness).forEach(angeredEntities::add);
	}

	public static void wit(Player player, List<LivingEntity> angeredRecruits, Crime crimeCommited) {
		if (angeredRecruits.isEmpty() || !(player instanceof ServerPlayer sPlayer)) return;

		// When they see a crime, they either say nothing or say "I'm reporting that."
		if (player.getRandom().nextBoolean()) {
			LivingEntity reporter = angeredRecruits.get(player.getRandom().nextInt(angeredRecruits.size()));
			List<String> reportingLines = ObVille.LINES_CONFIG.recruit_reporting;
			String line = (reportingLines != null && !reportingLines.isEmpty()) 
					? reportingLines.get(player.getRandom().nextInt(reportingLines.size())) 
					: "I'm reporting that.";
			new ClientboundVillagerMessagePacket(reporter.getName().copy().append(": ").append(new TextComponent(line)), player.getUUID()).send(sPlayer);
		}
	}

	public static void target(LivingEntity liv, Player player) {
		if (liv instanceof AbstractRecruitEntity recruit && !isHiredBy(recruit, player)) {
			recruit.setTarget(player);
		}
	}

	public static void angerNearbyAtExiled(ServerPlayer player) {
		List<AbstractRecruitEntity> recruits = player.level.getEntitiesOfClass(AbstractRecruitEntity.class, player.getBoundingBox().inflate(20.0));
		for (AbstractRecruitEntity recruit : recruits) {
			if (!isHiredBy(recruit, player) && recruit.getTarget() == null && ObVille.isLookingAtPlayer(recruit, player)) {
				recruit.setTarget(player);
			}
		}
	}
}

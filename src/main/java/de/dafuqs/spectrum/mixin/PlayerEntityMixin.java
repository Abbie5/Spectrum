package de.dafuqs.spectrum.mixin;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import de.dafuqs.spectrum.enchantments.ImprovedCriticalEnchantment;
import de.dafuqs.spectrum.entity.entity.SpectrumFishingBobberEntity;
import de.dafuqs.spectrum.helpers.SpectrumEnchantmentHelper;
import de.dafuqs.spectrum.interfaces.PlayerEntityAccessor;
import de.dafuqs.spectrum.items.ExperienceStorageItem;
import de.dafuqs.spectrum.items.trinkets.AttackRingItem;
import de.dafuqs.spectrum.items.trinkets.SpectrumTrinketItem;
import de.dafuqs.spectrum.progression.SpectrumAdvancementCriteria;
import de.dafuqs.spectrum.registries.SpectrumEnchantments;
import de.dafuqs.spectrum.registries.SpectrumItems;
import de.dafuqs.spectrum.registries.SpectrumStatusEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEntityAccessor {
	
	@Shadow public abstract Iterable<ItemStack> getItemsHand();
	
	@Shadow public abstract void increaseStat(Identifier stat, int amount);
	
	public SpectrumFishingBobberEntity spectrum$fishingBobber;
	
	@Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
	protected void spectrum$calculateModifiers(Entity target, CallbackInfo ci) {
		if ((Object) this instanceof PlayerEntity thisPlayerEntity) {
			Multimap<EntityAttribute, EntityAttributeModifier> map = Multimaps.newMultimap(Maps.newLinkedHashMap(), ArrayList::new);
			
			EntityAttributeModifier jeopardantModifier;
			if (SpectrumTrinketItem.hasEquipped(thisPlayerEntity, SpectrumItems.JEOPARDANT)) {
				jeopardantModifier = new EntityAttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_UUID, "spectrum:attack_ring", AttackRingItem.getAttackModifierForEntity(thisPlayerEntity), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
			} else {
				jeopardantModifier = new EntityAttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_UUID, "spectrum:attack_ring", 0, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
			}
			map.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, jeopardantModifier);
			
			int improvedCriticalLevel = SpectrumEnchantmentHelper.getUsableLevel(SpectrumEnchantments.IMPROVED_CRITICAL, thisPlayerEntity.getMainHandStack(), thisPlayerEntity);
			EntityAttributeModifier improvedCriticalModifier = new EntityAttributeModifier(AttackRingItem.ATTACK_RING_DAMAGE_UUID, "spectrum:improved_critical", ImprovedCriticalEnchantment.getCritMultiplier(improvedCriticalLevel), EntityAttributeModifier.Operation.ADDITION);
			map.put(AdditionalEntityAttributes.CRITICAL_BONUS_DAMAGE, improvedCriticalModifier);
			
			thisPlayerEntity.getAttributes().addTemporaryModifiers(map);
		}
	}
	
	@Inject(at = @At("TAIL"), method = "jump()V")
	protected void spectrum$jumpAdvancementCriterion(CallbackInfo ci) {
		if ((Object) this instanceof ServerPlayerEntity serverPlayerEntity) {
			SpectrumAdvancementCriteria.TAKE_OFF_BELT_JUMP.trigger(serverPlayerEntity);
		}
	}
	
	@Inject(at = @At("TAIL"), method = "isInvulnerableTo(Lnet/minecraft/entity/damage/DamageSource;)Z", cancellable = true)
	public void spectrum$isInvulnerableTo(DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValue() && damageSource.isFire() && SpectrumTrinketItem.hasEquipped((PlayerEntity) (Object) this, SpectrumItems.ASHEN_CIRCLET)) {
			cir.setReturnValue(true);
		}
	}
	
	@Override
	public void setSpectrumBobber(SpectrumFishingBobberEntity bobber) {
		this.spectrum$fishingBobber = bobber;
	}
	
	@Override
	public SpectrumFishingBobberEntity getSpectrumBobber() {
		return this.spectrum$fishingBobber;
	}
	
	@Inject(at = @At("HEAD"), method = "canFoodHeal()Z", cancellable = true)
	public void canFoodHeal(CallbackInfoReturnable<Boolean> cir) {
		PlayerEntity player = (PlayerEntity) (Object) this;
		if(player.hasStatusEffect(SpectrumStatusEffects.SCARRED)) {
			cir.setReturnValue(false);
		}
	}
	
	// If the player holds an ExperienceStorageItem in their hands
	// experience is tried to get put in there first
	@ModifyVariable(at = @At("HEAD"), method = "addExperience(I)V", argsOnly = true)
	public int addExperience(int experience) {
		for(ItemStack stack : getItemsHand()) {
			if(stack.getItem() instanceof ExperienceStorageItem) {
				experience = ExperienceStorageItem.addStoredExperience(stack, experience);
				if(experience == 0) {
					break;
				}
			}
		}
		return experience;
	}
	
}
package tfar.heartshopmod.mixin;

import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import tfar.heartshopmod.PlayerDuck;

@Mixin(Player.class)
abstract class PlayerMixin implements PlayerDuck {

}
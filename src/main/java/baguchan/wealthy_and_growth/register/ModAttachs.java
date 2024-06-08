package baguchan.wealthy_and_growth.register;

import baguchan.wealthy_and_growth.WealthyAndGrowth;
import baguchan.wealthy_and_growth.capability.PlayerTargetCapability;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModAttachs {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, WealthyAndGrowth.MODID);

    public static final Supplier<AttachmentType<PlayerTargetCapability>> PLAYER_TARGET = ATTACHMENT_TYPES.register(
            "player_target", () -> AttachmentType.serializable(PlayerTargetCapability::new).build());
}
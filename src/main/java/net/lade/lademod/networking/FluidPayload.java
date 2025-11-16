package net.lade.lademod.networking;

import net.lade.lademod.LadeMod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;


public record FluidPayload(BlockPos pos, FluidStack fluidStack1, FluidStack fluidStack2, FluidStack fluidStack3) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<FluidPayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(LadeMod.MODID, "fluid_sync")
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidStack> EMPTY_ALLOWED_FLUID_CODEC = new StreamCodec<>() {
        @Override
        public @NotNull FluidStack decode(RegistryFriendlyByteBuf buf) {
            boolean isPresent = buf.readBoolean();
            if (isPresent) {
                return FluidStack.STREAM_CODEC.decode(buf);
            } else {
                return FluidStack.EMPTY;
            }
        }

        @Override
        public void encode(@NotNull RegistryFriendlyByteBuf buf, FluidStack stack) {
            boolean isPresent = !stack.isEmpty();
            buf.writeBoolean(isPresent);
            if (isPresent) {
                FluidStack.STREAM_CODEC.encode(buf, stack);
            }
        }
    };

    public static final StreamCodec<RegistryFriendlyByteBuf, FluidPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            FluidPayload::pos,

            EMPTY_ALLOWED_FLUID_CODEC,
            FluidPayload::fluidStack1,

            EMPTY_ALLOWED_FLUID_CODEC,
            FluidPayload::fluidStack2,

            EMPTY_ALLOWED_FLUID_CODEC,
            FluidPayload::fluidStack3,

            FluidPayload::new
    );

    public FluidStack getFluid(int i){
        return switch (i){
            case 0 -> fluidStack1;
            case 1 -> fluidStack2;
            case 2 -> fluidStack3;
            default -> throw new IllegalStateException("Unexprected Value: " + i + ", there can only be three Fluid Stacks in the Payload");
        };
    }

    public BlockPos getPos(){
        return pos;
    }

    @Override
    public CustomPacketPayload.@NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
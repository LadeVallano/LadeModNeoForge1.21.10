package net.lade.lademod.block.entity;

import net.lade.lademod.fluid.ModFluids;
import net.lade.lademod.networking.FluidPayload;
import net.lade.lademod.gui.GeoGenMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.ContainerUser;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public class GeoGenBlockEntity extends BlockEntity implements Container, MenuProvider {

    int menuNumber = 0;

    public static final int MAX_IN1_FLUID = 16000;

    public final static int WATER_PER_TICK = 4;
    public final static int LAVA_PER_TICK = 4;
    public final static int STEAM_PER_TICK = 4;

    public static final Fluid IN1_FLUID = Fluids.WATER;
    public static final Fluid IN2_FLUID = Fluids.LAVA;
    public static final Fluid OUT_FLUID = ModFluids.STEAM_FLUID.get();

    private final Set<ServerPlayer> viewingPlayers = Collections.newSetFromMap(new WeakHashMap<>());

    public GeoGenBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GEO_GEN_BLOCK_ENTITY_TYPE.get(), pos, state);
    }


    FluidStacksResourceHandler TANK = new FluidStacksResourceHandler(3, MAX_IN1_FLUID) {

        @Override
        protected void onContentsChanged(int index, @NotNull FluidStack previousContents) {
            super.onContentsChanged(index, previousContents);
            setChanged();
        }

        @Override
        public boolean isValid(int index, @NotNull FluidResource resource) {
            return switch (index){
                case 0 -> resource.getFluid() == IN1_FLUID;
                case 1 -> resource.getFluid() == IN2_FLUID;
                case 2 -> resource.getFluid() == OUT_FLUID;
                default -> throw new IllegalArgumentException("index 3+ out of bounds for tank size" + size());
            };
        }
    };

    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level == null || level.isClientSide()) return;
        if (generateSteam()) {
            setChanged();
        }
        if (menuNumber > 0) {
            sendData();
        }
    }

    private boolean generateSteam() {
        if (!TANK.getResource(0).isEmpty() && !TANK.getResource(1).isEmpty() && TANK.getAmountAsInt(2) > TANK.getCapacityAsInt(2, FluidResource.EMPTY)) {
            try (Transaction transaction = Transaction.openRoot()) {
                TANK.extract(0, TANK.getResource(0), WATER_PER_TICK, transaction);
                TANK.extract(1, TANK.getResource(1), LAVA_PER_TICK, transaction);
                int generatedSteam = TANK.insert(2, FluidResource.of(OUT_FLUID), STEAM_PER_TICK, transaction);
                if (generatedSteam > 0) {
                    transaction.commit();
                    return true;
                }
            }

        }
        return false;
    }

    public ResourceHandler<FluidResource> getFluidHandlerForSide(@Nullable Direction side) {
        return TANK;
    }

    public void sendData() {
        if (level == null || level.isClientSide()) return;

        for (ServerPlayer serverPlayer : viewingPlayers) {
            FluidStack in1Send = new FluidStack(TANK.getResource(0).getFluid(), TANK.getAmountAsInt(0));
            FluidStack in2Send = new FluidStack(TANK.getResource(1).getFluid(), TANK.getAmountAsInt(1));
            FluidStack outSend = new FluidStack(TANK.getResource(2).getFluid(), TANK.getAmountAsInt(2));
            if (in1Send.isEmpty() && in2Send.isEmpty() && outSend.isEmpty()) return;
            PacketDistributor.sendToPlayer(serverPlayer, new FluidPayload(this.worldPosition, in1Send, in2Send, outSend));
        }
    }


    @Override
    public void handleUpdateTag(@NotNull ValueInput input) {
        super.handleUpdateTag(input);
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        TANK.serialize(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        TANK.deserialize(input);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public @NotNull ItemStack getItem(int i) {
        return null;
    }

    @Override
    public @NotNull ItemStack removeItem(int i, int i1) {
        return null;
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int i) {
        return null;
    }

    @Override
    public void setItem(int i, @NotNull ItemStack itemStack) {

    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level == null) return;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.literal("Geothermal_Generator");
    }


    @Override
    public void startOpen(@NotNull ContainerUser user) {
        viewingPlayers.add((ServerPlayer) user);
        menuNumber++;
    }

    @Override
    public void stopOpen(@NotNull ContainerUser user) {
        viewingPlayers.remove((ServerPlayer) user);
        menuNumber--;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerID, @NotNull Inventory inventory, @NotNull Player player) {
        assert level != null;
        return new GeoGenMenu(containerID, inventory, ContainerLevelAccess.create(level, getBlockPos()), this);
    }
}

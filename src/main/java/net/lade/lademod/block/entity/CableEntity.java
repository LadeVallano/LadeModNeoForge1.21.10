package net.lade.lademod.block.entity;

import net.lade.lademod.ResourceNetwork.FluidNetwork;
import net.lade.lademod.ResourceNetwork.ResourceNetwork;
import net.lade.lademod.util.ModResourceUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.resource.Resource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CableEntity<R extends Resource> extends BlockEntity {

    public ResourceHandler<R> getInternalResourceStorage() {
        return null;
    }

    public void createResourceNetwork() {
        if(this instanceof FluidCableEntity fl){
            new FluidNetwork(fl);
        }

    }


    public CableEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.CABLE_ENTITY_TYPE.get(), pos, state);
    }

    public CableEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState) {
        super(entityType, blockPos, blockState);
    }


    public ResourceNetwork<R> getNetwork() {
        return null;
    }

    public void setNetwork(ResourceNetwork<R> tResourceNetwork) {

    }

    public enum outputDirection {
        PUSH, PULL
    }

    public void tick(Level level, BlockPos pos, BlockState blockState) {
    }

    protected int getMaxCap(){
        return 0;
    }

    protected ResourceHandler<R> getCapabilityFrom(BlockPos neighbourPos, Direction direction) {
        return null;
    }

    private void transferFluid(outputDirection fluidIO, List<Direction> directions) {
        if (level == null || level.isClientSide()) return;
        for (Direction direction : directions) {
            if (direction == null) continue;
            BlockPos neighbourPos = getBlockPos().relative(direction);

            ResourceHandler<R> neighbourHandler = getCapabilityFrom(neighbourPos, direction);

            if (neighbourHandler != null) {
                ResourceHandler<R> extractHandler = getInternalResourceStorage();
                ResourceHandler<R> insertHandler = getInternalResourceStorage();

                if (fluidIO == outputDirection.PUSH) {
                    insertHandler = neighbourHandler;
                }
                if (fluidIO == outputDirection.PULL) {
                    extractHandler = neighbourHandler;
                }
                ModResourceUtil.transferFromTo(extractHandler, insertHandler, getMaxCap());
            }
        }
    }

    protected List<Direction> pullDirections = new ArrayList<>(6);
    protected List<Direction> pushDirections = new ArrayList<>(Arrays.stream(Direction.values()).toList());

    @Override
    public void setChanged() {
        super.setChanged();
        if (level == null) return;
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput.TypedOutputList<Direction> pullDirectionsList = output.list("pullDirections", Direction.CODEC);
        ValueOutput.TypedOutputList<Direction> pushDirectionsList = output.list("pushDirections", Direction.CODEC);

        for (Direction pullDirection : pullDirections) {
            pullDirectionsList.add(pullDirection);
        }
        for (Direction pushDirection : pushDirections) {
            pushDirectionsList.add(pushDirection);
        }
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);
        pullDirections.clear();

        for (Direction pullDirection : input.listOrEmpty("pullDirections", Direction.CODEC)) {
            pullDirections.add(pullDirection);
        }

        for (Direction pushDirection : input.listOrEmpty("pushDirections", Direction.CODEC)) {
            pushDirections.add(pushDirection);
        }


    }

    public List<Direction> getPullDirections() {
        return pullDirections;
    }

    public List<Direction> getPushDirections() {
        return pushDirections;
    }
}




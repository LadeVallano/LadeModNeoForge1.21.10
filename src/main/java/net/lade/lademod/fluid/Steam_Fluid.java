package net.lade.lademod.fluid;

import net.lade.lademod.block.ModBlocks;
import net.lade.lademod.fluid.FluidType.ModFluidTypes;
import net.lade.lademod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Steam_Fluid extends BaseFlowingFluid {

    public Steam_Fluid(Properties properties) {
        super(properties);
    }

    public Steam_Fluid() {
        this(new Properties(ModFluidTypes.STEAM_FLUID_TYPE, ModFluids.STEAM_FLUID, ModFluids.STEAM_FLOW));
    }

    @Override
    public @NotNull Fluid getFlowing() {
        return ModFluids.STEAM_FLOW.get();
    }

    @Override
    public @NotNull Fluid getSource() {
        return ModFluids.STEAM_FLUID.get();
    }

    @Override
    public boolean isSource(@NotNull FluidState fluidState) {
        return true;
    }

    @Override
    public int getAmount(@NotNull FluidState fluidState) {
        return 0;
    }

    protected int getSlopeFindDistance(LevelReader worldIn) {
        return worldIn.dimensionType().ultraWarm() ? 1 : 2;
    }

    public boolean isSame(@NotNull Fluid fluid) {
        return fluid == ModFluids.STEAM_FLUID.get() || fluid == ModFluids.STEAM_FLOW.get();
    }

    protected int getDropOff(LevelReader worldIn) {
        return worldIn.dimensionType().ultraWarm() ? 1 : 2;
    }

    public int getTickDelay(LevelReader worldIn) {
        return worldIn.dimensionType().ultraWarm() ? 10 : 30;
    }


    @Nullable
    @Override
    protected ParticleOptions getDripParticle() {
        return super.getDripParticle();
    }

    public boolean supportsBoating(FluidState state, Boat boat) {
        return true;
    }

    public boolean canExtinguish(FluidState state, BlockGetter getter, BlockPos pos) {
        return false;
    }

    protected void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        BlockPos blockpos = pos.above();
        if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender()) {
            if (random.nextInt(100) == 0) {
                double d0 = (double) pos.getX() + random.nextDouble();
                double d1 = (double) pos.getY() + 1.0;
                double d2 = (double) pos.getZ() + random.nextDouble();
                level.addParticle(ParticleTypes.DRIPPING_WATER, d0, d1, d2, 0.0, 0.0, 0.0);
                level.playLocalSound(d0, d1, d2, SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }

            if (random.nextInt(200) == 0) {
                level.playLocalSound((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), SoundEvents.WATER_AMBIENT, SoundSource.BLOCKS, 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
            }
        }

    }

    public int getSpreadDelay(@NotNull Level level, @NotNull BlockPos pos, FluidState currentState, @NotNull FluidState newState) {
        int i = this.getTickDelay(level);
        if (!currentState.isEmpty() && !newState.isEmpty() && !(Boolean) currentState.getValue(FALLING) && !(Boolean) newState.getValue(FALLING) && newState.getHeight(level, pos) > currentState.getHeight(level, pos) && level.getRandom().nextInt(4) != 0) {
            i *= 4;
        }

        return i;
    }

    protected void spreadTo(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull Direction direction, @NotNull FluidState fluidState) {
        if (direction == Direction.DOWN) {
            FluidState fluidstate = level.getFluidState(pos);
            if (fluidstate.is(FluidTags.WATER)) {
                if (blockState.getBlock() instanceof LiquidBlock) {
                    level.setBlock(pos, EventHooks.fireFluidPlaceBlockEvent(level, pos, pos, Blocks.STONE.defaultBlockState()), 3);
                }

                level.levelEvent(1501, pos, 0);
                return;
            }
        }

        super.spreadTo(level, pos, blockState, direction, fluidState);
    }

    public boolean canConvertToSource(FluidState state, Level level, BlockPos pos) {
        return this.canConvertToSource((ServerLevel) level);
    }

    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        level.levelEvent(1501, pos, 0);
    }

    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return state.getHeight(level, pos) >= 0.44444445F && fluid.is(FluidTags.WATER);
    }

    protected float getExplosionResistance() {
        return 100.0F;
    }

    protected @NotNull BlockState createLegacyBlock(@NotNull FluidState state) {
        return (BlockState) ((LiquidBlock) ModBlocks.STEAM_BLOCK.get()).defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    @Override
    public @NotNull FluidType getFluidType() {
        return ModFluidTypes.STEAM_FLUID_TYPE.get();
    }

    public static class Source extends Steam_Fluid {

        public Source(ResourceLocation resourceLocation) {
        }

        public int getAmount(@NotNull FluidState state) {
            return 8;
        }

    }

    public static class Flowing extends Steam_Fluid {

        public Flowing(ResourceLocation resourceLocation) {
        }

        protected void createFluidStateDefinition(StateDefinition.@NotNull Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(new net.minecraft.world.level.block.state.properties.IntegerProperty[]{LEVEL});
        }

        public int getAmount(@NotNull FluidState state) {
            return state.getValue(LEVEL);
        }

        public boolean isSource(@NotNull FluidState state) {
            return false;
        }
    }
}

package net.lade.lademod.fluid;

import java.util.concurrent.ThreadLocalRandom;
import javax.annotation.ParametersAreNonnullByDefault;

import net.lade.lademod.block.ModBlocks;
import net.lade.lademod.fluid.FluidType.ModFluidTypes;
import net.lade.lademod.item.ModItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ThickAirFluid extends BaseFlowingFluid {
    public ThickAirFluid(Properties properties) {
        super(properties);
        this.registerDefaultState((FluidState)((FluidState)this.getStateDefinition().any()).setValue(LEVEL, 8));
    }

    public ThickAirFluid() {
        this(new Properties(ModFluidTypes.THICK_AIR_FLUID_TYPE, ModFluids.THICK_AIR_FLUID, ModFluids.THICK_AIR_FLUID));
    }

    public ThickAirFluid(ResourceLocation resourceLocation) {
        this();
    }

    public Item getBucket() {
        return ModItems.THICK_AIR_BUCKET.get();
    }

    protected boolean canBeReplacedWith(FluidState state, BlockGetter level, BlockPos pos, Fluid fluid, Direction direction) {
        return false;
    }

    public Vec3 getFlow(BlockGetter blockReader, BlockPos pos, FluidState fluidState) {
        return Vec3.ZERO;
    }

    public float getHeight(FluidState state, BlockGetter level, BlockPos pos) {
        return this.getOwnHeight(state);
    }

    public float getOwnHeight(FluidState state) {
        return 1.0F;
    }

    protected BlockState createLegacyBlock(FluidState state) {
        return (ModBlocks.THICK_AIR_BLOCK.get()).defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    public boolean isSource(FluidState state) {
        return true;
    }

    public int getAmount(FluidState state) {
        return 8;
    }

    public VoxelShape getShape(FluidState state, BlockGetter level, BlockPos pos) {
        return Shapes.block();
    }

    @Override
    public FluidType getFluidType() {
        return ModFluidTypes.THICK_AIR_FLUID_TYPE.get();
    }

    public Fluid getFlowing() {
        return this.getSource();
    }

    public Fluid getSource() {
        return ModFluids.THICK_AIR_FLUID.get();
    }

    protected boolean canConvertToSource(Level level) {
        return false;
    }

    protected void beforeDestroyingBlock(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? level.getBlockEntity(pos) : null;
        Block.dropResources(state, level, pos, blockEntity);
    }

    protected int getSlopeFindDistance(LevelReader level) {
        return 0;
    }

    protected int getDropOff(LevelReader level) {
        return 8;
    }

    public int getTickDelay(LevelReader level) {
        return 5;
    }

    protected float getExplosionResistance() {
        return 1000.0F;
    }

    protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
        super.createFluidStateDefinition(builder);
        builder.add(new net.minecraft.world.level.block.state.properties.IntegerProperty[]{LEVEL});
    }

    private double nextDouble(double lower, double upper) {
        return ThreadLocalRandom.current().nextDouble(lower, upper);
    }
}

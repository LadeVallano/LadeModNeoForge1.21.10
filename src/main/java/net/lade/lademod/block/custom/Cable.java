package net.lade.lademod.block.custom;

import net.lade.lademod.ResourceNetwork.ResourceNetwork;
import net.lade.lademod.block.entity.CableEntity;
import net.lade.lademod.block.entity.FluidCableEntity;
import net.lade.lademod.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class Cable<R extends Resource> extends Block implements EntityBlock {

    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;

    public static final BooleanProperty PULL_NORTH = BooleanProperty.create("pull_north");
    public static final BooleanProperty PULL_SOUTH = BooleanProperty.create("pull_south");
    public static final BooleanProperty PULL_EAST = BooleanProperty.create("pull_east");
    public static final BooleanProperty PULL_WEST = BooleanProperty.create("pull_west");
    public static final BooleanProperty PULL_UP = BooleanProperty.create("pull_up");
    public static final BooleanProperty PULL_DOWN = BooleanProperty.create("pull_down");

    public static final BooleanProperty[] ALL_PROPERTIES = new BooleanProperty[]{NORTH, SOUTH, EAST, WEST, UP, DOWN, PULL_NORTH, PULL_SOUTH, PULL_EAST, PULL_WEST, PULL_UP, PULL_DOWN};

    private static final VoxelShape CORE_SHAPE = Block.box(6, 6, 6, 10, 10, 10);

    private static final VoxelShape NORTH_ARM_SHAPE = Block.box(6, 6, 0, 10, 10, 6);
    private static final VoxelShape SOUTH_ARM_SHAPE = Block.box(6, 6, 10, 10, 10, 16);
    private static final VoxelShape EAST_ARM_SHAPE = Block.box(10, 6, 6, 16, 10, 10);
    private static final VoxelShape WEST_ARM_SHAPE = Block.box(0, 6, 6, 6, 10, 10);
    private static final VoxelShape UP_ARM_SHAPE = Block.box(6, 10, 6, 10, 16, 10);
    private static final VoxelShape DOWN_ARM_SHAPE = Block.box(6, 0, 6, 10, 6, 10);
    private static final VoxelShape PULL_NORTH_ARM_SHAPE = Block.box(5, 5, 0, 11, 11, 1);
    private static final VoxelShape PULL_SOUTH_ARM_SHAPE = Block.box(5, 5, 15, 11, 11, 16);
    private static final VoxelShape PULL_EAST_ARM_SHAPE = Block.box(15, 5, 5, 16, 11, 11);
    private static final VoxelShape PULL_WEST_ARM_SHAPE = Block.box(0, 5, 5, 1, 11, 11);
    private static final VoxelShape PULL_UP_ARM_SHAPE = Block.box(5, 15, 5, 11, 16, 11);
    private static final VoxelShape PULL_DOWN_ARM_SHAPE = Block.box(5, 0, 5, 11, 1, 11);

    private static final VoxelShape[] ALL_SHAPES = new VoxelShape[]{NORTH_ARM_SHAPE, SOUTH_ARM_SHAPE, EAST_ARM_SHAPE, WEST_ARM_SHAPE, UP_ARM_SHAPE, DOWN_ARM_SHAPE,
            PULL_NORTH_ARM_SHAPE, PULL_SOUTH_ARM_SHAPE, PULL_EAST_ARM_SHAPE, PULL_WEST_ARM_SHAPE, PULL_UP_ARM_SHAPE, PULL_DOWN_ARM_SHAPE};

    public Cable(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        VoxelShape shape = CORE_SHAPE;
        for (int i = 0; i < ALL_PROPERTIES.length; i++) {
            shape = voxelFromProp(blockState, shape, ALL_SHAPES[i], ALL_PROPERTIES[i]);
        }
        return shape;
    }

    private VoxelShape voxelFromProp(@NotNull BlockState blockState, VoxelShape shape, VoxelShape armShape, BooleanProperty property) {
        if (blockState.getValue(property)) {
            shape = Shapes.or(shape, armShape);
        }
        return shape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, PULL_NORTH, PULL_EAST, PULL_SOUTH, PULL_WEST, PULL_UP, PULL_DOWN);
    }

    protected static BooleanProperty getPropertyFromDirection(Direction direction, boolean pull) {
        if (!pull) {
            return switch (direction) {
                case NORTH -> Cable.NORTH;
                case EAST -> Cable.EAST;
                case SOUTH -> Cable.SOUTH;
                case WEST -> Cable.WEST;
                case UP -> Cable.UP;
                case DOWN -> Cable.DOWN;
            };
        } else {
            return switch (direction) {
                case NORTH -> PULL_NORTH;
                case EAST -> PULL_EAST;
                case SOUTH -> PULL_SOUTH;
                case WEST -> PULL_WEST;
                case UP -> PULL_UP;
                case DOWN -> PULL_DOWN;
            };
        }
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        return super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack itemStack, @NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);

        if (!level.isClientSide() && itemStack.getItem() == Items.STICK && blockEntity instanceof CableEntity cableEntity) {
            Vec3 hitLocation = hitResult.getLocation();
            BlockPos blockPos = hitResult.getBlockPos();
            Direction clostestDirection = getClostestDirection(hitLocation, blockPos);

            switchMode(level, clostestDirection, pos, state, cableEntity);

            blockEntity.setChanged();
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    private static @NotNull Direction getClostestDirection(Vec3 hitLocation, BlockPos blockPos) {
        double relativeX = hitLocation.x - blockPos.getX();
        double relativeY = hitLocation.y - blockPos.getY();
        double relativeZ = hitLocation.z - blockPos.getZ();

        double minDistance = Double.MAX_VALUE;
        Direction clostestDirection = Direction.NORTH;

        if (relativeX < minDistance) {
            minDistance = relativeX;
            clostestDirection = Direction.WEST;
        }

        if (relativeY < minDistance) {
            minDistance = relativeY;
            clostestDirection = Direction.DOWN;
        }

        if (relativeZ < minDistance) {
            minDistance = relativeZ;
            clostestDirection = Direction.NORTH;
        }

        double distSouth = 1.0 - relativeZ;
        if (distSouth < minDistance) {
            minDistance = distSouth;
            clostestDirection = Direction.SOUTH;
        }

        double distEast = 1.0 - relativeX;
        if (distEast < minDistance) {
            minDistance = distEast;
            clostestDirection = Direction.EAST;
        }

        double distUp = 1.0 - relativeY;
        if (distUp < minDistance) {
            clostestDirection = Direction.UP;
        }
        return clostestDirection;
    }

    public static void switchMode(Level level, Direction direction, BlockPos pos, BlockState state, CableEntity cable) {

        BooleanProperty pullProp = getPropertyFromDirection(direction, true);

        if (cable.getPullDirections().contains(direction)) {
            cable.getPullDirections().remove(direction);
            cable.getPushDirections().add(direction);
            level.setBlock(pos, state.setValue(pullProp, false), UPDATE_ALL);
        } else {
            cable.getPullDirections().add(direction);
            cable.getPushDirections().remove(direction);
            level.setBlock(pos, state.setValue(pullProp, true), UPDATE_ALL);
        }
    }


    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!level.isClientSide() && blockEntity instanceof CableEntity cableEntity && cableEntity.getNetwork() == null) {
            cableEntity.createResourceNetwork();
        }

        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    protected void neighborChanged(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
        if (!level.isClientSide()) {
            CableEntity cableEntity = (CableEntity) level.getBlockEntity(pos);
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                boolean shouldConnect = this.canConnect(level, neighborPos, direction.getOpposite());
                BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
                BooleanProperty prop = getPropertyFromDirection(direction, false);

                if (state.getValue(prop) != shouldConnect) {
                    BooleanProperty pullProp = getPropertyFromDirection(direction, true);
                    state = state.setValue(prop, shouldConnect).setValue(pullProp, false);
                }

                if (shouldConnect && neighborEntity instanceof CableEntity neighborCableEntity && cableEntity instanceof CableEntity fluidCable) {

                    ResourceNetwork<R> newNetwork = neighborCableEntity.getNetwork();
                    ResourceNetwork<R> network = fluidCable.getNetwork();

                    if(newNetwork != null && network != null &&  newNetwork != network){
                        network.absorbNetwork(newNetwork);
                    }
                }
            }
            level.setBlock(pos, state, UPDATE_ALL);
        }
        super.neighborChanged(state, level, pos, neighborBlock, null, movedByPiston);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        if(!level.isClientSide()){
            int cableCount = 0;
            for (Direction direction : Direction.values()) {
                BlockPos neighborPos = pos.relative(direction);
                BlockEntity neighborEntity = level.getBlockEntity(neighborPos);
                if(neighborEntity instanceof CableEntity cableEntity){
                    if(cableCount > 0){
                        System.out.println("nnnn");
                        cableEntity.getNetwork().destroy();
                        cableEntity.createResourceNetwork();
                    }
                    cableCount++;
                }
            }

        }
        super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston);
    }

    protected void absorbNetwork(CableEntity<R> networkCable, CableEntity<R> newCable) {


    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> type) {
        if (level.isClientSide()) return null;

        if (type == ModBlockEntities.CABLE_ENTITY_TYPE.get()) {
            return (lvl, pos, st, be) -> {
                if (be instanceof CableEntity cableEntity) cableEntity.tick(level, pos, blockState);
            };
        }
        return null;
    }


    /* Should return whether the adjacent block has the proper capability*/
    protected abstract boolean canConnect(Level level, BlockPos pos, Direction direction);
}

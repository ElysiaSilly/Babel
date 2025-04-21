package com.elysiasilly.babel.api.common.item.cycleable;

import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CycleBlockItem extends BlockItem {

    /// todo block picking (middle mouse click)

    public enum Mode { RANDOM_ONLY, CYCLE_ONLY, RANDOM_AND_CYCLE }

    private final Mode mode;
    private boolean assignToItem = false;

    private final List<PredefinedBlockState> blocks = new ArrayList<>();

    private int random = 0, index = 0, previousIndex = 0;

    public CycleBlockItem(Properties properties, Mode mode, PredefinedBlockState...blocks) {
        super(null, properties);

        this.mode = mode;
        this.blocks.addAll(List.of(blocks));


    }

    ///

    public int size() {
        return blocks().size();
    }

    public boolean shouldAssignToItem() {
        return this.assignToItem;
    }

    public boolean randomOnly() {
        return mode().equals(Mode.RANDOM_ONLY);
    }

    public boolean cycleOnly() {
        return mode().equals(Mode.CYCLE_ONLY);
    }

    public boolean randomAndCycle() {
        return mode().equals(Mode.RANDOM_AND_CYCLE);
    }

    public boolean canRandom() {
        return mode().equals(Mode.RANDOM_ONLY) || mode().equals(Mode.RANDOM_AND_CYCLE);
    }

    public boolean isRandom() {
        return (canRandom() && index() >= blocks().size()) || randomOnly();
    }

    public List<PredefinedBlockState> blocks() {
        return this.blocks;
    }

    public void saveIndex() {
        this.previousIndex = index();
    }

    public void loadIndex() {
        index(this.previousIndex);
    }

    public int index() {
        return this.index;
    }

    public void index(int index) {
        this.index = index;
    }

    public Mode mode() {
        return this.mode;
    }

    public int random() {
        return this.random;
    }

    @Override
    public Block getBlock() {
        return block().block();
    }

    public PredefinedBlockState block(int index) {
        return blocks.get((canRandom() && index >= blocks().size()) || randomOnly() ? random() : index);
    }

    public PredefinedBlockState block() {
        return blocks().get(isRandom() ? random() : index());
    }

    public void nextIndex() {
        index(nextIndex(index()));
    }

    public int nextIndex(int index) {
        return isRandom() || (!canRandom() && index == size() - 1) ? 0 : index + 1;
    }

    public void previousIndex() {
        index(previousIndex(index()));
    }

    public int previousIndex(int index) {
        return index == 0 ? canRandom() ? size() + 1 : size() : index - 1;
    }

    public Block randomBlock(RandomSource random) {
        return blocks().get(random.nextInt(blocks.size())).block();
    }

    public boolean cycleRandom() {
        if(canRandom()) {
            if(isRandom()) {
                saveIndex();
                index(size());
            } else {
                loadIndex();
            }
            return true;
        }
        return false;
    }

    public boolean cyclePrevious() {
        if(randomOnly()) {
            return false;
        } else {
            previousIndex();
            return true;
        }
    }

    public boolean cycleNext() {
        if(randomOnly()) {
            return false;
        } else {
            nextIndex();
            return true;
        }
    }

    public void random(RandomSource random) {
        this.random = random.nextInt(size());
    }

    ///

    public CycleBlockItem assignToItem() {
        this.assignToItem = true; return this;
    }

    ///

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getItemInHand().getCount() < block().cost() && !context.getPlayer().hasInfiniteMaterials()) return InteractionResult.FAIL;

        if(context.getLevel().isClientSide) return InteractionResult.SUCCESS;

        random(context.getLevel().random);

        InteractionResult interactionResult = this.place(new BlockPlaceContext(context));
        if (!interactionResult.consumesAction() && context.getItemInHand().has(DataComponents.FOOD)) {
            InteractionResult altInteractionResult = super.use(context.getLevel(), context.getPlayer(), context.getHand()).getResult();
            return altInteractionResult == InteractionResult.CONSUME ? InteractionResult.CONSUME_PARTIAL : altInteractionResult;
        } else {
            return interactionResult;
        }
    }

    @Override
    protected @Nullable BlockState getPlacementState(BlockPlaceContext context) {
        BlockState blockstate = block().get(context);
        return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
    }

    @Override
    public void registerBlocks(Map<Block, Item> blockToItemMap, Item item) {
        if(shouldAssignToItem()) {
            for(PredefinedBlockState block : blocks()) {
                blockToItemMap.put(block.block(), item);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public String getDescriptionId() {
        String string = randomOnly() ? "" : isRandom() ?
                Component.translatable("misc.babel.random").getString() : Component.translatable(Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this)) + "." + index()).getString();

        return Component.translatable("misc.babel.cycleable").getString().replaceAll("%1", this.getOrCreateDescriptionId()).replaceAll("%2", string);
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if (!this.getBlock().isEnabled(context.getLevel().enabledFeatures())) {
            return InteractionResult.FAIL;
        } else if (!context.canPlace()) {
            return InteractionResult.FAIL;
        } else {
            BlockPlaceContext blockplacecontext = this.updatePlacementContext(context);
            if (blockplacecontext == null) {
                return InteractionResult.FAIL;
            } else {
                BlockState blockstate = this.getPlacementState(blockplacecontext);
                if (blockstate == null) {
                    return InteractionResult.FAIL;
                } else if (!this.placeBlock(blockplacecontext, blockstate)) {
                    return InteractionResult.FAIL;
                } else {
                    BlockPos blockpos = blockplacecontext.getClickedPos();
                    Level level = blockplacecontext.getLevel();
                    Player player = blockplacecontext.getPlayer();
                    ItemStack itemstack = blockplacecontext.getItemInHand();
                    BlockState blockstate1 = level.getBlockState(blockpos);
                    if (blockstate1.is(blockstate.getBlock())) {
                        blockstate1 = this.updateBlockStateFromTag(blockpos, level, itemstack, blockstate1);
                        this.updateCustomBlockEntityTag(blockpos, level, player, itemstack, blockstate1);
                        updateBlockEntityComponents(level, blockpos, itemstack);
                        blockstate1.getBlock().setPlacedBy(level, blockpos, blockstate1, player, itemstack);
                        if (player instanceof ServerPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer)player, blockpos, itemstack);
                        }
                    }

                    SoundType soundtype = blockstate1.getSoundType(level, blockpos, context.getPlayer());
                    level.playSound(
                            player,
                            blockpos,
                            this.getPlaceSound(blockstate1, level, blockpos, context.getPlayer()),
                            SoundSource.BLOCKS,
                            (soundtype.getVolume() + 1.0F) / 2.0F,
                            soundtype.getPitch() * 0.8F
                    );
                    level.gameEvent(GameEvent.BLOCK_PLACE, blockpos, GameEvent.Context.of(player, blockstate1));
                    itemstack.consume(block().cost(), player);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }
}

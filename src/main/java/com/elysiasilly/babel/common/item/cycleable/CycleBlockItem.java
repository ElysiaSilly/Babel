package com.elysiasilly.babel.common.item.cycleable;

import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CycleBlockItem extends BlockItem {

    private final Mode mode;
    private boolean assignToItem = false;

    private final List<BlockPropertyHolder> blocks = new ArrayList<>();
    private int index;
    private final int max;

    private int random;

    public CycleBlockItem(Properties properties, Mode mode, @Nonnull BlockPropertyHolder...blocks) {
        super(null, properties);

        this.mode = mode;
        this.index = this.mode.random ? 0 : 1;

        this.blocks.addAll(List.of(blocks));
        this.max = this.blocks.size();// + 1;
    }

    public CycleBlockItem assignToItem() {
        this.assignToItem = true;
        return this;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        ItemStack item = player.getItemInHand(usedHand);

        if(!player.isShiftKeyDown()) return InteractionResultHolder.pass(item);

        if(level.isClientSide) return InteractionResultHolder.success(item);

        boolean flag = cycleBlock();

        if(flag) player.setItemInHand(usedHand, item);

        return flag ? InteractionResultHolder.success(item) : InteractionResultHolder.pass(item);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if(context.getItemInHand().getCount() < getOptStateBlock().getCost() && !context.getPlayer().hasInfiniteMaterials()) return InteractionResult.FAIL;

        if(context.getLevel().isClientSide) return InteractionResult.SUCCESS;

        this.random = context.getLevel().random.nextInt(blocks.size());

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
        BlockState blockstate = getOptStateBlock().get(context); //this.getBlock().getStateForPlacement(context);
        return blockstate != null && this.canPlace(context, blockstate) ? blockstate : null;
    }

    @Override
    public Block getBlock() {
        return getOptStateBlock().getBlock();
    }

    public BlockPropertyHolder getOptStateBlock() {
        if(index == 0) {
            return this.blocks.get(this.random);
        } else {
            return getOptStateBlock(index);
        }
    }

    public int getIndex() {
        return this.index;
    }

    public int getNextIndex(int index) {
        int min = this.mode.random ? 0 : 1;

        if(index < this.max) {
            index++;
        } else if (index > min) {
            index = min;
        }

        return index;
    }

    public int getPreviousIndex(int index) {
        int min = this.mode.random ? 0 : 1;

        if(index > min && index != 0) {
            index--;
        } else {
            index = this.max;
        }

        return index;
    }

    public Block getRandomBlock(Level level) {
        return this.blocks.get(level.random.nextInt(blocks.size())).getBlock();
    }

    public boolean cycleBlock() {
        if(this.mode == Mode.RANDOM_ONLY) return false;

        this.index = getNextIndex(this.index);

        return true;
    }

    public BlockPropertyHolder getOptStateBlock(int index) {
        return this.blocks.get(index - 1);
    }

    public ResourceLocation getIcon(int index) {
        if(index == 0) {
            return ResourceLocation.parse("null"); // todo
        } else {
            return getOptStateBlock(index).getIcon();
        }
    }

    @Override
    public void registerBlocks(Map<Block, Item> blockToItemMap, Item item) {
        if(assignToItem) {
            for(BlockPropertyHolder block : this.blocks) {
                blockToItemMap.put(block.getBlock(), item);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    @Override
    public String getDescriptionId() {
        return Component.translatable(this.getOrCreateDescriptionId()).getString() + getBlockDescription(index);
    }

    public String getBlockDescription(int index) {
        if(this.mode == Mode.RANDOM_ONLY) {
            return "";
        } else {
            String string = index == 0 ?
                    Component.translatable("tooltip.factory_expansion.random").getString() :
                    Component.translatable(Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this)) + "." + index).getString();
            return " " + "(" + string + ")";
        }
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
                    itemstack.consume(getOptStateBlock().getCost(), player);
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
    }

    public enum Mode {

        RANDOM_ONLY(true, false),
        CYCLE_ONLY(false, true),
        RANDOM_AND_CYCLE(true, true);

        public final boolean random;
        public final boolean selection;

        Mode(boolean random, boolean selection) {
            this.random = random;
            this.selection = selection;
        }
    }
}

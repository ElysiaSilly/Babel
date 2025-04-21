package com.elysiasilly.babel.api.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BabelBE extends BlockEntity {

    public BabelBE(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void markDirty(BlockUpdate update) {
        this.setChanged();
        assert level != null;
        level.sendBlockUpdated(worldPosition, this.getBlockState(), this.getBlockState(), update.value);
    }

    public void tickServer() {}

    public void tickClient() {}

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        serialize(tag, registries);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider lookupProvider) {
        deserialize(tag, lookupProvider);
        super.handleUpdateTag(tag, lookupProvider);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        serialize(tag, registries);
        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        deserialize(tag, registries);
        super.loadAdditional(tag, registries);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider lookupProvider) {
        deserialize(pkt.getTag(), lookupProvider);
    }

    public abstract void serialize(CompoundTag tag, HolderLookup.Provider registries);

    public abstract void deserialize(CompoundTag tag, HolderLookup.Provider registries);
}

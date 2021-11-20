package io.github.darealturtywurty.tutorialmod.common.block.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.github.darealturtywurty.tutorialmod.common.entity.SittableEntity;
import io.github.darealturtywurty.tutorialmod.core.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.Constants.NBT;

public class ToiletBlockEntity extends BlockEntity {

    public SittableEntity seat;
    public int ticks = 0;
    public final Map<UUID, Integer> playerUses = new HashMap<>();

    public ToiletBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.TOILET.get(), pos, state);
    }

    public SittableEntity getOrCreateSeat() {
        if (this.seat == null) {
            final var seat = new SittableEntity(this.level);
            seat.absMoveTo(this.worldPosition.getX() + 0.5D, this.worldPosition.getY(),
                    this.worldPosition.getZ() + 0.5D,
                    getBlockState().getValue(HorizontalDirectionalBlock.FACING).toYRot(), 0.0f);
            this.level.addFreshEntity(seat);
            this.seat = seat;
        }

        return this.seat;
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(this.worldPosition, -1, save(new CompoundTag()));
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        final ListTag playerUses = nbt.getList("PlayerUseMap", NBT.TAG_COMPOUND);
        playerUses.forEach(player -> {
            if (player instanceof final CompoundTag tag) {
                final UUID uuid = tag.getUUID("UUID");
                final int uses = tag.getInt("Uses");
                this.playerUses.put(uuid, uses);
            }
        });
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        getOrCreateSeat();
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        super.save(nbt);

        final var playerUses = new ListTag();
        this.playerUses.forEach((uuid, uses) -> {
            final var playerTag = new CompoundTag();
            playerTag.putUUID("UUID", uuid);
            playerTag.putInt("Uses", uses);
            playerUses.add(playerTag);
        });

        nbt.put("PlayerUseMap", playerUses);
        return nbt;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (this.seat != null) {
            this.seat.kill();
        }
    }

    public void tick() {
        if (this.ticks % 5 == 0 && (this.seat == null || this.seat.isRemoved())) {
            if (this.seat != null) {
                this.seat.kill();
                this.seat = null;
            }
            getOrCreateSeat();
        }
        this.ticks++;
    }
}

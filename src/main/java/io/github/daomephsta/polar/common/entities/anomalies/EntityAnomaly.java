package io.github.daomephsta.polar.common.entities.anomalies;

import static io.github.daomephsta.polar.common.config.PolarConfig.POLAR_CONFIG;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import io.github.daomephsta.polar.api.PolarApi;
import io.github.daomephsta.polar.api.Polarity;
import io.github.daomephsta.polar.common.PolarCommonNetworking;
import io.github.daomephsta.polar.common.advancements.triggers.PolarCriteria;
import io.github.daomephsta.polar.common.core.Constants;
import io.github.daomephsta.polar.common.entities.EntityRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAnomaly extends Entity
{
    private Polarity polarity;
    private long closingTimestamp;
    private boolean open = false;

    public EntityAnomaly(EntityType<EntityAnomaly> entityType, World world)
    {
        super(entityType, world);
        setInvulnerable(true);
    }

    public EntityAnomaly(World world, Polarity polarity)
    {
        this(EntityRegistry.ANOMALY, world);

        this.polarity = polarity;
        int days = POLAR_CONFIG.anomalies().minLifetime() + world.getRandom().nextInt(POLAR_CONFIG.anomalies().maxLifetime() - POLAR_CONFIG.anomalies().minLifetime()); // min to max - 1 days
        int additionalTicks = (int) Math.floor(world.getRandom().nextDouble() * Constants.MC_DAY_TICKS); // Random portion of a day
        // 3 - 8 days
        this.closingTimestamp = world.getTime() + days * Constants.MC_DAY_TICKS + additionalTicks;
    }

    @Override
    public void tick()
    {
        super.tick();
        long closeIn = closingTimestamp - world.getTime();
        if ((closeIn <= 0 && !open) || PolarApi.CHARGE_STORAGE.get(this).getStoredCharge() == 0)
        {
            this.discard();
            return;
        }
        if (world.getTime() % 10 == 0) irradiateNearbyItems();
    }

    private void irradiateNearbyItems()
    {
        Box irradiationBounds = this.getBoundingBox().expand(2.0D);
        for (ItemEntity entityItem : world.getEntitiesByType(EntityType.ITEM, irradiationBounds, 
            item -> item.getItemAge() >= 100))
        {
            ItemStack output = AnomalyIrradiationCrafting.getOutput(getPolarity(), entityItem.getStack());
            if (!output.isEmpty())
            {
                entityItem.discard();

                // Spawn stacks
                if (!world.isClient)
                {
                    // Spawn full stacks
                    for (int i = 0; i < output.getCount() / output.getCount(); i++)
                    {
                        ItemStack fullStack = output.copy();
                        fullStack.setCount(output.getMaxCount());
                        // Spawn up to half a block out
                        double x = entityItem.getX() + (random.nextDouble() - 0.5D);
                        double z = entityItem.getZ() + (random.nextDouble() - 0.5D);
                        ItemEntity fullStackEntity = new ItemEntity(world, x, entityItem.getY(), z, fullStack);
                        world.spawnEntity(fullStackEntity);
                    }
                    // Spawn remainder stack
                    ItemStack remainderStack = output.copy();
                    remainderStack.setCount(output.getCount() % output.getMaxCount());
                    // Spawn up to half a block out
                    double x = entityItem.getX() + (random.nextDouble() - 0.5D);
                    double y = entityItem.getY();
                    double z = entityItem.getZ() + (random.nextDouble() - 0.5D);
                    ItemEntity remainderStackEntity = new ItemEntity(world, x, y, z, remainderStack);
                    world.spawnEntity(remainderStackEntity);
                }
            }
        }
    }
    
    @Override
    public ActionResult interactAt(PlayerEntity player, Vec3d vec, Hand hand)
    {
        if (player instanceof ServerPlayerEntity)
            PolarCriteria.PLAYER_ANOMALY_INTERACTION.handle((ServerPlayerEntity) player);
        return ActionResult.SUCCESS;
    }
    
    @Override
    public boolean damage(DamageSource source, float amount)
    {
        Entity trueSource = source.getAttacker();
        if (trueSource instanceof ServerPlayerEntity)
            PolarCriteria.PLAYER_ANOMALY_INTERACTION.handle((ServerPlayerEntity) trueSource);
        return super.damage(source, amount);
    }
    
    @Override
    public void onPlayerCollision(PlayerEntity player)
    {
        if (player instanceof ServerPlayerEntity)
            PolarCriteria.PLAYER_ANOMALY_INTERACTION.handle((ServerPlayerEntity) player);
    }
    
    @Override
    public boolean collides()
    {
        return true;
    }

    public Polarity getPolarity()
    {
        return polarity;
    }

    public boolean isOpen()
    {
        return open;
    }

    public void open()
    {
        this.open = true;
    }

    public void close()
    {
        this.open = false;
    }
    
    public static void registerComponents(EntityComponentFactoryRegistry registry)
    {
        registry.registerFor(EntityAnomaly.class, PolarApi.CHARGE_STORAGE, 
            entity -> new AnomalyChargeStorage(entity, 1000 + entity.random.nextInt(1000)));
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt)
    {
        this.polarity = Polarity.valueOf(nbt.getString("polarity"));
        if (nbt.contains("closeIn")) this.closingTimestamp = world.getTime() + nbt.getLong("closeIn");
        else this.closingTimestamp = nbt.getLong("closingTimestamp");
    }
    
    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt)
    {
        nbt.putString("polarity", this.polarity.name());
        nbt.putLong("closingTimestamp", this.closingTimestamp);
    }
    
    @Override
    protected void initDataTracker() {}
    
    @Override
    public Packet<?> createSpawnPacket()
    {
        return PolarCommonNetworking.createEntitySpawnPacket(this);
    }
}

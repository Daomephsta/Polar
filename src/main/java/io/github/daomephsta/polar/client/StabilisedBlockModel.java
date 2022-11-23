package io.github.daomephsta.polar.client;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mojang.datafixers.util.Pair;

import io.github.daomephsta.polar.common.Polar;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.rendering.data.v1.RenderAttachedBlockView;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockRenderView;

public class StabilisedBlockModel implements UnbakedModel
{
    public static final StabilisedBlockModel INSTANCE = new StabilisedBlockModel();
    private static final SpriteIdentifier STABILISED_BLOCK_OVERLAY = new SpriteIdentifier(
        PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, Polar.id("blocks/red/stabilised_block_overlay"));

    @Override
    public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> spriteGetter,
        ModelBakeSettings settings, Identifier identifier)
    {
        return new Baked(spriteGetter.apply(STABILISED_BLOCK_OVERLAY));
    }

    @Override
    public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> modelGetter,
        Set<Pair<String, String>> textures)
    {
        return Set.of(STABILISED_BLOCK_OVERLAY);
    }

    @Override
    public Collection<Identifier> getModelDependencies()
    {
        return List.of();
    }

    public static UnbakedModel variantProvider(ModelIdentifier modelId, ModelProviderContext context) throws ModelProviderException
    {
        return modelId.getNamespace().equals(Polar.MOD_ID) && modelId.getPath().equals("stabilised_block")
                ? StabilisedBlockModel.INSTANCE
                : null;
    }

    private static final class Baked implements BakedModel, FabricBakedModel
    {
        private final Sprite overlay;

        private Baked(Sprite overlay)
        {
            this.overlay = overlay;
        }

        @Override
        public List<BakedQuad> getQuads(BlockState state, Direction side, Random random)
        {
            return List.of();
        }

        @Override
        public void emitBlockQuads(BlockRenderView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context)
        {
            BlockState camoState = (BlockState) ((RenderAttachedBlockView) blockView).getBlockEntityRenderAttachment(pos);
            BakedModel model = MinecraftClient.getInstance().getBlockRenderManager().getModel(camoState);
            emitQuads(blockView, pos, randomSupplier, context, camoState, model);
            context.pushTransform(this::retextureTransform);
            emitQuads(blockView, pos, randomSupplier, context, camoState, model);
            context.popTransform();
        }

        public void emitQuads(BlockRenderView blockView, BlockPos pos, Supplier<Random> randomSupplier,
            RenderContext context, BlockState camoState, BakedModel model)
        {
            if (model instanceof FabricBakedModel fabricModel)
                fabricModel.emitBlockQuads(blockView, camoState, pos, randomSupplier, context);
            else
                context.fallbackConsumer().accept(model);
        }

        private boolean retextureTransform(MutableQuadView quadView)
        {
            quadView.spriteBake(0, overlay, MutableQuadView.BAKE_LOCK_UV)
                .colorIndex(-1);
            return true;
        }

        @Override
        public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {}

        @Override
        public boolean useAmbientOcclusion()
        {
            return true;
        }

        @Override
        public boolean hasDepth()
        {
            return true;
        }

        @Override
        public boolean isBuiltin()
        {
            return false;
        }

        @Override
        public Sprite getParticleSprite()
        {
            return overlay;
        }

        @Override
        public ModelOverrideList getOverrides()
        {
            return ModelOverrideList.EMPTY;
        }

        @Override
        public boolean isVanillaAdapter()
        {
            return false;
        }

        @Override
        public ModelTransformation getTransformation()
        {
            return ModelTransformation.NONE;
        }

        @Override
        public boolean isSideLit()
        {
            return false;
        }
    }
}

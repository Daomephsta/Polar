
package io.github.daomephsta.polar.client;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.ImmutableSet;

import io.github.daomephsta.polar.common.Polar;
import io.github.daomephsta.polar.common.tileentities.StabilisedBlockBlockEntity;
import net.fabricmc.fabric.api.client.model.ModelProviderContext;
import net.fabricmc.fabric.api.client.model.ModelProviderException;
import net.fabricmc.fabric.api.client.model.ModelVariantProvider;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext.QuadTransform;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.ModelItemPropertyOverrideList;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ExtendedBlockView;

public class StabilisedBlockModel implements UnbakedModel
{
	public static final StabilisedBlockModel INSTANCE = new StabilisedBlockModel();
	private static final Identifier STABILISED_BLOCK_OVERLAY = new Identifier(Polar.MOD_ID, "blocks/red/stabilised_block_overlay");

	@Override
	public BakedModel bake(ModelLoader loaded, Function<Identifier, Sprite> spriteGetter, ModelBakeSettings bakeSettings)
	{
		return new Baked(spriteGetter.apply(STABILISED_BLOCK_OVERLAY));
	}

	@Override
	public Collection<Identifier> getTextureDependencies(Function<Identifier, UnbakedModel> modelGetter, Set<String> var2)
	{
		return ImmutableSet.of(STABILISED_BLOCK_OVERLAY);
	}

	@Override
	public Collection<Identifier> getModelDependencies()
	{
		return Collections.emptyList();
	}

	private static final class Baked implements BakedModel, FabricBakedModel
	{
		private final Sprite overlay;
		private final QuadTransform retextureTransform;

		private Baked(Sprite overlay)
		{
			this.overlay = overlay;
			retextureTransform = new RetextureTransform(overlay);
		}

		@Override
		public List<BakedQuad> getQuads(BlockState state, Direction side, Random random)
		{
			return Collections.emptyList();
		}

		@Override
		public void emitBlockQuads(ExtendedBlockView blockView, BlockState state, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context)
		{
			BlockEntity blockEntity = blockView.getBlockEntity(pos);
			if (blockEntity instanceof StabilisedBlockBlockEntity)
			{
				BlockState camoState = ((StabilisedBlockBlockEntity) blockEntity).getCamoBlockState();
				BakedModel model = MinecraftClient.getInstance().getBlockRenderManager().getModel(camoState);
				emitQuads(blockView, pos, randomSupplier, context, camoState, model);
				context.pushTransform(retextureTransform);
				emitQuads(blockView, pos, randomSupplier, context, camoState, model);
				context.popTransform();
			}
		}

		public void emitQuads(ExtendedBlockView blockView, BlockPos pos, Supplier<Random> randomSupplier, RenderContext context, BlockState camoState, BakedModel model)
		{
			if (model instanceof FabricBakedModel)
				((FabricBakedModel) model).emitBlockQuads(blockView, camoState, pos, randomSupplier, context);
			else
				context.fallbackConsumer().accept(model);
		}

		private static class RetextureTransform implements QuadTransform
		{
			private final Sprite newTexture;

			private RetextureTransform(Sprite newTexture)
			{
				this.newTexture = newTexture;
			}

			@Override
			public boolean transform(MutableQuadView quadView)
			{
				quadView.spriteBake(0, newTexture, MutableQuadView.BAKE_LOCK_UV)
					.colorIndex(-1);
				return true;
			}
		}

		@Override
		public void emitItemQuads(ItemStack stack, Supplier<Random> randomSupplier, RenderContext context) {}

		@Override
		public boolean useAmbientOcclusion()
		{
			return true;
		}

		@Override
		public boolean hasDepthInGui()
		{
			return true;
		}

		@Override
		public boolean isBuiltin()
		{
			return false;
		}

		@Override
		public Sprite getSprite()
		{
			return overlay;
		}

		@Override
		public ModelItemPropertyOverrideList getItemPropertyOverrides()
		{
			return ModelItemPropertyOverrideList.EMPTY;
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
	}

	public static enum VariantProvider implements ModelVariantProvider
	{
		INSTANCE;

		@Override
		public UnbakedModel loadModelVariant(ModelIdentifier modelId, ModelProviderContext context) throws ModelProviderException
		{
			return modelId.getNamespace().equals(Polar.MOD_ID) && modelId.getPath().equals("stabilised_block")
					? StabilisedBlockModel.INSTANCE
					: null;
		}
	}
}

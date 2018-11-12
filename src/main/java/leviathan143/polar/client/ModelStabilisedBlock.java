package leviathan143.polar.client;

import java.util.*;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;

import daomephsta.umbra.shennanigans.WrapperBlockSafeBlockAccess;
import leviathan143.polar.common.Polar;
import leviathan143.polar.common.blocks.red.BlockStabilised;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

public class ModelStabilisedBlock implements IModel
{
	public static final ModelStabilisedBlock INSTANCE = new ModelStabilisedBlock();
	
	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
	{
		return new BakedModelStabilisedBlock();
	}

	@Override
	public Collection<ResourceLocation> getTextures()
	{
		return ImmutableSet.of(new ResourceLocation("minecraft:blocks/iron_bars"));
	}
	
	private static final class BakedModelStabilisedBlock implements IBakedModel
	{
		private TextureAtlasSprite overlay;
		
		@Override
		@SuppressWarnings("deprecation")
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
		{
			BlockRenderLayer layer = MinecraftForgeClient.getRenderLayer();
			IExtendedBlockState extState = (IExtendedBlockState) state;
			IBlockState camoState = extState.getValue(BlockStabilised.CAMO_STATE);
			IBlockAccess blockAccess = new WrapperBlockSafeBlockAccess(extState.getValue(BlockStabilised.BLOCK_ACCESS));
			BlockPos pos = extState.getValue(BlockStabilised.POSITION);
			//Handle actual states
			camoState = camoState.getBlock().getActualState(camoState, blockAccess, pos);
			//Handle extended states
			camoState = camoState.getBlock().getExtendedState(camoState, blockAccess, pos);
			//Get the camo state's model
			IBakedModel model = Minecraft.getMinecraft().getBlockRendererDispatcher().getModelForState(camoState);
			List<BakedQuad> camoQuads = model.getQuads(camoState, side, rand);
			if (camoState.getBlock().canRenderInLayer(camoState, layer))
				return camoQuads;
			if (layer == BlockRenderLayer.CUTOUT)
				return generateOverlay(camoQuads);
			
			return Collections.emptyList();
		}
		
		private TextureAtlasSprite getOverlayTexture()
		{
			if (overlay == null)
				overlay = Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry("minecraft:blocks/iron_bars");
			return overlay;
		}
		
		private List<BakedQuad> generateOverlay(List<BakedQuad> sourceQuads)
		{
			List<BakedQuad> overlay = new ArrayList<>(sourceQuads.size());
			for (BakedQuad sourceQuad : sourceQuads)
			{
				overlay.add(new BakedQuadRetextured(sourceQuad, getOverlayTexture()));
			}
			return overlay;
		}

		@Override
		public boolean isAmbientOcclusion()
		{
			return true;
		}

		@Override
		public boolean isGui3d()
		{
			return true;
		}

		@Override
		public boolean isBuiltInRenderer()
		{
			return false;
		}

		@Override
		public TextureAtlasSprite getParticleTexture()
		{
			return getOverlayTexture();
		}

		@Override
		public ItemOverrideList getOverrides()
		{
			return ItemOverrideList.NONE;
		}
	}
	
	public static enum ModelStabilisedBlockLoader implements ICustomModelLoader
	{
		INSTANCE;

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) {}

		@Override
		public boolean accepts(ResourceLocation modelLocation)
		{
			return modelLocation.getNamespace().equals(Polar.MODID) && modelLocation.getPath().equals("stabilised_block");
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws Exception
		{
			return ModelStabilisedBlock.INSTANCE;
		}
	}
}

package moonfather.vegan_mod.stupid_fluid;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.Nullable;

public class ClientExtensions {
    private ResourceLocation still;
    private ResourceLocation flowing;
    private ResourceLocation overlay;
    private ResourceLocation renderOverlay;
    private Vector3f fogColor;
    private TintFunction tintFunction;
    private int fixedTint = 0xFFFFFFFF;

    @FunctionalInterface
    public interface TintFunction
    {
        Integer getColor(FluidState state, BlockAndTintGetter getter, BlockPos pos);
    }

    private final String modid;

    private ClientExtensions(String modid, String fluidName) {
        this.modid = modid;
        still(fluidName);
        flowing(fluidName);
        overlay(fluidName);
    }

    public ClientExtensions flowing(String name) {
        return flowing(name, "blocks");
    }

    public ClientExtensions flowing(String name, String folder) {
        this.flowing = ResourceLocation.fromNamespaceAndPath(this.modid, folder + "/" + name + "_flowing");
        return this;
    }

    public ClientExtensions fogColor(float red, float green, float blue) {
        this.fogColor = new Vector3f(red, green, blue);
        return this;
    }

    public ClientExtensions overlay(String name) {
        return overlay(name, "blocks");
    }

    public ClientExtensions overlay(String name, String folder) {
        this.overlay = ResourceLocation.fromNamespaceAndPath(this.modid, folder + "/" + name + "_overlay");
        return renderOverlay(ResourceLocation.fromNamespaceAndPath(this.modid, "textures/" + folder + "/" + name + "_overlay.png"));
    }

    public ClientExtensions renderOverlay(ResourceLocation path) {
        this.renderOverlay = path;
        return this;
    }

    public ClientExtensions still(String name) {
        return still(name, "blocks");
    }

    public ClientExtensions still(String name, String folder) {
        this.still = ResourceLocation.fromNamespaceAndPath(this.modid, folder + "/" + name + "_still");
        return this;
    }

    public ClientExtensions tint(int tint) {
        this.fixedTint = tint;
        return this;
    }

    public ClientExtensions tint(TintFunction tinter) {
        this.tintFunction = tinter;
        return this;
    }

    /////////////////////////////////////

    public static ClientExtensions create(String modid, String fluidName) { return new ClientExtensions(modid, fluidName); }
    public IClientFluidTypeExtensions build() { return createExtension(this); }

    /////////////////////////////////////

    public static IClientFluidTypeExtensions createExtension(ClientExtensions extensions) {
        return new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getFlowingTexture() {
                return extensions.flowing;
            }

            @Nullable
            @Override
            public ResourceLocation getOverlayTexture() {
                return extensions.overlay;
            }

            @Override
            public ResourceLocation getRenderOverlayTexture(Minecraft minecraft) {
                return extensions.renderOverlay;
            }

            @Override
            public ResourceLocation getStillTexture() {
                return extensions.still;
            }

            @Override
            public int getTintColor(FluidState state, BlockAndTintGetter getter, BlockPos pos) {
                return extensions.tintFunction == null ? extensions.fixedTint : extensions.tintFunction.getColor(state, getter, pos);
            }

            @Override
            public @NotNull Vector3f modifyFogColor(Camera camera, float partialTick, ClientLevel level,
                                                    int renderDistance, float darkenWorldAmount, Vector3f fluidFogColor) {
                return extensions.fogColor == null
                        ? IClientFluidTypeExtensions.super.modifyFogColor(camera, partialTick, level, renderDistance,
                        darkenWorldAmount, fluidFogColor)
                        : extensions.fogColor;
            }

            @Override
            public void modifyFogRender(Camera camera, FogRenderer.FogMode mode, float renderDistance, float partialTick,
                                        float nearDistance, float farDistance, FogShape shape) {
                RenderSystem.setShaderFogStart(1f);
                RenderSystem.setShaderFogEnd(6f);
            }
        };
    }
}

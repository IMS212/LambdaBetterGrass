/*
 * Copyright © 2021 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of LambdaBetterGrass.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package dev.lambdaurora.lambdabettergrass.mixin;

import dev.lambdaurora.lambdabettergrass.gui.LBGOption;
import dev.lambdaurora.spruceui.Tooltip;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public abstract class VideoOptionsScreenMixin extends GameOptionsScreen {
	@Unique
	private SimpleOption<?> lbg$option;

	public VideoOptionsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
		super(parent, gameOptions, title);
	}

	@Inject(method = "<init>", at = @At("TAIL"))
	private void onConstruct(Screen parent, MinecraftClient client, GameOptions gameOptions, CallbackInfo ci) {
		this.lbg$option = LBGOption.getOption(this);
	}

	@ModifyArg(
			method = "addOptions",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/gui/widget/OptionListWidget;addAll([Lnet/minecraft/client/option/SimpleOption;)V"
			),
			index = 0
	)
	private SimpleOption<?>[] addOptionButton(SimpleOption<?>[] old) {
		var options = new SimpleOption<?>[old.length + 1];
		System.arraycopy(old, 0, options, 0, old.length);
		options[options.length - 1] = this.lbg$option;
		return options;
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		Tooltip.renderAll(context);
	}
}
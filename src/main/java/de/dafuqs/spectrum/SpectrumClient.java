package de.dafuqs.spectrum;

import de.dafuqs.spectrum.blocks.SpectrumBlocks;
import de.dafuqs.spectrum.blocks.altar.AltarScreen;
import de.dafuqs.spectrum.blocks.altar.SpectrumContainers;
import de.dafuqs.spectrum.blocks.altar.SpectrumScreenHandlerTypes;
import de.dafuqs.spectrum.fluid.SpectrumFluids;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class SpectrumClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SpectrumBlocks.registerClient();
        SpectrumFluids.registerClient();

        SpectrumContainers.register();
        ScreenRegistry.register(SpectrumScreenHandlerTypes.ALTAR_SCREEN_HANDLER, AltarScreen::new);
    }
}
